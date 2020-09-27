package net.bfcode.bfbase.command.module.essential;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.ImmutableList;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.user.BaseUser;
import net.bfcode.bfbase.util.BukkitUtils;
import net.bfcode.bfbase.util.CC;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class IpHistoryCommand extends BaseCommand {
    private static final List COMPLETIONS_FIRST;

    static {
        COMPLETIONS_FIRST = ImmutableList.of("player", "address");
    }

    private final BasePlugin plugin;

    public IpHistoryCommand(final BasePlugin plugin) {
        super("iphistory", "Checks data about IP addresses or players.");
        this.setUsage("/(command) <player|address>");
        this.plugin = plugin;
    }

    private Set getSharingPlayerNames(final String ipAddress) {
        return Collections.emptySet();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        new BukkitRunnable() {
            public void run() {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: " + getUsage());
                    return;
                }
                if (!args[0].equalsIgnoreCase("player")) {
                    if (!args[0].equalsIgnoreCase("address")) {
                        sender.sendMessage(ChatColor.RED + "Usage: " + getUsage());
                        return;
                    }
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[0].toLowerCase() + " <address>");
                        return;
                    }
                    final Set sharingNames1 = getSharingPlayerNames(args[1]);
                    if (sharingNames1.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "No players share the ip '" + args[1] + "'.");
                        return;
                    }
                    sender.sendMessage(CC.YELLOW + "IP address: " + args[1] + " is shared by: " + CC.GOLD + StringUtils.join((Iterable) sharingNames1, ", "));
                } else {
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[0].toLowerCase() + " <playerName>");
                        return;
                    }
                    OfflinePlayer sharingNames2 = BukkitUtils.offlinePlayerWithNameOrUUID(args[1]);
                    if (!sharingNames2.hasPlayedBefore() && sharingNames2.getPlayer() == null) {
                        sender.sendMessage(CC.GOLD + "Player named or with UUID '" + ChatColor.WHITE + args[1] + CC.GOLD + "' not found.");
                        return;
                    }
                    BaseUser baseUser = plugin.getUserManager().getUser(sharingNames2.getUniqueId());
                    sender.sendMessage(CC.GOLD + " IP Addresses used by " + sharingNames2.getName() + ": ");
                    List<String> ipList = baseUser.getAddressHistories();
                    for (String address : ipList) {
                        sender.sendMessage(CC.GRAY + "  " + address + CC.YELLOW + ": [" + StringUtils.join(getSharingPlayerNames(address), ", ") + ']');
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }

    @Override
    public List onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? IpHistoryCommand.COMPLETIONS_FIRST : ((args.length == 2 && args[0].equalsIgnoreCase("player")) ? null : Collections.emptyList());
    }
}
