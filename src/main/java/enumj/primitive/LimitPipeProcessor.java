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

final class LimitPipeProcessor {
    private long size;
    private LimitPipeProcessor(long maxSize) {
        this.size = maxSize;
    }
    private boolean processInputValue() { return this.size > 0; }
    private boolean hasOutputValue() { return this.size > 0; }
    private void retrieveOutputValue() { --this.size; }
    private boolean isInactive() { return this.size == 0; }

    // ---------------------------------------------------------------------- //

    public final class OfInt extends AbstractPipeProcessor.OfInt {
        private int value;
        public OfInt() { super(false, true); }
        @Override public final void processInputValue(int value) {
            if (LimitPipeProcessor.this.processInputValue()) {
                this.value = value;
            }
        }
        @Override public final boolean hasOutputValue() {
            return LimitPipeProcessor.this.hasOutputValue();
        }
        @Override protected final int retrieveIntOutputValue() {
            LimitPipeProcessor.this.retrieveOutputValue();
            return value;
        }
        @Override protected final void clearOutputValue() {}
        @Override public final boolean isInactive() {
            return LimitPipeProcessor.this.isInactive();
        }
    }
    public final class OfLong extends AbstractPipeProcessor.OfLong {
        private long value;
        public OfLong() { super(false, true); }
        @Override public final void processInputValue(long value) {
            if (LimitPipeProcessor.this.processInputValue()) {
                this.value = value;
            }
        }
        @Override public final boolean hasOutputValue() {
            return LimitPipeProcessor.this.hasOutputValue();
        }
        @Override protected final long retrieveLongOutputValue() {
            LimitPipeProcessor.this.retrieveOutputValue();
            return value;
        }
        @Override protected final void clearOutputValue() {}
        @Override public final boolean isInactive() {
            return LimitPipeProcessor.this.isInactive();
        }
    }
    public final class OfDouble extends AbstractPipeProcessor.OfDouble {
        private double value;
        public OfDouble() { super(false, true); }
        @Override public final void processInputValue(double value) {
            if (LimitPipeProcessor.this.processInputValue()) {
                this.value = value;
            }
        }
        @Override public final boolean hasOutputValue() {
            return LimitPipeProcessor.this.hasOutputValue();
        }
        @Override protected final double retrieveDoubleOutputValue() {
            LimitPipeProcessor.this.retrieveOutputValue();
            return value;
        }
        @Override protected final void clearOutputValue() {}
        @Override public final boolean isInactive() {
            return LimitPipeProcessor.this.isInactive();
        }
    }
}
