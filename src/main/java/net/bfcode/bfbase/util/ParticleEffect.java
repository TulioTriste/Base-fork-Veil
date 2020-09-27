package net.bfcode.bfbase.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

public enum ParticleEffect
{
    EXPLOSION_NORMAL("EXPLOSION_NORMAL", 0, "explode", 0, -1), 
    EXPLOSION_LARGE("EXPLOSION_LARGE", 1, "largeexplode", 1, -1), 
    EXPLOSION_HUGE("EXPLOSION_HUGE", 2, "hugeexplosion", 2, -1), 
    FIREWORKS_SPARK("FIREWORKS_SPARK", 3, "fireworksSpark", 3, -1), 
    WATER_BUBBLE("WATER_BUBBLE", 4, "bubble", 4, -1, false, true), 
    WATER_SPLASH("WATER_SPLASH", 5, "splash", 5, -1), 
    WATER_WAKE("WATER_WAKE", 6, "wake", 6, 7), 
    SUSPENDED("SUSPENDED", 7, "suspended", 7, -1, false, true), 
    SUSPENDED_DEPTH("SUSPENDED_DEPTH", 8, "depthSuspend", 8, -1), 
    CRIT("CRIT", 9, "crit", 9, -1), 
    CRIT_MAGIC("CRIT_MAGIC", 10, "magicCrit", 10, -1), 
    SMOKE_NORMAL("SMOKE_NORMAL", 11, "smoke", 11, -1), 
    SMOKE_LARGE("SMOKE_LARGE", 12, "largesmoke", 12, -1), 
    SPELL("SPELL", 13, "spell", 13, -1), 
    SPELL_INSTANT("SPELL_INSTANT", 14, "instantSpell", 14, -1), 
    SPELL_MOB("SPELL_MOB", 15, "mobSpell", 15, -1), 
    SPELL_MOB_AMBIENT("SPELL_MOB_AMBIENT", 16, "mobSpellAmbient", 16, -1), 
    SPELL_WITCH("SPELL_WITCH", 17, "witchMagic", 17, -1), 
    DRIP_WATER("DRIP_WATER", 18, "dripWater", 18, -1), 
    DRIP_LAVA("DRIP_LAVA", 19, "dripLava", 19, -1), 
    VILLAGER_ANGRY("VILLAGER_ANGRY", 20, "angryVillager", 20, -1), 
    VILLAGER_HAPPY("VILLAGER_HAPPY", 21, "happyVillager", 21, -1), 
    TOWN_AURA("TOWN_AURA", 22, "townaura", 22, -1), 
    NOTE("NOTE", 23, "note", 23, -1), 
    PORTAL("PORTAL", 24, "portal", 24, -1), 
    ENCHANTMENT_TABLE("ENCHANTMENT_TABLE", 25, "enchantmenttable", 25, -1), 
    FLAME("FLAME", 26, "flame", 26, -1), 
    LAVA("LAVA", 27, "lava", 27, -1), 
    FOOTSTEP("FOOTSTEP", 28, "footstep", 28, -1), 
    CLOUD("CLOUD", 29, "cloud", 29, -1), 
    REDSTONE("REDSTONE", 30, "reddust", 30, -1), 
    SNOWBALL("SNOWBALL", 31, "snowballpoof", 31, -1), 
    SNOW_SHOVEL("SNOW_SHOVEL", 32, "snowshovel", 32, -1), 
    SLIME("SLIME", 33, "slime", 33, -1), 
    HEART("HEART", 34, "heart", 34, -1), 
    BARRIER("BARRIER", 35, "barrier", 35, 8), 
    ITEM_CRACK("ITEM_CRACK", 36, "iconcrack", 36, -1, true), 
    BLOCK_CRACK("BLOCK_CRACK", 37, "blockcrack", 37, -1, true), 
    BLOCK_DUST("BLOCK_DUST", 38, "blockdust", 38, 7, true), 
    WATER_DROP("WATER_DROP", 39, "droplet", 39, 8), 
    ITEM_TAKE("ITEM_TAKE", 40, "take", 40, 8), 
    MOB_APPEARANCE("MOB_APPEARANCE", 41, "mobappearance", 41, 8), 
    DRAGON_BREATH("DRAGON_BREATH", 42, "dragonbreath", 42, 9), 
    END_ROD("END_ROD", 43, "endRod", 43, 9), 
    DAMAGE_INDICATOR("DAMAGE_INDICATOR", 44, "damageIndicator", 44, 9), 
    SWEEP_ATTACK("SWEEP_ATTACK", 45, "sweepAttack", 45, 9), 
    FALLING_DUST("FALLING_DUST", 46, "fallingdust", 46, 10, true), 
    TOTEM("TOTEM", 47, "totem", 47, 11), 
    SPIT("SPIT", 48, "spit", 48, 11);
    
