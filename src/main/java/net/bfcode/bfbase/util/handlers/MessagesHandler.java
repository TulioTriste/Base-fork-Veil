package net.bfcode.bfbase.util.handlers;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class MessagesHandler
{
    private static List<String> messageList;
    private static List<String> socialList;
    private static List<String> soundList;
    
    public static boolean isMessagesDisabled(final CommandSender sender) {
        return MessagesHandler.messageList.contains(sender.getName());
    }
    
    public static boolean isSocialSpyEnabled(final CommandSender sender) {
        return MessagesHandler.socialList.contains(sender.getName());
    }
    
    public static boolean isSoundDisabled(final CommandSender sender) {
        return MessagesHandler.soundList.contains(sender.getName());
    }
    
    public static void setPrivateMessages(final CommandSender sender, final boolean b) {
        if (b) {
            MessagesHandler.messageList.add(sender.getName());
            sender.sendMessage(ChatColor.RED + "You have disabled your private messages.");
        }
        else {
            MessagesHandler.messageList.remove(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "You have enabled your private messages.");
        }
    }
    
    public static void setSocialSpy(final CommandSender sender, final boolean b) {
        if (b) {
            MessagesHandler.socialList.add(sender.getName());
            sender.sendMessage(ChatColor.YELLOW + "Social Spy is now enabled for you.");
        }
        else {
            MessagesHandler.socialList.remove(sender.getName());
            sender.sendMessage(ChatColor.RED + "Social Spy is now disabled for you.");
        }
    }
    
    public static void setSound(final CommandSender sender, final boolean b) {
        if (b) {
            MessagesHandler.soundList.add(sender.getName());
            sender.sendMessage(ChatColor.RED + "You have disabled your sounds.");
        }
        else {
            MessagesHandler.soundList.remove(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "You have enabled your sounds.");
        }
    }
    
    static {
        MessagesHandler.messageList = new ArrayList<String>();
        MessagesHandler.socialList = new ArrayList<String>();
        MessagesHandler.soundList = new ArrayList<String>();
    }
}
