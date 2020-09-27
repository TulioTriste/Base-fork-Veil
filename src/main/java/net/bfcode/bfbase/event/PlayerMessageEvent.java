package net.bfcode.bfbase.event;

import dev.hatsur.library.Library;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import net.bfcode.bfhcf.utils.CC;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.user.BaseUser;
import net.bfcode.bfbase.util.chat.StaffMessage;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class PlayerMessageEvent extends Event implements Cancellable {
	
    private static final HandlerList handlers;
    private final Player sender;
    private final UUID uuid;
    private final Player recipient;
    private final String message;
    private final boolean isReply;
    private boolean cancelled;
    
    public PlayerMessageEvent(final Player sender, final Set<Player> recipients, final String message, final boolean isReply, final UUID uuid) {
        this.cancelled = false;
        this.sender = sender;
        this.uuid = uuid;
        this.recipient = Iterables.getFirst(recipients, null);
        this.message = message;
        this.isReply = isReply;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerMessageEvent.handlers;
    }
    
    public Player getSender() {
        return this.sender;
    }
    
    public Player getRecipient() {
        return this.recipient;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public boolean isReply() {
        return this.isReply;
    }
    
    @SuppressWarnings("static-access")
	public void send() {
        Preconditions.checkNotNull(this.sender, "The sender cannot be null");
        Preconditions.checkNotNull(this.recipient, "The recipient cannot be null");
        final BaseUser sendingUser = BasePlugin.getPlugin().getUserManager().getUser(this.sender.getUniqueId());
        final BaseUser recipientUser = (BasePlugin.getPlugin().getUserManager().getUser(this.recipient.getUniqueId()));
        sendingUser.setLastRepliedTo(recipientUser.getUniqueId());
        recipientUser.setLastRepliedTo(sendingUser.getUniqueId());
        final long millis = System.currentTimeMillis();
        recipientUser.setLastReceivedMessageMillis(millis);
        final String displayName = Library.getInstance().getPermissionManager().getCurrentPermissionAdapter().getProfile(((Player)this.sender).getUniqueId()).getColoredUsername();

        final String displayName2 = Library.getInstance().getPermissionManager().getCurrentPermissionAdapter().getProfile(((Player)this.recipient).getUniqueId()).getColoredUsername();
        final String[] array = new String[] { "kys", "nigger", "kill", "cunt", "hack", "phase", "aura", "killaura", "forceop", "exploit", "theboys", "antikb", "grief", "worldedit" };
        for (final String strings : array) {
            if (this.message.contains(strings)) {
                StaffMessage.broadcastFilter(this.sender, ChatColor.translateAlternateColorCodes('&', "&7&o-> " + this.recipient.getName() + "&7&o: &7&o" + this.message.replace(strings, ChatColor.RED + "" + ChatColor.ITALIC + strings + ChatColor.GRAY + "" + ChatColor.ITALIC)), false);
                break;
            }
        }
        if(BasePlugin.getPlugin().getTogglemsgCommand().msg.contains(this.recipient.getName()) && !this.sender.hasPermission("rank.staff")) {
        	this.sender.sendMessage(CC.translate("&cThis player has private messaging toggled."));
        	return;
        }
        this.sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GRAY + "To " + displayName2 + ChatColor.GRAY + ") " + ChatColor.GRAY + this.message);
        this.recipient.sendMessage(ChatColor.GRAY + "(" + ChatColor.GRAY + "From " + displayName + ChatColor.GRAY + ") " + ChatColor.GRAY + this.message);
        return;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public HandlerList getHandlers() {
        return PlayerMessageEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
