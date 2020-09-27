package net.bfcode.bfbase.command.module.essential;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.CC;
import net.minecraft.util.com.google.common.primitives.Ints;

public class SetMaxPlayersCommand extends BaseCommand {
	
	public SetMaxPlayersCommand() {
        super("setmaxplayers", "");
        this.setUsage("/(command) <number>");
	}
	
    public static void setMaxPlayers(final int amount) throws ReflectiveOperationException {
        final String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        final Object playerlist = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".CraftServer").getDeclaredMethod("getHandle", (Class<?>[])null).invoke(Bukkit.getServer(), (Object[])null);
        final Field maxplayers = playerlist.getClass().getSuperclass().getDeclaredField("maxPlayers");
        maxplayers.setAccessible(true);
        maxplayers.set(playerlist, amount);
    }
	
    @SuppressWarnings("static-access")
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("command.setmaxplayers")) {
            sender.sendMessage(CC.translate("&cYou do not have permissions!"));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: /" + label + " <amount>"));
        }
        else {
            final Integer amount = Ints.tryParse(args[0]);
            if (amount == null) {
                sender.sendMessage(CC.translate("&c'" + args[0] + "' is not a valid number."));
                return true;
            }
            if (amount <= 0) {
                sender.sendMessage(CC.translate("&cNumber must be positive."));
                return true;
            }
            try {
                this.setMaxPlayers(amount);
            }
            catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            Command.broadcastCommandMessage(sender, CC.translate("&eSet the maximum players to &a" + amount + "&e."));
        }
        return true;
    }
}
