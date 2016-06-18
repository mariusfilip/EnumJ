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

final class PrimitivePipeSource {
    final static class OfInt extends PipeSource<Integer>
                             implements PrimitiveIterator.OfInt {
        final private AbstractPrimitiveEnumerator.OfInt intSource;
        protected OfInt(PrimitiveIterator.OfInt source) {
            this(PrimitiveIteratorEnumerator.of(source));
        }
        private OfInt(AbstractPrimitiveEnumerator.OfInt source) {
            super(source);
            this.intSource = source;
        }
        @Override
        protected void internalYieldNext(Out<? super Integer> value) {
            value.setInt(intSource.nextInt());
        }
        @Override
        public int nextInt() { return intSource.nextInt(); }
    }
    public static PrimitivePipeSource.OfInt of(
            PrimitiveIterator.OfInt source) {
        return new OfInt(source);
    }
}
