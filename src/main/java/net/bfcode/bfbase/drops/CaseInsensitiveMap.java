package net.bfcode.bfbase.drops;

import java.util.Map;
import net.minecraft.util.gnu.trove.strategy.HashingStrategy;
import net.minecraft.util.gnu.trove.map.hash.TCustomHashMap;

public class CaseInsensitiveMap<V> extends TCustomHashMap<String, V>
{
    public CaseInsensitiveMap() {
        super((HashingStrategy)CaseInsensitiveHashingStrategy.INSTANCE);
    }
    
    public CaseInsensitiveMap(final Map<? extends String, ? extends V> map) {
        super((HashingStrategy)CaseInsensitiveHashingStrategy.INSTANCE, (Map)map);
    }
}
