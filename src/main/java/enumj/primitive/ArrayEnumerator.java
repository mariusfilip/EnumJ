/*
 * The MIT License
 *
 * Copyright 2016 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package enumj.primitive;

final class ArrayEnumerator extends AbstractEnumerator {

    private int size;
    private int index;
    
    private ArrayEnumerator(int size) {
        this.size = size;
        this.index = 0;
    }
    @Override
    protected final boolean internalHasNext() { return index < size; }
    @Override
    protected final void moveToNext() { ++index; }
    @Override
    protected final int internalNextInt() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected final long internalNextLong() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected final double internalNextDouble() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected final void cleanup() {
        size = index = 0;
    }
    
    public final class OfInt extends AbstractIntEnumerator {
        private int[] source;
        
        private OfInt(int[] source) {
            assert ArrayEnumerator.this.size == source.length;
            this.source = source;
        }

        @Override
        protected final boolean internalHasNext() {
            return ArrayEnumerator.this.internalHasNext();
        }        
        @Override
        protected final void moveToNext() {
            ArrayEnumerator.this.moveToNext();
        }
        @Override
        protected final int internalNextInt() {
            return source[ArrayEnumerator.this.index-1];
        }
        @Override
        protected final void cleanup() {
            ArrayEnumerator.this.cleanup();
            source = null;
        }
    }
    public final class OfLong extends AbstractLongEnumerator {
        private long[] source;
        
        private OfLong(long[] source) {
            assert ArrayEnumerator.this.size == source.length;
            this.source = source;
        }

        @Override
        protected final boolean internalHasNext() {
            return ArrayEnumerator.this.internalHasNext();
        }        
        @Override
        protected final void moveToNext() {
            ArrayEnumerator.this.moveToNext();
        }
        @Override
        protected final long internalNextLong() {
            return source[ArrayEnumerator.this.index-1];
        }
        @Override
        protected final void cleanup() {
            ArrayEnumerator.this.cleanup();
            source = null;
        }
    }
    public final class OfDouble extends AbstractDoubleEnumerator {
        private double[] source;
        
        private OfDouble(double[] source) {
            assert ArrayEnumerator.this.size == source.length;
            this.source = source;
        }

        @Override
        protected final boolean internalHasNext() {
            return ArrayEnumerator.this.internalHasNext();
        }        
        @Override
        protected final void moveToNext() {
            ArrayEnumerator.this.moveToNext();
        }
        @Override
        protected final double internalNextDouble() {
            return source[ArrayEnumerator.this.index-1];
        }
        @Override
        protected final void cleanup() {
            ArrayEnumerator.this.cleanup();
            source = null;
        }
    }
    
    public static OfInt of(int[] source) {
        final ArrayEnumerator arr = new ArrayEnumerator(source.length);
        return arr.new OfInt(source);
    }
    public static OfLong of(long[] source) {
        final ArrayEnumerator arr = new ArrayEnumerator(source.length);
        return arr.new OfLong(source);
    }
    public static OfDouble of(double[] source) {
        final ArrayEnumerator arr = new ArrayEnumerator(source.length);
        return arr.new OfDouble(source);
    }
}
