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

import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

interface PipeProcessor {
    public AbstractPipeProcessor getNext();
    public void setNext(AbstractPipeProcessor next);
    public PipeSource getSource();
    public void setSource(PipeSource source);
    public void processInputValue(int value);
    public void processInputValue(long value);
    public void processInputValue(double value);
    public boolean hasOutputValue();
    public int getIntOutputValue();
    public long getLongOutputValue();
    public double getDoubleOutputValue();
    public boolean isInactive();
}

abstract class AbstractPipeProcessor implements PipeProcessor {

    public final boolean nextOnSameSourceOnNoValue;
    public final boolean hasNextNeedsValue;

    protected AbstractPipeProcessor(boolean nextOnSameSourceOnNoValue,
                                    boolean hasNextNeedsValue) {
        this.nextOnSameSourceOnNoValue =
                nextOnSameSourceOnNoValue;
        this.hasNextNeedsValue = hasNextNeedsValue;
    }

    // ---------------------------------------------------------------------- //

    private AbstractPipeProcessor next;
    private PipeSource            source;

    @Override public final AbstractPipeProcessor getNext() {
        return next;
    }
    @Override public final void setNext(AbstractPipeProcessor next) {
        if (this.next == null) {
            this.next = next;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override public final PipeSource getSource() {
        return source;
    }
    @Override public final void setSource(PipeSource reference) {
        if (this.source == null) {
            this.source = reference;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    // ---------------------------------------------------------------------- //

    @Override public final int getIntOutputValue() {
        final int result = retrieveIntOutputValue();
        clearOutputValue();
        return result;
    }
    @Override public final long getLongOutputValue() {
        final long result = retrieveLongOutputValue();
        clearOutputValue();
        return result;
    }
    @Override public final double getDoubleOutputValue() {
        final double result = retrieveDoubleOutputValue();
        clearOutputValue();
        return result;
    }

    protected abstract int retrieveIntOutputValue();
    protected abstract long retrieveLongOutputValue();
    protected abstract double retrieveDoubleOutputValue();    
    protected abstract void clearOutputValue();
    
    // ---------------------------------------------------------------------- //
    
    public static abstract class OfInt extends AbstractPipeProcessor {
        
        protected OfInt(boolean nextOnSameSourceOnNoValue,
                        boolean hasNextNeedsValue) {
            super(nextOnSameSourceOnNoValue, hasNextNeedsValue);
        }

        // ------------------------------------------------------------------ //

        public boolean pushFrontMap(IntUnaryOperator mapper) {
            return false;
        }
        public boolean enqueueMap(IntUnaryOperator mapper) {
            return false;
        }
        public boolean pushFrontFilter(IntPredicate predicate) {
            return false;
        }
        public boolean enqueueFilter(IntPredicate predicate) {
            return false;
        }

        // ------------------------------------------------------------------ //
        
        @Override
        public final void processInputValue(long value) {
            throw new UnsupportedOperationException();
        }
        @Override
        public final void processInputValue(double value) {
            throw new UnsupportedOperationException();
        }
        @Override
        protected final long retrieveLongOutputValue() {
            throw new UnsupportedOperationException();
        }
        @Override
        protected final double retrieveDoubleOutputValue() {
            throw new UnsupportedOperationException();
        }
    }
    public static abstract class OfLong extends AbstractPipeProcessor {
        
        protected OfLong(boolean nextOnSameSourceOnNoValue,
                        boolean hasNextNeedsValue) {
            super(nextOnSameSourceOnNoValue, hasNextNeedsValue);
        }

        // ------------------------------------------------------------------ //

        public boolean pushFrontMap(LongUnaryOperator mapper) {
            return false;
        }
        public boolean enqueueMap(LongUnaryOperator mapper) {
            return false;
        }
        public boolean pushFrontFilter(LongPredicate predicate) {
            return false;
        }
        public boolean enqueueFilter(LongPredicate predicate) {
            return false;
        }

        // ------------------------------------------------------------------ //
        
        @Override
        public final void processInputValue(int value) {
            throw new UnsupportedOperationException();
        }
        @Override
        public final void processInputValue(double value) {
            throw new UnsupportedOperationException();
        }
        @Override
        protected final int retrieveIntOutputValue() {
            throw new UnsupportedOperationException();
        }
        @Override
        protected final double retrieveDoubleOutputValue() {
            throw new UnsupportedOperationException();
        }
    }
    public static abstract class OfDouble extends AbstractPipeProcessor {
        
        protected OfDouble(boolean nextOnSameSourceOnNoValue,
                           boolean hasNextNeedsValue) {
            super(nextOnSameSourceOnNoValue, hasNextNeedsValue);
        }

        // ------------------------------------------------------------------ //

        public boolean pushFrontMap(DoubleUnaryOperator mapper) {
            return false;
        }
        public boolean enqueueMap(DoubleUnaryOperator mapper) {
            return false;
        }
        public boolean pushFrontFilter(DoublePredicate predicate) {
            return false;
        }
        public boolean enqueueFilter(DoublePredicate predicate) {
            return false;
        }

        // ------------------------------------------------------------------ //
        
        @Override
        public final void processInputValue(int value) {
            throw new UnsupportedOperationException();
        }
        @Override
        public final void processInputValue(long value) {
            throw new UnsupportedOperationException();
        }
        @Override
        protected final int retrieveIntOutputValue() {
            throw new UnsupportedOperationException();
        }
        @Override
        protected final long retrieveLongOutputValue() {
            throw new UnsupportedOperationException();
        }
    }
}
