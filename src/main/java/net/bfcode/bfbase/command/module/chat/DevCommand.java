package net.bfcode.bfbase.command.module.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.bfcode.bfhcf.HCFaction;
import net.bfcode.bfhcf.utils.CC;
import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfbase.util.BukkitUtils;

public class DevCommand extends BaseCommand {
	
	private BasePlugin plugin;

	public DevCommand(BasePlugin plugin) {
        super("dev", "Authors for this plugin.");
        this.setAliases(new String[] { "developer", "hcfcore", "authors" });
        this.plugin = plugin;
	}
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	sender.sendMessage(CC.translate("&7&m" + BukkitUtils.STRAIGHT_LINE_DEFAULT));
    	sender.sendMessage(CC.translate("&6&lHCF-Core oficial version"));
    	sender.sendMessage(CC.translate(""));
		sender.sendMessage(CC.translate(" &f» &eAutores: &fJavaPinq & Risas"));
		sender.sendMessage(CC.translate(" &f» &eVersion: &f" + HCFaction.getPlugin().getDescription().getVersion()));
		sender.sendMessage(CC.translate("&cCopyright » 2019-2020 | JavaPinq & Risas - Todos los derechos reservados."));
    	sender.sendMessage(CC.translate("&7&m" + BukkitUtils.STRAIGHT_LINE_DEFAULT));
        return true;
    }

}
