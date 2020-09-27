package net.bfcode.bfbase.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

public class StaffModeEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers;
    private final boolean staffMode;
    private boolean cancelled;
    
    public StaffModeEvent(final Player player, final boolean staffMode) {
        super(player);
        this.staffMode = staffMode;
    }
    
    public static HandlerList getHandlerList() {
        return StaffModeEvent.handlers;
    }
    
    public boolean isStaffMode() {
        return this.staffMode;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public HandlerList getHandlers() {
        return StaffModeEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
