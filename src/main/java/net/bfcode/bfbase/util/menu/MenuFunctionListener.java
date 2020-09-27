package net.bfcode.bfbase.util.menu;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.bfcode.bfbase.util.menu.slot.ClickOptions;
import net.bfcode.bfbase.util.menu.slot.Slot;

public final class MenuFunctionListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void handleGuiDrag(final InventoryDragEvent event) {
        final InventoryView view = event.getView();
        final Inventory top = view.getTopInventory();
        if (top.getHolder() instanceof Menu) {
            final Menu menu = (Menu)top.getHolder();
            final ClickType clickType = (event.getType() == DragType.EVEN) ? ClickType.LEFT : ClickType.RIGHT;
            final Map<Integer, ItemStack> newItems = (Map<Integer, ItemStack>)event.getNewItems();
            for (final Map.Entry<Integer, ItemStack> entry : newItems.entrySet()) {
                final int index = entry.getKey();
                final ItemStack item = entry.getValue();
                if (index < top.getSize()) {
                    final InventoryAction action = (item.getAmount() > 1) ? InventoryAction.PLACE_SOME : InventoryAction.PLACE_ONE;
                    this.passClickToSlot((InventoryInteractEvent)event, action, clickType, top, menu, index, event.getNewItems().get(index));
                    if (event.getResult() == Event.Result.DENY) {
                        return;
                    }
                    continue;
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void handleGuiClick(final InventoryClickEvent event) {
        final InventoryView view = event.getView();
        final Inventory top = view.getTopInventory();
        if (top.getHolder() instanceof Menu) {
            final Menu menu = (Menu)top.getHolder();
            final Inventory clicked = event.getClickedInventory();
            final InventoryAction action = event.getAction();
            switch (action) {
                case DROP_ALL_CURSOR:
                case DROP_ONE_CURSOR:
                case COLLECT_TO_CURSOR: {
                    event.setResult(Event.Result.DENY);
                    event.setCancelled(true);
                }
                case DROP_ALL_SLOT:
                case DROP_ONE_SLOT:
                case PICKUP_ALL:
                case PICKUP_HALF:
                case PICKUP_ONE:
                case PICKUP_SOME:
                case HOTBAR_MOVE_AND_READD:
                case PLACE_ALL:
                case PLACE_ONE:
                case PLACE_SOME:
                case HOTBAR_SWAP:
                case SWAP_WITH_CURSOR: {
                    if (clicked == top) {
                        this.passClickToSlot(event, menu, event.getSlot());
                        break;
                    }
                    break;
                }
                case MOVE_TO_OTHER_INVENTORY: {
                    if (clicked == top) {
                        this.passClickToSlot(event, menu, event.getSlot());
                        break;
                    }
                    final ItemStack moving = event.getCurrentItem();
                    for (int amountLeft = moving.getAmount(), nextAvailableSlot = this.getNextAvailableSlot(top, moving); nextAvailableSlot > -1 && amountLeft > 0 && event.getResult() != Event.Result.DENY; nextAvailableSlot = this.getNextAvailableSlot(top, moving, nextAvailableSlot + 1)) {
                        final ItemStack inSlot = top.getItem(nextAvailableSlot);
                        int maxAvailable;
                        if (inSlot == null || inSlot.getType() == Material.AIR) {
                            maxAvailable = moving.getMaxStackSize();
                        }
                        else {
                            maxAvailable = inSlot.getMaxStackSize() - inSlot.getAmount();
                        }
                        maxAvailable = Math.min(maxAvailable, amountLeft);
                        amountLeft -= maxAvailable;
                        final ItemStack adding = new ItemStack(moving);
                        adding.setAmount(maxAvailable);
                        this.passClickToSlot((InventoryInteractEvent)event, event.getAction(), event.getClick(), event.getClickedInventory(), menu, nextAvailableSlot, adding);
                    }
                    break;
                }
			default:
				break;
            }
        }
    }
    
    private int getNextAvailableSlot(final Inventory inventory, final ItemStack moving) {
        return this.getNextAvailableSlot(inventory, moving, 0);
    }
    
    private int getNextAvailableSlot(final Inventory inventory, final ItemStack moving, final int startPosition) {
        for (int targetSlot = startPosition; targetSlot < inventory.getSize(); ++targetSlot) {
            final ItemStack inSlot = inventory.getItem(targetSlot);
            if (moving.isSimilar(inSlot) && inSlot.getAmount() < inSlot.getMaxStackSize()) {
                return targetSlot;
            }
        }
        return inventory.firstEmpty();
    }
    
    private void passClickToSlot(final InventoryClickEvent event, final Menu menu, final int slotIndex) {
        this.passClickToSlot((InventoryInteractEvent)event, event.getAction(), event.getClick(), event.getClickedInventory(), menu, slotIndex);
    }
    
    private void passClickToSlot(final InventoryInteractEvent handle, final InventoryAction inventoryAction, final ClickType clickType, final Inventory clicked, final Menu menu, final int slotIndex) {
        this.passClickToSlot(handle, inventoryAction, clickType, clicked, menu, slotIndex, null);
    }
    
    private void passClickToSlot(final InventoryInteractEvent handle, final InventoryAction inventoryAction, final ClickType clickType, final Inventory clicked, final Menu menu, final int slotIndex, final ItemStack addingItem) {
        final Slot slot = menu.getSlot(slotIndex);
        final ClickOptions options = slot.getClickOptions();
        if (!options.isAllowedClickType(clickType) || !options.isAllowedAction(inventoryAction)) {
            handle.setResult(Event.Result.DENY);
        }
        final ClickInformation clickInformation = new ClickInformation(handle, inventoryAction, clickType, clicked, menu, slot, handle.getResult(), addingItem);
        if (slot.getClickHandler().isPresent()) {
            slot.getClickHandler().get().click((Player)handle.getWhoClicked(), clickInformation);
        }
        handle.setResult(clickInformation.getResult());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void preventShiftClickInCustomTiles(final InventoryClickEvent event) {
        final Inventory top = event.getView().getTopInventory();
        if (top.getHolder() instanceof Menu && this.isShiftClickingBlocked(top.getType()) && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
        }
    }
    
    private boolean isShiftClickingBlocked(final InventoryType type) {
        switch (type) {
            case HOPPER:
            case WORKBENCH:
            case DROPPER:
            case DISPENSER: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @EventHandler
    public void triggerCloseHandler(final InventoryCloseEvent event) {
        final Inventory closed = event.getInventory();
        if (closed.getHolder() instanceof Menu) {
            final Menu menu = (Menu)closed.getHolder();
            menu.getCloseHandler().ifPresent(h -> h.close((Player)event.getPlayer(), menu));
        }
    }
}
