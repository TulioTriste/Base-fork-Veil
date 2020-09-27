package net.bfcode.bfbase.util.menu;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T>
{
    private T[] array;
    private int current;
    
    public ArrayIterator(final T[] array) {
        this.array = array;
    }
    
    @Override
    public boolean hasNext() {
        return this.array.length > this.current;
    }
    
    @Override
    public T next() {
        return (T)this.array[this.current++];
    }
}
