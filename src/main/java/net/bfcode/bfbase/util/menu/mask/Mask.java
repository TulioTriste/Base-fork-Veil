package net.bfcode.bfbase.util.menu.mask;

public interface Mask extends Iterable<Integer>
{
    boolean test(final int p0);
    
    boolean test(final int p0, final int p1);
    
    public interface Builder
    {
        int currentLine();
        
        int rows();
        
        int columns();
        
        Builder row(final int p0) throws IllegalStateException;
        
        Builder nextRow();
        
        Builder previousRow() throws IllegalStateException;
        
        Builder apply(final String p0);
        
        Mask build();
    }
}
