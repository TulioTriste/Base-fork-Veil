package net.bfcode.bfbase.drops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import net.bfcode.bfbase.util.Config;
import net.bfcode.bfbase.util.GenericUtils;

public class FlatFileDropsManager implements DropsManager
{
    private final JavaPlugin plugin;
    private Map<String, Drop> dropNameMap;
    private Config config;
    private List<Drop> drop;
    
    public FlatFileDropsManager(final JavaPlugin plugin) {
        this.drop = new ArrayList<Drop>();
        this.plugin = plugin;
        this.reloadDropData();
    }
    
    @Override
    public Collection<Drop> getDrops() {
        return this.dropNameMap.values();
    }
    
    @Override
    public Drop getDrop(final String uuid) {
        return this.dropNameMap.get(uuid);
    }
    
    @Override
    public boolean containsDrop(final Drop drop) {
        return this.drop.contains(drop);
    }
    
    @Override
    public void createDrop(final Drop drop) {
        if (this.drop.add(drop)) {
            this.dropNameMap.put(drop.getName(), drop);
        }
    }
    
    @Override
    public void removeDrop(final Drop drop) {
        if (this.drop.remove(drop)) {
            this.dropNameMap.remove(drop.getName());
        }
    }
    
    @Override
    public void reloadDropData() {
        this.config = new Config(this.plugin, "drops");
        final Object object = this.config.get("drop");
        if (object instanceof List) {
            this.drop = GenericUtils.createList(object, Drop.class);
            this.dropNameMap = new CaseInsensitiveMap<Drop>();
            for (final Drop drop : this.drop) {
                this.dropNameMap.put(drop.getName(), drop);
            }
        }
    }
    
    @Override
    public void saveDropData() {
        this.config.set("drop", (Object)this.drop);
        this.config.save();
    }
}
