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

import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;

final class SkipPipeProcessor {

    private long n;

    private SkipPipeProcessor(long n) {
        this.n = n;
    }

    private void processInputValue(Runnable setter) {
        if (this.n == 0) { setter.run(); }
    }
    private boolean hasOutputValue() {
        if (n == 0) { return true; }
        --n; return false;
    }

    // ---------------------------------------------------------------------- //

    public final class OfInt extends AbstractPipeProcessor.OfInt {

        private final MutableInt value = new MutableInt();

        private OfInt() { super(false, true); }

        @Override public final void processInputValue(int value) {
            SkipPipeProcessor.this.processInputValue(() -> {
                this.value.setValue(value);
            });
        }
        @Override public final boolean hasOutputValue() {
            return SkipPipeProcessor.this.hasOutputValue();
        }
        @Override protected final int retrieveIntOutputValue() {
            return value.intValue();
        }
        @Override protected final void clearOutputValue() {}
        @Override public final boolean isInactive() { return false; }
    }
    public final class OfLong extends AbstractPipeProcessor.OfLong {

        private final MutableLong value = new MutableLong();
                
        private OfLong() { super(false, true); }
        
        @Override public final void processInputValue(long value) {
            SkipPipeProcessor.this.processInputValue(() -> {
                this.value.setValue(value);
            });
        }
        @Override public final boolean hasOutputValue() {
            return SkipPipeProcessor.this.hasOutputValue();
        }
        @Override protected final long retrieveLongOutputValue() {
            return value.longValue();
        }
        @Override protected final void clearOutputValue() {}
        @Override public final boolean isInactive() { return false; }
    }
    public final class OfDouble extends AbstractPipeProcessor.OfDouble {

        private MutableDouble value = new MutableDouble();

        private OfDouble() { super(false, true); }

        @Override public final void processInputValue(double value) {
            SkipPipeProcessor.this.processInputValue(() -> {
                this.value.setValue(value);
            });
        }
        @Override public final boolean hasOutputValue() {
            return SkipPipeProcessor.this.hasOutputValue();
        }
        @Override protected final double retrieveDoubleOutputValue() {
            return value.doubleValue();
        }
        @Override protected final void clearOutputValue() {}
        @Override public final boolean isInactive() { return false; }
    }

    // ---------------------------------------------------------------------- //

    public static OfInt ofInt(long n) {
        return (new SkipPipeProcessor(n)).new OfInt();
    }
    public static OfLong ofLong(long n) {
        return (new SkipPipeProcessor(n)).new OfLong();
    }
    public static OfDouble ofDouble(long n) {
        return (new SkipPipeProcessor(n)).new OfDouble();
    }
}
