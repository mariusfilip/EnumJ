/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
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

import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Pipe processor that skips over enumerated elements
 * as long as a condition holds true.
 *
 * @param <E> type of enumerated elements.
 * @see SkipPipeProcessor
 * @see WhilePipeProcessor
 */
class SkipWhilePipeProcessor<E> extends AbstractPipeProcessor<E,E> {

    private final InOut<E>          value;
    private final ValuePredicate<E> filter;
    
    public SkipWhilePipeProcessor(ValuePredicate<E> filter) {
        super(true, true);
        Checks.ensureNotNull(filter, Messages.NULL_ENUMERATOR_PREDICATE);
        this.value = new InOut<>();
        this.filter = filter;
    }

    @Override
    public void processInputValue(In<E> value) {
        if (this.filter.cleared()) {
            this.value.setValue(value);
            return;
        }
        if (!this.filter.test(value)) {
            this.value.setValue(value);
            this.filter.clear();
            return;
        }
    }
    @Override
    public boolean hasOutputValue() {
        return this.value.isPresent();
    }
    @Override
    public void getOutputValue(Out<E> value) {
        value.setValue(this.value);
        this.value.clear();
    }
    @Override
    public boolean isInactive() {
        return false;
    }
}
