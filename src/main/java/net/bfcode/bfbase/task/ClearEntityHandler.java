package net.bfcode.bfbase.task;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Tameable;
import org.bukkit.scheduler.BukkitRunnable;

import net.bfcode.bfbase.BasePlugin;

public class ClearEntityHandler extends BukkitRunnable
{
    public void run() {
        for (final World world : Bukkit.getWorlds()) {
            int i = 0;
            for (final Chunk chunk : world.getLoadedChunks()) {
                for (final Entity entity : chunk.getEntities()) {
                    if ((entity.getType() == EntityType.DROPPED_ITEM || entity.getType() == EntityType.SKELETON || entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ARROW || entity.getType() == EntityType.SPIDER || entity.getType() == EntityType.CREEPER || entity.getType() == EntityType.ENDERMAN || entity.getType() == EntityType.SILVERFISH || entity.getType() == EntityType.MAGMA_CUBE || entity.getType() == EntityType.IRON_GOLEM || entity.getType() == EntityType.GHAST) && (!(entity instanceof Tameable) || !((Tameable)entity).isTamed())) {
                        if (entity.getType() == EntityType.DROPPED_ITEM && entity instanceof Item) {
                            final Material material;
                            if ((material = ((Item)entity).getItemStack().getType()) == Material.SAND || material == Material.TNT || material == Material.DIAMOND || material == Material.EMERALD || material == Material.DIAMOND_ORE || material == Material.GOLD_ORE || material == Material.GOLD_INGOT || material == Material.IRON_INGOT || material == Material.IRON_ORE || material == Material.MOB_SPAWNER || material == Material.HOPPER) {
                                break;
                            }
                            if (material == Material.BEACON) {
                                break;
                            }
                        }
                        entity.remove();
                        ++i;
                    }
                }
            }
            BasePlugin.getPlugin().getLogger().info("Cleared " + i + " entities in " + world.getName() + "!");
        }
    }
}
