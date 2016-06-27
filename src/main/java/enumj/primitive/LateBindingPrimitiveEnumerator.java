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

import java.util.Optional;
import java.util.PrimitiveIterator;

public final class LateBindingPrimitiveEnumerator {
    public static final class OfInt extends AbstractIntEnumerator {
        private PrimitiveIterator.OfInt source;
        private int                     value;
        private OfInt(PrimitiveIterator.OfInt source) {
            this.source = source;
        }
        @Override
        protected final boolean internalHasNext() { return source.hasNext(); }
        @Override
        protected final void moveToNext() { value = source.nextInt(); }
        @Override
        protected final int internalNextInt() { return value; }
        @Override
        protected final void cleanup() { source = null; }
        
        public void bind(PrimitiveIterator.OfInt source) {
            this.source = source;
        }
        public Optional<PrimitiveIterator.OfInt> binding() {
            return Optional.ofNullable(source);
        }
    }
    public static final class OfLong extends AbstractLongEnumerator {
        private PrimitiveIterator.OfLong source;
        private long                     value;
        private OfLong(PrimitiveIterator.OfLong source) {
            this.source = source;
        }
        @Override
        protected final boolean internalHasNext() { return source.hasNext(); }
        @Override
        protected final void moveToNext() { value = source.nextLong(); }
        @Override
        protected final long internalNextLong() { return value; }
        @Override
        protected final void cleanup() { source = null; }
        
        public void bind(PrimitiveIterator.OfLong source) {
            this.source = source;
        }
        public Optional<PrimitiveIterator.OfLong> binding() {
            return Optional.ofNullable(source);
        }
    }
    public static final class OfDouble extends AbstractDoubleEnumerator {
        private PrimitiveIterator.OfDouble source;
        private double                     value;
        private OfDouble(PrimitiveIterator.OfDouble source) {
            this.source = source;
        }
        @Override
        protected final boolean internalHasNext() { return source.hasNext(); }
        @Override
        protected final void moveToNext() { value = source.nextDouble(); }
        @Override
        protected final double internalNextDouble() { return value; }
        @Override
        protected final void cleanup() { source = null; }
        
        public void bind(PrimitiveIterator.OfDouble source) {
            this.source = source;
        }
        public Optional<PrimitiveIterator.OfDouble> binding() {
            return Optional.ofNullable(source);
        }
    }
}
