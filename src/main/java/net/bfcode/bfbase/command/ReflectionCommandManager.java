package net.bfcode.bfbase.command;

	import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

import net.bfcode.bfbase.BasePlugin;

public class ReflectionCommandManager implements CommandManager
{
    private static final String PERMISSION_MESSAGE;
    private final Map<String, BaseCommand> commandMap;
    
    public ReflectionCommandManager(final BasePlugin plugin) {
        this.commandMap = new HashMap<String, BaseCommand>();
        final ConsoleCommandSender console = Bukkit.getConsoleSender();
        final Server server = Bukkit.getServer();
        server.getScheduler().runTaskLater((Plugin)plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                final Optional optionalCommandMap = ReflectionCommandManager.this.getCommandMap(server);
                if (!optionalCommandMap.isPresent()) {
                    Bukkit.broadcastMessage("[" + plugin.getDescription().getFullName() + "] Command map not found");
                    console.sendMessage("[" + plugin.getDescription().getFullName() + "] Command map not found");
                    return;
                }
                final CommandMap bukkitCommandMap = (CommandMap) optionalCommandMap.get();
                for (final BaseCommand command : ReflectionCommandManager.this.commandMap.values()) {
                    final String commandName = command.getName();
                    final Optional optional = ReflectionCommandManager.this.getPluginCommand(commandName, (Plugin)plugin);
                    if (optional.isPresent()) {
                        final PluginCommand pluginCommand = (PluginCommand) optional.get();
                        pluginCommand.setAliases((List)Arrays.asList(command.getAliases()));
                        pluginCommand.setDescription(command.getDescription());
                        pluginCommand.setExecutor((CommandExecutor)command);
                        pluginCommand.setTabCompleter((TabCompleter)command);
                        pluginCommand.setUsage(command.getUsage());
                        if(!command.getName().contains("Dev")) {
                            pluginCommand.setPermission(command.getPermission());
                            pluginCommand.setPermissionMessage(ReflectionCommandManager.PERMISSION_MESSAGE);
                        }
                        bukkitCommandMap.register(plugin.getDescription().getName(), (Command)pluginCommand);
                    }
                    else {
                        Bukkit.broadcastMessage("[" + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
                        console.sendMessage("[" + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
                    }
                }
            }
        }, 1L);
    }
    
    @Override
    public boolean containsCommand(final BaseCommand command) {
        return this.commandMap.containsValue(command);
    }
    
    @Override
    public void registerAll(final BaseCommandModule module) {
        if (module.isEnabled()) {
            final Set<BaseCommand> commands = module.getCommands();
            for (final BaseCommand command : commands) {
                this.commandMap.put(command.getName(), command);
            }
        }
    }
    
    @Override
    public void registerCommand(final BaseCommand command) {
        this.commandMap.put(command.getName(), command);
    }
    
    @Override
    public void registerCommands(final BaseCommand[] commands) {
        for (final BaseCommand command : commands) {
            this.commandMap.put(command.getName(), command);
        }
    }
    
    @Override
    public void unregisterCommand(final BaseCommand command) {
        this.commandMap.values().remove(command);
    }
    
    @Override
    public BaseCommand getCommand(final String id) {
        return this.commandMap.get(id);
    }
    
    private Optional<PluginCommand> getPluginCommand(final String name, final Plugin plugin) {
        try {
            final Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            return Optional.of(constructor.newInstance(name, plugin));
        }
        catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
    
    private Optional<Object> getCommandMap(final Server server) {
        final PluginManager pluginManager = server.getPluginManager();
        if (pluginManager instanceof SimplePluginManager) {
            try {
                final Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                return Optional.of(field.get(pluginManager));
            }
            catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    
    static {
        PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission for this command.";
    }
}
