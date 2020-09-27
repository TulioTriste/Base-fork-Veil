package net.bfcode.bfbase.command.module;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommandModule;
import net.bfcode.bfbase.command.module.inventory.ClearInvCommand;
import net.bfcode.bfbase.command.module.inventory.CopyInvCommand;
import net.bfcode.bfbase.command.module.inventory.GiveCommand;
import net.bfcode.bfbase.command.module.inventory.IdCommand;
import net.bfcode.bfbase.command.module.inventory.InvSeeCommand;
import net.bfcode.bfbase.command.module.inventory.ItemCommand;
import net.bfcode.bfbase.command.module.inventory.MoreCommand;
import net.bfcode.bfbase.command.module.inventory.SkullCommand;

public class InventoryModule extends BaseCommandModule
{
    public InventoryModule(final BasePlugin plugin) {
        this.commands.add(new ClearInvCommand());
        this.commands.add(new GiveCommand());
        this.commands.add(new IdCommand());
        this.commands.add(new InvSeeCommand(plugin));
        this.commands.add(new ItemCommand());
        this.commands.add(new MoreCommand());
        this.commands.add(new SkullCommand());
        this.commands.add(new CopyInvCommand());
    }
}
