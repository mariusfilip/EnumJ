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

final class AbstractPrimitiveEnumerator {
    static abstract class OfInt
           extends AbstractEnumerator<Integer>
           implements PrimitiveEnumerator.OfInt {
        private int     value;
        private boolean boxIt = true;
        @Override protected final void internalNext(Out<Integer> value) {
            this.value = internalNextInt();
            if (boxIt) {
                value.setInt(this.value);
            } else {
                value.clear();
            }
        }
        @Override public final int nextInt() {
            boxIt = false;
            try {
                super.next();
            } finally {
                boxIt = true;
            }
            return this.value;
        }
        protected abstract int internalNextInt();
    }
    static abstract class OfLong
           extends AbstractEnumerator<Long>
           implements PrimitiveIterator.OfLong {
        private long    value;
        private boolean boxIt = true;
        @Override protected final void internalNext(Out<Long> value) {
            this.value = internalNextLong();
            if (boxIt) {
                value.setLong(this.value);
            } else {
                value.clear();
            }
        }
        @Override public final long nextLong() {
            boxIt = false;
            try {
                super.next();
            } finally {
                boxIt = true;
            }
            return this.value;
        }
        protected abstract long internalNextLong();
    }
    static abstract class OfDouble
           extends AbstractEnumerator<Double>
           implements PrimitiveIterator.OfDouble {
        private double  value;
        private boolean boxIt = true;
        @Override protected final void internalNext(Out<Double> value) {
            this.value = internalNextDouble();
            if (boxIt) {
                value.setDouble(this.value);
            } else {
                value.clear();
            }
        }
        @Override public final double nextDouble() {
            boxIt = false;
            try {
                super.next();
            } finally {
                boxIt = true;
            }
            return this.value;
        }
        protected abstract double internalNextDouble();
    }
}
