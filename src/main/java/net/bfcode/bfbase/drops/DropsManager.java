package net.bfcode.bfbase.drops;

import java.util.Collection;

public interface DropsManager
{
    Collection<Drop> getDrops();
    
    Drop getDrop(final String p0);
    
    boolean containsDrop(final Drop p0);
    
    void createDrop(final Drop p0);
    
    void removeDrop(final Drop p0);
    
    void reloadDropData();
    
    void saveDropData();
}
