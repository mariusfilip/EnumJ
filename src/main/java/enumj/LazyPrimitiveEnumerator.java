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

import java.util.PrimitiveIterator;
import java.util.function.Supplier;

final class LazyPrimitiveEnumerator {
    final static class OfInt extends AbstractPrimitiveEnumerator.OfInt {
        private Supplier<? extends PrimitiveIterator.OfInt> source;
        private PrimitiveIterator.OfInt                     enumerator;
        
        private OfInt(Supplier<? extends PrimitiveIterator.OfInt> source) {
            this.source = source;
        }
        @Override
        protected boolean internalHasNext() {
            if (enumerator == null) { enumerator = source.get(); }
            return enumerator.hasNext();
        }
        @Override
        protected int internalNextInt() {
            return enumerator.nextInt();
        }
        @Override
        protected void cleanup() {
            source = null;
            enumerator = null;
        }
    }
    public static AbstractPrimitiveEnumerator.OfInt of(
            Supplier<? extends PrimitiveIterator.OfInt> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return new OfInt(source);
    }
}
