package net.bfcode.bfbase.command.module;

import org.bukkit.plugin.Plugin;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommandModule;
import net.bfcode.bfbase.command.module.teleport.BackCommand;
import net.bfcode.bfbase.command.module.teleport.LobbyCommand;
import net.bfcode.bfbase.command.module.teleport.TeleportAllCommand;
import net.bfcode.bfbase.command.module.teleport.TeleportCommand;
import net.bfcode.bfbase.command.module.teleport.TeleportHereCommand;
import net.bfcode.bfbase.command.module.teleport.TopCommand;
import net.bfcode.bfbase.command.module.teleport.WorldCommand;

public class TeleportModule extends BaseCommandModule
{
    public TeleportModule(final BasePlugin plugin) {
        this.commands.add(new LobbyCommand((Plugin)plugin));
        this.commands.add(new BackCommand(plugin));
        this.commands.add(new TeleportCommand());
        this.commands.add(new TeleportAllCommand());
        this.commands.add(new TeleportHereCommand());
        this.commands.add(new TopCommand());
        this.commands.add(new WorldCommand());
    }
}
