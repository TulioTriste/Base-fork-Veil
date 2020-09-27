package net.bfcode.bfbase;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import net.bfcode.bfbase.command.CommandManager;
import net.bfcode.bfbase.command.SimpleCommandManager;
import net.bfcode.bfbase.command.module.ChatModule;
import net.bfcode.bfbase.command.module.EssentialModule;
import net.bfcode.bfbase.command.module.InventoryModule;
import net.bfcode.bfbase.command.module.TeleportModule;
import net.bfcode.bfbase.command.module.chat.ToggleMsgCommand;
import net.bfcode.bfbase.command.module.essential.DropsCommand;
import net.bfcode.bfbase.command.module.essential.VanishCommand;
import net.bfcode.bfbase.command.module.essential.VanishStaffCommand;
import net.bfcode.bfbase.command.module.teleport.WorldCommand;
import net.bfcode.bfbase.drops.Drop;
import net.bfcode.bfbase.drops.DropsManager;
import net.bfcode.bfbase.drops.FlatFileDropsManager;
import net.bfcode.bfbase.kit.FlatFileKitManager;
import net.bfcode.bfbase.kit.Kit;
import net.bfcode.bfbase.kit.KitExecutor;
import net.bfcode.bfbase.kit.KitListener;
import net.bfcode.bfbase.kit.KitManager;
import net.bfcode.bfbase.listener.ChatListener;
import net.bfcode.bfbase.listener.ColouredSignListener;
import net.bfcode.bfbase.listener.DecreasedLagListener;
import net.bfcode.bfbase.listener.FreezeHandler;
import net.bfcode.bfbase.listener.JoinListener;
import net.bfcode.bfbase.listener.MoveByBlockEvent;
import net.bfcode.bfbase.listener.NameVerifyListener;
import net.bfcode.bfbase.listener.PlayerLimitListener;
import net.bfcode.bfbase.listener.StaffModeHandler;
import net.bfcode.bfbase.listener.VanishListener;
import net.bfcode.bfbase.task.AnnouncementHandler;
import net.bfcode.bfbase.task.AutoRestartHandler;
import net.bfcode.bfbase.task.ClearEntityHandler;
import net.bfcode.bfbase.user.BaseUser;
import net.bfcode.bfbase.user.ConsoleUser;
import net.bfcode.bfbase.user.NameHistory;
import net.bfcode.bfbase.user.ServerParticipator;
import net.bfcode.bfbase.user.UserManager;
import net.bfcode.bfbase.util.Cooldowns;
import net.bfcode.bfbase.util.PersistableLocation;
import net.bfcode.bfbase.util.RandomUtils;
import net.bfcode.bfbase.util.SignHandler;
import net.bfcode.bfbase.util.bossbar.BossBarManager;
import net.bfcode.bfbase.util.chat.Lang;
import net.bfcode.bfbase.util.cuboid.Cuboid;
import net.bfcode.bfbase.util.cuboid.NamedCuboid;
import net.bfcode.bfbase.util.itemdb.ItemDb;
import net.bfcode.bfbase.util.itemdb.SimpleItemDb;

@Getter
@Setter
public class BasePlugin extends JavaPlugin {
	
	@Getter
    private static BasePlugin plugin;
    public BukkitRunnable announcementTask;
    private ItemDb itemDb;
    public String Configfile;
    private Random random;
    private RandomUtils randomUtils;
    private AutoRestartHandler autoRestartHandler;
    private BukkitRunnable clearEntityHandler;
    private CommandManager commandManager;
    private KitManager kitManager;
    private PlayTimeManager playTimeManager;
    private ServerHandler serverHandler;
    private SignHandler signHandler;
    private UserManager userManager;
    private KitExecutor kitExecutor;
    private DropsManager dropsManager;
    private FreezeHandler freezeHandler;
    private VanishCommand vanishCommand;
    private StaffModeHandler staffModeHandler;
    private VanishListener vanishListener;
    private ToggleMsgCommand togglemsgCommand;
    
    public BasePlugin() {
        random = new Random();
    }
    
