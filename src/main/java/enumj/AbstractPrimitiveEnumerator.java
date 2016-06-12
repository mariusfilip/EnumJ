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

import static enumj.AbstractNumericEnumerator.INT_TYPE;
import java.util.PrimitiveIterator;

final class AbstractPrimitiveEnumerator {
    static abstract class OfInt
           extends AbstractNumericEnumerator<Integer>
           implements PrimitiveEnumerator.OfInt {
        private int value;
        protected OfInt() {
            super(INT_TYPE);
        }
        @Override
        protected final int internalNextInt() { return value = safeNextInt(); }
        @Override
        public final int nextInt() {
            boxIt = false;
            try {
                super.next();
                return value;
            } finally {
                boxIt = true;
            }
        }
        protected abstract int safeNextInt();
    }
    static abstract class OfLong
           extends AbstractNumericEnumerator<Long>
           implements PrimitiveIterator.OfLong {
        private long value;
        protected OfLong() {
            super(LONG_TYPE);
        }
        @Override
        protected final long internalNextLong() {
            return value = safeNextLong();
        }
        @Override
        public final long nextLong() {
            boxIt = false;
            try {
                super.next();
                return value;
            } finally {
                boxIt = true;
            }
        }
        protected abstract long safeNextLong();
    }
    static abstract class OfDouble
           extends AbstractNumericEnumerator<Double>
           implements PrimitiveIterator.OfDouble {
        private double value;
        protected OfDouble() {
            super(DOUBLE_TYPE);
        }
        @Override
        protected final double internalNextDouble() {
            return value = nextDouble();
        }
        @Override
        public final double nextDouble() {
            boxIt = false;
            try {
                super.next();
                return value;
            } finally {
                boxIt = true;
            }
        }
        protected abstract double safeNextDouble();
    }
}
