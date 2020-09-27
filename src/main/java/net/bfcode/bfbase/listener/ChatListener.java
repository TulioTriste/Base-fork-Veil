package net.bfcode.bfbase.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.event.PlayerMessageEvent;
import net.bfcode.bfbase.user.BaseUser;
import net.bfcode.bfbase.user.ServerParticipator;
import net.bfcode.bfbase.util.BukkitUtils;

public class ChatListener implements Listener
{
    private static final String MESSAGE_SPY_FORMAT;
    private static final long AUTO_IDLE_TIME;
    private final BasePlugin plugin;
    
    public ChatListener(final BasePlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerPreMessage(final PlayerMessageEvent event) {
        final Player sender = event.getSender();
        final Player recipient = event.getRecipient();
        final UUID recipientUUID = recipient.getUniqueId();
        if (!sender.hasPermission("base.messaging.bypass")) {
            final BaseUser recipientUser = this.plugin.getUserManager().getUser(recipientUUID);
            if (!recipientUser.isMessagesVisible() || recipientUser.getIgnoring().contains(sender.getName())) {
                event.setCancelled(true);
                sender.sendMessage(ChatColor.RED + recipient.getName() + " has private messaging toggled.");
            }
            return;
        }
        final ServerParticipator senderParticipator = this.plugin.getUserManager().getParticipator((CommandSender)sender);
        if (!senderParticipator.isMessagesVisible()) {
            event.setCancelled(true);
            sender.sendMessage(ChatColor.RED + "You have private messages toggled.");
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMessage(final PlayerMessageEvent event) {
        final Player sender = event.getSender();
        final Player recipient = event.getRecipient();
        final String message = event.getMessage();
        if (BukkitUtils.getIdleTime(recipient) > ChatListener.AUTO_IDLE_TIME) {
            sender.sendMessage(ChatColor.RED + recipient.getName() + " may not respond as their idle time is over " + DurationFormatUtils.formatDurationWords(ChatListener.AUTO_IDLE_TIME, true, true) + '.');
        }
        final UUID senderUUID = sender.getUniqueId();
        final String senderId = senderUUID.toString();
        final String recipientId = recipient.getUniqueId().toString();
        final Collection<CommandSender> recipients = new HashSet<CommandSender>();
        recipients.remove(sender);
        recipients.remove(recipient);
        recipients.add((CommandSender)Bukkit.getConsoleSender());
        for (final CommandSender target : recipients) {
            final ServerParticipator participator = this.plugin.getUserManager().getParticipator(target);
            final Set<String> messageSpying = participator.getMessageSpying();
            if (!messageSpying.contains("all") && !messageSpying.contains(recipientId) && !messageSpying.contains(senderId)) {
                continue;
            }
            target.sendMessage(String.format(Locale.ENGLISH, ChatListener.MESSAGE_SPY_FORMAT, sender.getName(), recipient.getName(), message));
        }
    }
    

    
    static {
        MESSAGE_SPY_FORMAT = ChatColor.GRAY + "[" + ChatColor.GOLD + "SS: " + ChatColor.AQUA + "%1$s" + ChatColor.GRAY + " -> " + ChatColor.AQUA + "%2$s" + ChatColor.GRAY + "] %3$s";
        AUTO_IDLE_TIME = TimeUnit.MINUTES.toMillis(5L);
    }
}