    public void onEnable() {
        BasePlugin.plugin = this;
        registerConfig();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        ConfigurationSerialization.registerClass(Drop.class);
        ConfigurationSerialization.registerClass(ServerParticipator.class);
        ConfigurationSerialization.registerClass(BaseUser.class);
        ConfigurationSerialization.registerClass(ConsoleUser.class);
        ConfigurationSerialization.registerClass(NameHistory.class);
        ConfigurationSerialization.registerClass(PersistableLocation.class);
        ConfigurationSerialization.registerClass(Cuboid.class);
        ConfigurationSerialization.registerClass(NamedCuboid.class);
        ConfigurationSerialization.registerClass(Kit.class);
        registerManagers();
        registerCommands();
        registerListeners();
        registerCooldowns();
        reloadSchedulers();
        
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "clearlag 100000");
    }
    
    public void onDisable() {
        super.onDisable();
        BossBarManager.unhook();
        kitManager.saveKitData();
        playTimeManager.savePlaytimeData();
        serverHandler.saveServerData();
        signHandler.cancelTasks(null);
        userManager.saveParticipatorData();
        dropsManager.saveDropData();
        BasePlugin.plugin = null;
        registerConfig();
    }
    
    private void registerManagers() {
        BossBarManager.hook();
        randomUtils = new RandomUtils();
        autoRestartHandler = new AutoRestartHandler(this);
        kitManager = new FlatFileKitManager(this);
        serverHandler = new ServerHandler(this);
        signHandler = new SignHandler(this);
        userManager = new UserManager(this);
        itemDb = new SimpleItemDb(this);
        dropsManager = new FlatFileDropsManager(this);
        try {
            Lang.initialize("en_US");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void registerCommands() {
        (commandManager = new SimpleCommandManager(this)).registerAll(new ChatModule(this));
        commandManager.registerAll(new EssentialModule(this));
        commandManager.registerAll(new InventoryModule(this));
        commandManager.registerAll(new TeleportModule(this));
        kitExecutor = new KitExecutor(this);
        getCommand("kit").setExecutor(kitExecutor);

		Map<String, Map<String, Object>> map = getDescription().getCommands();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			PluginCommand command = getCommand((String) entry.getKey());
			command.setPermission("command." + entry.getKey());
			command.setPermissionMessage(ChatColor.RED.toString() + "You do not have permission to use this command.");
		}
    }
	
	public void registerCooldowns() {
        Cooldowns.createCooldown("REPAIR_ALL");
        Cooldowns.createCooldown("chat_delay");
        Cooldowns.createCooldown("record_delay");
        Cooldowns.createCooldown("newvideo_delay");
        Cooldowns.createCooldown("twitch_delay");
        Cooldowns.createCooldown("TEMPORAL_COORDS");
        Cooldowns.createCooldown("TEMPCOORDS_COMMAND");
    }
    
    public void registerConfig() {
        final File config = new File(getDataFolder(), "config.yml");
        Configfile = config.getPath();
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
    
    private void registerListeners() {
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new WorldCommand(), this);
        manager.registerEvents(new ChatListener(this), this);
        manager.registerEvents(new ColouredSignListener(), this);
        manager.registerEvents(new DecreasedLagListener(this), this);
        manager.registerEvents(new JoinListener(this), this);
        manager.registerEvents(new KitListener(this), this);
        manager.registerEvents(new MoveByBlockEvent(), this);
        manager.registerEvents(new NameVerifyListener(this), this);
        manager.registerEvents(playTimeManager = new PlayTimeManager(this), this);
        manager.registerEvents(new PlayerLimitListener(), this);
        manager.registerEvents(new DropsCommand(this), this);
        manager.registerEvents(new VanishStaffCommand(), this);
        manager.registerEvents(new VanishListener(), this);
        manager.registerEvents(new StaffModeHandler(), this);
        freezeHandler = new FreezeHandler(this);
    }
    
    private void reloadSchedulers() {
        if (clearEntityHandler != null) {
            clearEntityHandler.cancel();
        }
        if (announcementTask != null) {
            announcementTask.cancel();
        }
        final long announcementDelay = serverHandler.getAnnouncementDelay() * 20L;
        final long claggdelay = serverHandler.getClearlagdelay() * 20L;
        final AnnouncementHandler announcementTask = (AnnouncementHandler) (this.announcementTask = new AnnouncementHandler(this));
        final ClearEntityHandler clearEntityHandler = (ClearEntityHandler) (this.clearEntityHandler = new ClearEntityHandler());
        clearEntityHandler.runTaskTimer(this, claggdelay, claggdelay);
        announcementTask.runTaskTimer(this, announcementDelay, announcementDelay);
    }
}
