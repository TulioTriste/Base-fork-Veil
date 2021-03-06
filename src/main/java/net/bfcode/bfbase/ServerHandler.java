package net.bfcode.bfbase;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;


public class ServerHandler
{
    private final List<String> announcements;
    private final List<String> serverRules;
    private final BasePlugin plugin;
    public boolean useProtocolLib;
    private Boolean kitmap;
    private int clearlagdelay;
    private int announcementDelay;
    private long chatSlowedMillis;
    private long chatDisabledMillis;
    private int chatSlowedDelay;
    private String broadcastFormat;
    private FileConfiguration config;
    private boolean decreasedLagMode;
    private boolean end;
    private Location endExit;
    private boolean donorOnly;
    private int worldBorder;
    private int netherBorder;
    private int endBorder;
    
    public ServerHandler(final BasePlugin plugin) {
        this.announcements = new ArrayList<String>();
        this.serverRules = new ArrayList<String>();
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.reloadServerData();
    }
    
    public void setServerBorder(final World.Environment environment, final Integer integer) {
        if (environment.equals(World.Environment.NORMAL)) {
            this.worldBorder = integer;
        }
        else if (environment.equals(World.Environment.NETHER)) {
            this.netherBorder = integer;
        }
        else if (environment.equals(World.Environment.THE_END)) {
            this.endBorder = integer;
        }
    }
    
    private void reloadServerData() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
        final String exitWorld = this.config.getString("end.exitLocation.world", "world");
        final double x = this.config.getDouble("end.exitLocation.x", 0.0);
        final double y = this.config.getDouble("end.exitLocation.y", 66.0);
        final double z = this.config.getDouble("end.exitLocation.z", -200.0);
        this.endExit = new Location(Bukkit.getWorld(exitWorld), x, y, z);
        this.donorOnly = this.config.getBoolean("donor-only-enter");
        this.end = this.config.getBoolean("end-open");
        this.worldBorder = this.config.getInt("border.worldBorder", 3000);
        this.netherBorder = this.config.getInt("border.netherBorder", 1000);
        this.endBorder = this.config.getInt("border.endBorder", 1500);
        this.serverRules.clear();
        this.clearlagdelay = this.config.getInt("clearlag.delay", 100000);
        this.announcementDelay = this.config.getInt("announcements.delay", 15);
        this.announcements.clear();
        for (final String each : this.config.getStringList("announcements.list")) {
            this.announcements.add(ChatColor.translateAlternateColorCodes('&', each));
        }
        this.chatDisabledMillis = this.config.getLong("chat.disabled.millis", 0L);
        this.chatSlowedMillis = this.config.getLong("chat.slowed.millis", 0L);
        this.chatSlowedDelay = this.config.getInt("chat.slowed.delay", 15);
        this.useProtocolLib = this.config.getBoolean("use-protocol-lib", true);
        this.decreasedLagMode = this.config.getBoolean("decreased-lag-mode");
        this.broadcastFormat = ChatColor.translateAlternateColorCodes('&', this.config.getString("broadcast.format", ChatColor.AQUA + " &7%1$s"));
        this.kitmap = this.config.getBoolean("kit-map");
    }
    
    public void saveServerData() {
        this.config.set("clearlag.delay", this.clearlagdelay);
        this.config.set("server-rules", this.serverRules);
        this.config.set("use-protocol-lib", this.useProtocolLib);
        this.config.set("chat.disabled.millis", this.chatDisabledMillis);
        this.config.set("chat.slowed.millis", this.chatSlowedMillis);
        this.config.set("chat.slowed-delay", this.chatSlowedDelay);
        this.config.set("announcements.delay", this.announcementDelay);
        this.config.set("announcements.list", this.announcements);
        this.config.set("kit-map", this.kitmap);
        this.config.set("decreased-lag-mode", this.decreasedLagMode);
        this.config.set("end.exitLocation.world", this.endExit.getWorld().getName());
        this.config.set("end.exitLocation.x", this.endExit.getX());
        this.config.set("end.exitLocation.y", this.endExit.getY());
        this.config.set("end.exitLocation.z", this.endExit.getX());
        this.config.set("donor-only-enter", this.donorOnly);
        this.config.set("end-open", this.end);
        this.config.set("border.worldBorder", this.worldBorder);
        this.config.set("border.netherBorder", this.netherBorder);
        this.config.set("border.endBorder", this.endBorder);
        this.plugin.saveConfig();
    }
    
    public void setChatSlowedMillis(final long ticks) {
        this.chatSlowedMillis = System.currentTimeMillis() + ticks;
    }
    
    public long getRemainingChatSlowedMillis() {
        return this.chatSlowedMillis - System.currentTimeMillis();
    }
    
    public boolean isChatDisabled() {
        return this.getRemainingChatDisabledMillis() > 0L;
    }
    
    public long getChatDisabledMillis() {
        return this.chatDisabledMillis;
    }
    
    public void setChatDisabledMillis(final long ticks) {
        final long millis = System.currentTimeMillis();
        this.chatDisabledMillis = millis + ticks;
    }
    
    public long getRemainingChatDisabledMillis() {
        return this.chatDisabledMillis - System.currentTimeMillis();
    }
    
    public int getChatSlowedDelay() {
        return this.chatSlowedDelay;
    }
    
    public List<String> getAnnouncements() {
        return this.announcements;
    }
    
    public boolean isUseProtocolLib() {
        return this.useProtocolLib;
    }
    
    public void setUseProtocolLib(final boolean useProtocolLib) {
        this.useProtocolLib = useProtocolLib;
    }
    
    public Boolean getKitmap() {
        return this.kitmap;
    }
    
    public void setKitmap(final Boolean kitmap) {
        this.kitmap = kitmap;
    }
    
    public int getClearlagdelay() {
        return this.clearlagdelay;
    }
    
    public void setClearlagdelay(final int clearlagdelay) {
        this.clearlagdelay = clearlagdelay;
    }
    
    public int getAnnouncementDelay() {
        return this.announcementDelay;
    }
    
    public void setAnnouncementDelay(final int announcementDelay) {
        this.announcementDelay = announcementDelay;
    }
    
    public long getChatSlowedMillis() {
        return this.chatSlowedMillis;
    }
    
    public void setChatSlowedDelay(final int chatSlowedDelay) {
        this.chatSlowedDelay = chatSlowedDelay;
    }
    
    public String getBroadcastFormat() {
        return this.broadcastFormat;
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public boolean isDecreasedLagMode() {
        return this.decreasedLagMode;
    }
    
    public void setDecreasedLagMode(final boolean decreasedLagMode) {
        this.decreasedLagMode = decreasedLagMode;
    }
    
    public boolean isEnd() {
        return this.end;
    }
    
    public void setEnd(final boolean end) {
        this.end = end;
    }
    
    public Location getEndExit() {
        return this.endExit;
    }
    
    public boolean isDonorOnly() {
        return this.donorOnly;
    }
    
    public void setDonorOnly(final boolean donorOnly) {
        this.donorOnly = donorOnly;
    }
    
    public int getWorldBorder() {
        return this.worldBorder;
    }
    
    public int getNetherBorder() {
        return this.netherBorder;
    }
    
    public int getEndBorder() {
        return this.endBorder;
    }

}
