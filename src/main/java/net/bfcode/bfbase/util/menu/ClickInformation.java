package net.bfcode.bfbase.util.menu;

import java.util.Objects;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.bfcode.bfbase.util.menu.slot.Slot;

public class ClickInformation
{
    private final InventoryInteractEvent handle;
    private final InventoryAction inventoryAction;
    private final ClickType clickType;
    private final Inventory clicked;
    private final Menu clickedMenu;
    private final Slot clickedSlot;
    private final ItemStack addingItem;
    private Event.Result result;
    
    ClickInformation(final InventoryInteractEvent handle, final InventoryAction inventoryAction, final ClickType clickType, final Inventory clicked, final Menu clickedMenu, final Slot clickedSlot, final Event.Result result) {
        this(handle, inventoryAction, clickType, clicked, clickedMenu, clickedSlot, result, null);
    }
    
    ClickInformation(final InventoryInteractEvent handle, final InventoryAction inventoryAction, final ClickType clickType, final Inventory clicked, final Menu clickedMenu, final Slot clickedSlot, final Event.Result result, final ItemStack addingItem) {
        this.handle = handle;
        this.inventoryAction = inventoryAction;
        this.clickType = clickType;
        this.clicked = clicked;
        this.clickedMenu = clickedMenu;
        this.clickedSlot = clickedSlot;
        this.result = result;
        this.addingItem = addingItem;
    }
    
    public Menu getClickedMenu() {
        return this.clickedMenu;
    }
    
    public Slot getClickedSlot() {
        return this.clickedSlot;
    }
    
    public InventoryAction getAction() {
        return this.inventoryAction;
    }
    
    public ClickType getClickType() {
        return this.clickType;
    }
    
    public Event.Result getResult() {
        return this.result;
    }
    
    public void setResult(final Event.Result result) {
        Objects.requireNonNull(result);
        this.result = result;
    }
    
    public ItemStack getAddingItem() {
        if (!this.isAddingItem()) {
            throw new IllegalStateException("Not adding item");
        }
        if (this.addingItem != null) {
            return new ItemStack(this.addingItem);
        }
        if (this.handle instanceof InventoryDragEvent) {
            final InventoryDragEvent event = (InventoryDragEvent)this.handle;
            return event.getNewItems().get(this.clickedSlot.getIndex());
        }
        final InventoryClickEvent clickEvent = (InventoryClickEvent)this.handle;
        return (clickEvent.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) ? clickEvent.getCurrentItem() : clickEvent.getCursor();
    }
    
    public int getItemAmount() {
        switch (this.getAction()) {
            case PLACE_ALL: {
                return this.getAddingItem().getAmount();
            }
            case PLACE_SOME: {
                final ItemStack current = this.getClickedSlot().getItem();
                final int limit = (current == null) ? 64 : current.getType().getMaxStackSize();
                return Math.min(limit, this.getAddingItem().getAmount());
            }
            case PLACE_ONE:
            case PICKUP_ONE:
            case DROP_ONE_SLOT: {
                return 1;
            }
            case PICKUP_HALF: {
                return (int)Math.ceil(this.getClickedSlot().getItem().getAmount() / 2.0);
            }
            case PICKUP_ALL:
            case DROP_ALL_SLOT: {
                return this.getClickedSlot().getItem().getAmount();
            }
            case MOVE_TO_OTHER_INVENTORY: {
                return this.isAddingItem() ? this.getAddingItem().getAmount() : this.getClickedSlot().getItem().getAmount();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public boolean isAddingItem() {
        switch (this.getAction()) {
            case PLACE_ALL:
            case PLACE_SOME:
            case PLACE_ONE:
            case SWAP_WITH_CURSOR:
            case HOTBAR_SWAP: {
                return true;
            }
            case MOVE_TO_OTHER_INVENTORY: {
                return this.handle.getView().getBottomInventory() == this.clicked;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean isTakingItem() {
        switch (this.getAction()) {
            case PICKUP_ONE:
            case PICKUP_HALF:
            case PICKUP_ALL:
            case PICKUP_SOME:
            case SWAP_WITH_CURSOR: {
                return true;
            }
            case MOVE_TO_OTHER_INVENTORY:
            case HOTBAR_SWAP:
            case HOTBAR_MOVE_AND_READD: {
                return this.handle.getView().getTopInventory() == this.clicked;
            }
            default: {
                return this.isDroppingItem();
            }
        }
    }
    
    public boolean isDroppingItem() {
        switch (this.getAction()) {
            case DROP_ONE_SLOT:
            case DROP_ALL_SLOT: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
