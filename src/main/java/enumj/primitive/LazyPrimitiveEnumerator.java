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

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Supplier;

final class LazyPrimitiveEnumerator extends AbstractEnumerator {
    private Supplier<Iterator> source;
    private Iterator iterator;
    
    private LazyPrimitiveEnumerator(Supplier<Iterator> source) {
        this.source = source;
    }
    @Override
    protected boolean internalHasNext() {
        if (iterator == null) {
            iterator = source.get();
        }
        return iterator.hasNext();
    }
    @Override
    protected void moveToNext() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected int internalNextInt() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected long internalNextLong() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected double internalNextDouble() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected void cleanup() {
        source = null;
        iterator = null;
    }
    
    public final class OfInt extends AbstractIntEnumerator {
        private PrimitiveIterator.OfInt iterator;
        private int                     value;
        private OfInt() {}
        @Override
        protected boolean internalHasNext() {
            return LazyPrimitiveEnumerator.this.internalHasNext();
        }
        @Override
        protected void moveToNext() {
            if (iterator == null) {
                iterator = (PrimitiveIterator.OfInt)
                        LazyPrimitiveEnumerator.this.iterator;
            }
            value = iterator.nextInt();
        }
        @Override
        protected int internalNextInt() { return value; }
        @Override
        protected void cleanup() {
            LazyPrimitiveEnumerator.this.cleanup();
            iterator = null;
        }
    }
    public final class OfLong extends AbstractLongEnumerator {
        private PrimitiveIterator.OfLong iterator;
        private long                     value;
        private OfLong() {}
        @Override
        protected boolean internalHasNext() {
            return LazyPrimitiveEnumerator.this.internalHasNext();
        }
        @Override
        protected void moveToNext() {
            if (iterator == null) {
                iterator = (PrimitiveIterator.OfLong)
                        LazyPrimitiveEnumerator.this.iterator;
            }
            value = iterator.nextLong();
        }
        @Override
        protected long internalNextLong() { return value; }
        @Override
        protected void cleanup() {
            LazyPrimitiveEnumerator.this.cleanup();
            iterator = null;
        }
    }
    public final class OfDouble extends AbstractDoubleEnumerator {
        private PrimitiveIterator.OfDouble iterator;
        private double                     value;
        private OfDouble() {}
        @Override
        protected boolean internalHasNext() {
            return LazyPrimitiveEnumerator.this.internalHasNext();
        }
        @Override
        protected void moveToNext() {
            if (iterator == null) {
                iterator = (PrimitiveIterator.OfDouble)
                        LazyPrimitiveEnumerator.this.iterator;
            }
            value = iterator.nextDouble();
        }
        @Override
        protected double internalNextDouble() { return value; }
        @Override
        protected void cleanup() {
            LazyPrimitiveEnumerator.this.cleanup();
            iterator = null;
        }
    }
    
    public static OfInt of(Supplier<PrimitiveIterator.OfInt> source, int what) {
        final LazyPrimitiveEnumerator e = new LazyPrimitiveEnumerator(() ->
                source.get());
        return e.new OfInt();
    }
    public static OfLong of(Supplier<PrimitiveIterator.OfLong> source,
                            long what) {
        final LazyPrimitiveEnumerator e = new LazyPrimitiveEnumerator(() ->
                source.get());
        return e.new OfLong();
    }
    public static OfDouble of(Supplier<PrimitiveIterator.OfDouble> source,
                              double what) {
        final LazyPrimitiveEnumerator e = new LazyPrimitiveEnumerator(() ->
                source.get());
        return e.new OfDouble();
    }
}
