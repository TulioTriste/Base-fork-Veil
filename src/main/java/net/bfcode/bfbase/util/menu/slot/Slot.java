package net.bfcode.bfbase.util.menu.slot;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.bfcode.bfbase.util.menu.ClickInformation;

public interface Slot
{
    int getIndex();
    
    ClickOptions getClickOptions();
    
    void setClickOptions(final ClickOptions p0);
    
    ItemStack getItem();
    
    void setItem(final ItemStack p0);
    
    Optional<ClickHandler> getClickHandler();
    
    void setClickHandler(final ClickHandler p0);
    
    @FunctionalInterface
    public interface ClickHandler
    {
        void click(final Player p0, final ClickInformation p1);
    }
}
