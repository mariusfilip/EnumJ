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

import java.util.PrimitiveIterator;

final class IteratorEnumerator {    
    public static class OfInt extends AbstractIntEnumerator {
        
        private PrimitiveIterator.OfInt source;
        private int                     value;
        
        protected OfInt(PrimitiveIterator.OfInt source) {
            this.source = source;
        }
        @Override
        protected final boolean internalHasNext() {
            return source.hasNext();
        }
        @Override
        protected final void moveToNext() {
            value = source.nextInt();
        }
        @Override
        protected final int internalNextInt() {
            return value;
        }
        @Override
        protected final void cleanup() { source = null; }
    }
    public static class OfLong extends AbstractLongEnumerator {
        
        private PrimitiveIterator.OfLong source;
        private long                     value;
        
        protected OfLong(PrimitiveIterator.OfLong source) {
            this.source = source;
        }
        @Override
        protected final boolean internalHasNext() {
            return source.hasNext();
        }
        @Override
        protected final void moveToNext() {
            value = source.nextLong();
        }
        @Override
        protected final long internalNextLong() {
            return value;
        }
        @Override
        protected final void cleanup() { source = null; }
    }
    public static class OfDouble extends AbstractDoubleEnumerator {
        
        private PrimitiveIterator.OfDouble source;
        private double                     value;
        
        protected OfDouble(PrimitiveIterator.OfDouble source) {
            this.source = source;
        }
        @Override
        protected final boolean internalHasNext() {
            return source.hasNext();
        }
        @Override
        protected final void moveToNext() {
            value = source.nextDouble();
        }
        @Override
        protected final double internalNextDouble() {
            return value;
        }
        @Override
        protected final void cleanup() { source = null; }
    }
    
    public static OfInt of(PrimitiveIterator.OfInt source) {
        if (source instanceof OfInt) {
            return (OfInt)source;
        }
        return new OfInt(source);
    }
    public static OfLong of(PrimitiveIterator.OfLong source) {
        if (source instanceof OfLong) {
            return (OfLong)source;
        }
        return new OfLong(source);
    }
    public static OfDouble of(PrimitiveIterator.OfDouble source) {
        if (source instanceof OfDouble) {
            return (OfDouble)source;
        }
        return new OfDouble(source);
    }
}
