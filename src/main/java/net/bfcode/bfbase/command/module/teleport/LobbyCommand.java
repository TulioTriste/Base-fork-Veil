package net.bfcode.bfbase.command.module.teleport;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

public class LobbyCommand extends BaseCommand
{
    private static final long LOBBY_DELAY;
    private final Map<UUID, Long> lobbyMap;
    private Plugin main;
    
    public LobbyCommand(final Plugin plugin) {
        super("lobby", "Goes to lobby.");
        this.setAliases(new String[] { "hub", "leaveserver" });
        this.setUsage("/(command) <world>");
        this.main = plugin;
        this.lobbyMap = Maps.newHashMap();
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        final Player player = (Player)sender;
        final UUID uuid = player.getUniqueId();
        final long millis = System.currentTimeMillis();
        final Long lastReport = this.lobbyMap.get(uuid);
        if (lastReport != null && lastReport - millis > 0L) {
            sender.sendMessage(ChatColor.RED + "You have already used this command in the last " + DurationFormatUtils.formatDurationWords(LobbyCommand.LOBBY_DELAY, true, true) + '.');
            return true;
        }
        this.lobbyMap.put(uuid, millis + LobbyCommand.LOBBY_DELAY);
        final Player p = (Player)sender;
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            p.sendMessage(ChatColor.GREEN + "You have been sent to the lobby.");
            out.writeUTF("Connect");
            out.writeUTF(BasePlugin.getPlugin().getConfig().getString("lobby-in-bungee"));
        }
        catch (IOException e) {
            p.sendMessage(ChatColor.RED + "Error while trying to connect to the lobby.");
        }
        p.sendPluginMessage(this.main, "BungeeCord", b.toByteArray());
        return true;
    }
    
    static {
        LOBBY_DELAY = TimeUnit.SECONDS.toMillis(30L);
    }
}
