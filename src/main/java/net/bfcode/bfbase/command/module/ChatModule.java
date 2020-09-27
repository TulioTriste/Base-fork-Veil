package net.bfcode.bfbase.command.module;

import net.bfcode.bfbase.BasePlugin;
import net.bfcode.bfbase.command.BaseCommandModule;
import net.bfcode.bfbase.command.module.chat.AnnouncementCommand;
import net.bfcode.bfbase.command.module.chat.ClearChatCommand;
import net.bfcode.bfbase.command.module.chat.DevCommand;
import net.bfcode.bfbase.command.module.chat.DisableChatCommand;
import net.bfcode.bfbase.command.module.chat.IgnoreCommand;
import net.bfcode.bfbase.command.module.chat.MediaCommand;
import net.bfcode.bfbase.command.module.chat.MessageCommand;
import net.bfcode.bfbase.command.module.chat.MessageSpyCommand;
import net.bfcode.bfbase.command.module.chat.NewVideoCommand;
import net.bfcode.bfbase.command.module.chat.RecordCommand;
import net.bfcode.bfbase.command.module.chat.ReplyCommand;
import net.bfcode.bfbase.command.module.chat.SlowChatCommand;
import net.bfcode.bfbase.command.module.chat.ToggleMsgCommand;
import net.bfcode.bfbase.command.module.chat.ToggleSoundsCommand;
import net.bfcode.bfbase.command.module.chat.TwitchCommand;

public class ChatModule extends BaseCommandModule
{
    public ChatModule(final BasePlugin plugin) {
        this.commands.add(new ToggleSoundsCommand(plugin));
        this.commands.add(new AnnouncementCommand(plugin));
        this.commands.add(new MediaCommand(plugin));
        this.commands.add(new IgnoreCommand(plugin));
        this.commands.add(new MessageCommand(plugin));
        this.commands.add(new MessageSpyCommand(plugin));
        this.commands.add(new ReplyCommand(plugin));
        this.commands.add(new ClearChatCommand(plugin));
        this.commands.add(new DisableChatCommand(plugin));
        this.commands.add(new SlowChatCommand(plugin));
        this.commands.add(new RecordCommand(plugin));
        this.commands.add(new NewVideoCommand(plugin));
        this.commands.add(new TwitchCommand(plugin));
        this.commands.add(new ToggleMsgCommand(plugin));
        this.commands.add(new DevCommand(plugin));
    }
}
