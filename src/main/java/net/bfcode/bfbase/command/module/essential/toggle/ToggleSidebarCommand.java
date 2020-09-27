package net.bfcode.bfbase.command.module.essential.toggle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.command.BaseCommand;
import net.bfcode.bfhcf.HCFaction;
import net.bfcode.bfhcf.scoreboard.PlayerBoard;
import net.bfcode.bfhcf.utils.CC;

public class ToggleSidebarCommand extends BaseCommand {

	public ToggleSidebarCommand() {
        super("togglesidebar", "Toggle Sidebar");
        this.setAliases(new String[] { "togglesb" });
        this.setUsage("/(command)]");
	}
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	if(!(sender instanceof Player)) {
    		sender.sendMessage(CC.translate("&4No Console."));
    		return true;
    	}
    	Player player = (Player) sender;
    	PlayerBoard board = HCFaction.getPlugin().getScoreboardHandler().getPlayerBoard(player.getUniqueId());
    	if(board.isSidebarVisible()) {
    		player.sendMessage(CC.translate("&cSe ha desactivado la Scoreboard!"));
    		board.setSidebarVisible(false);
    	} else {
    		player.sendMessage(CC.translate("&aSe ha activado la Scoreboard!"));
    		board.setSidebarVisible(true);
    	}
        return true;
    }

}
