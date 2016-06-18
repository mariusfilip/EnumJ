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
package enumj;

import java.util.Iterator;
import java.util.PrimitiveIterator;

final class PrimitiveIteratorEnumerator {
    static class OfInt
           extends AbstractPrimitiveEnumerator.OfInt {
        private PrimitiveIterator.OfInt source;
        protected OfInt(PrimitiveIterator.OfInt source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            if (source instanceof Enumerator) {
                Checks.ensureNonEnumerating((Enumerator)source);
            }
            this.source = source;
        }

        @Override
        protected final boolean internalHasNext() { return source.hasNext(); }
        @Override
        protected final int     internalNextInt() { return source.nextInt(); }
        @Override
        protected final void    cleanup() { source = null; }
    }
    static final class OfGenericInt
           extends AbstractPrimitiveEnumerator.OfInt {
        private Iterator<Integer> source;
        private OfGenericInt(Iterator<Integer> source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            if (source instanceof Enumerator) {
                Checks.ensureNonEnumerating((Enumerator)source);
            }
            this.source = source;
        }

        @Override
        protected boolean internalHasNext() { return source.hasNext(); }
        @Override
        protected int     internalNextInt() { return source.next(); }
        @Override
        protected void    cleanup() { source = null; }
    }
    static class OfLong
           extends AbstractPrimitiveEnumerator.OfLong {
        private PrimitiveIterator.OfLong source;
        protected OfLong(PrimitiveIterator.OfLong source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            if (source instanceof Enumerator) {
                Checks.ensureNonEnumerating((Enumerator)source);
            }
            this.source = source;
        }

        @Override
        protected boolean internalHasNext() {
            return source.hasNext();
        }
        @Override
        protected long internalNextLong() {
            return source.nextLong();
        }
        @Override
        protected void cleanup() {
            source = null;
        }
    }
    static class OfDouble
           extends AbstractPrimitiveEnumerator.OfDouble {
        private PrimitiveIterator.OfDouble source;
        protected OfDouble(PrimitiveIterator.OfDouble source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            if (source instanceof Enumerator) {
                Checks.ensureNonEnumerating((Enumerator)source);
            }
            this.source = source;
        }

        @Override
        protected boolean internalHasNext() {
            return source.hasNext();
        }
        @Override
        protected double internalNextDouble() {
            return source.nextDouble();
        }
        @Override
        protected void cleanup() {
            source = null;
        }
    }

    static AbstractPrimitiveEnumerator.OfInt
           ofGenericInt(Iterator<Integer> source) {
        final AbstractPrimitiveEnumerator.OfInt result =
                (source != null
                 && source instanceof AbstractPrimitiveEnumerator.OfInt)
                ? (AbstractPrimitiveEnumerator.OfInt)source
            : new OfGenericInt(source);
        if (result == source) {
            Checks.ensureNonEnumerating(result);
        }
        return result;
    }

    static AbstractPrimitiveEnumerator.OfInt
           of(PrimitiveIterator.OfInt source) {
        final AbstractPrimitiveEnumerator.OfInt result =
                (source != null
                 && source instanceof AbstractPrimitiveEnumerator.OfInt)
                ? (AbstractPrimitiveEnumerator.OfInt)source
            : new OfInt(source);
        if (result == source) {
            Checks.ensureNonEnumerating(result);
        }
        return result;
    }
    static AbstractPrimitiveEnumerator.OfLong
           of(PrimitiveIterator.OfLong source) {
        final AbstractPrimitiveEnumerator.OfLong result =
                (source != null
                 && source instanceof AbstractPrimitiveEnumerator.OfLong)
                ? (AbstractPrimitiveEnumerator.OfLong)source
            : new OfLong(source);
        if (result == source) {
            Checks.ensureNonEnumerating(result);
        }
        return result;
    }
    static AbstractPrimitiveEnumerator.OfDouble
           of(PrimitiveIterator.OfDouble source) {
        final AbstractPrimitiveEnumerator.OfDouble result =
                (source != null
                 && source instanceof AbstractPrimitiveEnumerator.OfDouble)
                ? (AbstractPrimitiveEnumerator.OfDouble)source
            : new OfDouble(source);
        if (result == source) {
            Checks.ensureNonEnumerating(result);
        }
        return result;
    }
}
