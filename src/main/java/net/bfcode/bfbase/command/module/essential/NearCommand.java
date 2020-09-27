package net.bfcode.bfbase.command.module.essential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import net.bfcode.bfbase.command.BaseCommand;

public class NearCommand extends BaseCommand
{
    public NearCommand() {
        super("near", "Count entities near a player.");
        this.setUsage("/(command) <playerName>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "No.");
            return false;
        }
        final Player p = (Player)sender;
        final List<String> nearby = this.getNearbyEnemies(p);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &eNearby players: &a(" + nearby.size() + ")"));
        if(nearby.isEmpty()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l * &7None"));
        } else {
            for(String nears : this.getNearbyEnemies(p)) {
            	Player nearplayer = Bukkit.getPlayer(nears.replace("[", "").replace("]", ""));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l * &7" + nearplayer.getName() + " &7[&f" + Math.round(p.getLocation().distance(nearplayer.getLocation())) + "&7]"));
            }	
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    private List<String> getNearbyEnemies(final Player player) {
        final List<String> players = new ArrayList<String>();
        final Collection<Entity> nearby = (Collection<Entity>)player.getNearbyEntities(50.0, 50.0, 50.0);
        for (final Entity entity : nearby) {
            if (entity instanceof Player) {
                final Player target = (Player)entity;
                if (!target.canSee(player)) {
                    continue;
                }
                if (!player.canSee(target)) {
                    continue;
                }
                if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    continue;
                }
                players.add(target.getName());
            }
        }
        return players;
    }
}