    private final String name;
    @Deprecated
    private final int id;
    
    private ParticleEffect(final String name, final int id) {
        this.name = name;
        this.id = id;
    }
    
    private ParticleEffect(final String s, final int n, final String name, final int id, final int requiredVersion, final boolean requiresData, final boolean requiresWater) {
        this.name = name;
        this.id = id;
    }
    
    private ParticleEffect(final String s, final int n, final String name, final int id, final int requiredVersion, final boolean requiresData) {
        this(s, n, name, id, requiredVersion, requiresData, false);
    }
    
    private ParticleEffect(final String s, final int n, final String name, final int id, final int requiredVersion) {
        this(s, n, name, id, requiredVersion, false);
    }
    
    @Deprecated
    String getName() {
        return this.name;
    }
    
    @Deprecated
    public int getId() {
        return this.id;
    }
    
    public void display(final Player player, final float x, final float y, final float z, final float speed, final int amount) {
        this.display(player, x, y, z, 0.0f, 0.0f, 0.0f, speed, amount);
    }
    
    public void display(final Player player, final float x, final float y, final float z, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount) {
        final PacketPlayOutWorldParticles packet = this.createPacket(x, y, z, offsetX, offsetY, offsetZ, speed, amount);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
    }
    
    public void display(final Player player, final Location location, final float speed, final int amount) {
        this.display(player, location, 0.0f, 0.0f, 0.0f, speed, amount);
    }
    
    public void display(final Player player, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount) {
        final PacketPlayOutWorldParticles packet = this.createPacket(location, offsetX, offsetY, offsetZ, speed, amount);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
    }
    
    public void broadcast(final float x, final float y, final float z, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount) {
        final PacketPlayOutWorldParticles packet = this.createPacket(x, y, z, offsetX, offsetY, offsetZ, speed, amount);
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }
    
    public void broadcast(final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount) {
        this.broadcast(location, offsetX, offsetY, offsetZ, speed, amount, null, null);
    }
    
    public void broadcast(final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Entity source) {
        this.broadcast(location, offsetX, offsetY, offsetZ, speed, amount, source, null);
    }
    
    public void broadcast(final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Entity source, final Predicate<Player> predicate) {
        final Packet packet = (Packet)this.createPacket(location, offsetX, offsetY, offsetZ, speed, amount);
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if ((source == null || player.canSee((Player)source)) && (predicate == null || predicate.apply(player))) {
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
    
    public void sphere(final Player player, final Location location, final float radius) {
        this.sphere(player, location, radius, 20.0f, 2);
    }
    
    public void sphere(final Player player, final Location location, final float radius, final float density, final int intensity) {
        Preconditions.checkNotNull((Object)location, (Object)"Location cannot be null");
        Preconditions.checkArgument(radius >= 0.0f, (Object)"Radius must be positive");
        Preconditions.checkArgument(density >= 0.0f, (Object)"Density must be positive");
        Preconditions.checkArgument(intensity >= 0, (Object)"Intensity must be positive");
        final float deltaPitch = 180.0f / density;
        final float deltaYaw = 360.0f / density;
        final World world = location.getWorld();
        for (int i = 0; i < density; ++i) {
            for (int j = 0; j < density; ++j) {
                final float pitch = -90.0f + j * deltaPitch;
                final float yaw = -180.0f + i * deltaYaw;
                final float x = radius * MathHelper.sin(-yaw * 0.017453292f - 3.1415927f) * -MathHelper.cos(-pitch * 0.017453292f) + (float)location.getX();
                final float y = radius * MathHelper.sin(-pitch * 0.017453292f) + (float)location.getY();
                final float z = radius * MathHelper.cos(-yaw * 0.017453292f - 3.1415927f) * -MathHelper.cos(-pitch * 0.017453292f) + (float)location.getZ();
                final Location target = new Location(world, (double)x, (double)y, (double)z);
                if (player == null) {
                    this.broadcast(target, 0.0f, 0.0f, 0.0f, 0.0f, intensity);
                }
                else {
                    this.display(player, target, 0.0f, 0.0f, 0.0f, 0.0f, intensity);
                }
            }
        }
    }
    
    private PacketPlayOutWorldParticles createPacket(final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount) {
        return this.createPacket((float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, speed, amount);
    }
    
    private PacketPlayOutWorldParticles createPacket(final float x, final float y, final float z, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount) {
        Preconditions.checkArgument(speed >= 0.0f, (Object)"Speed must be positive");
        Preconditions.checkArgument(amount > 0, (Object)"Cannot use less than one particle.");
        return new PacketPlayOutWorldParticles(this.name, x, y, z, offsetX, offsetY, offsetZ, speed, amount);
    }
}
