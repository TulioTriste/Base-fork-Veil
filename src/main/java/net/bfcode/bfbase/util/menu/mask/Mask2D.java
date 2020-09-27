package net.bfcode.bfbase.util.menu.mask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bfcode.bfbase.util.menu.Menu;

public class Mask2D implements Mask
{
    private List<Integer> mask;
    
    Mask2D(final List<Integer> mask) {
        this.mask = mask;
    }
    
    public static Builder builder(final Menu menu) {
        final Menu.Dimension dimension = menu.getDimensions();
        return new Builder(dimension.getRows(), dimension.getColumns());
    }
    
    public static Builder builder(final int rows, final int cols) {
        return new Builder(rows, cols);
    }
    
    @Override
    public boolean test(final int index) {
        return this.mask.contains(index);
    }
    
    @Override
    public boolean test(final int row, final int col) {
        return this.test(row * 9 + col);
    }
    
    public List<Integer> getMask() {
        return this.mask;
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return this.mask.iterator();
    }
    
    public static class Builder implements Mask.Builder
    {
        private int currentLine;
        private int rows;
        private int cols;
        private int[][] mask;
        
        public Builder(final int rows, final int cols) {
            this.rows = rows;
            this.cols = cols;
            this.mask = new int[rows][cols];
        }
        
        @Override
        public int currentLine() {
            return this.currentLine;
        }
        
        @Override
        public int rows() {
            return this.rows;
        }
        
        @Override
        public int columns() {
            return this.cols;
        }
        
        @Override
        public Builder row(final int row) throws IllegalStateException {
            if (row < 0 || row >= this.rows) {
                throw new IllegalStateException("row not between 0 and " + this.rows());
            }
            this.currentLine = row;
            return this;
        }
        
        @Override
        public Builder nextRow() throws IllegalStateException {
            if (this.currentLine == this.mask.length) {
                throw new IllegalStateException("already at end");
            }
            ++this.currentLine;
            return this;
        }
        
        @Override
        public Builder previousRow() throws IllegalStateException {
            if (this.currentLine == 0) {
                throw new IllegalStateException("already at start");
            }
            --this.currentLine;
            return this;
        }
        
        @Override
        public Builder apply(final String pattern) {
            final char[] chars = pattern.toCharArray();
            for (int i = 0; i < 9 && i < chars.length; ++i) {
                final String ch = String.valueOf(chars[i]);
                final int c = Integer.parseInt(ch);
                this.mask[this.currentLine][i] = Math.min(c, 1);
            }
            return this;
        }
        
        @Override
        public Mask2D build() {
            final List<Integer> slots = new ArrayList<Integer>();
            for (int r = 0; r < this.mask.length; ++r) {
                final int[] col = this.mask[r];
                for (int c = 0; c < col.length; ++c) {
                    final int state = col[c];
                    if (state == 1) {
                        slots.add(r * this.columns() + c);
                    }
                }
            }
            return new Mask2D(slots);
        }
    }
}
