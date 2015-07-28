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

import java.util.function.Predicate;

/**
 * Type of {@link AbstractPipeProcessor} that skips over enumerated elements
 * as long as a condition holds {@code true}.
 * @param <E> type of enumerated elements.
 */
class SkipWhilePipeProcessor<E> extends AbstractPipeProcessor<E,E> {

    /**
     * Enumerated value.
     */
    protected E            value;
    /**
     * {@link Predicate} that filters skipped elements.
     */
    protected Predicate<E> filter;

    /**
     * Constructs a {@link SkipWhilePipeProcessor} that skips enumerated
     * elements as long as the given {@code filter} holds {@code true}.
     * @param filter {@link Predicate} filtering skipped elements.
     */
    public SkipWhilePipeProcessor(Predicate<E> filter) {
        super(true, true);
        Checks.ensureNotNull(filter, Messages.NULL_ENUMERATOR_PREDICATE);
        this.filter = filter;
    }

    @Override
    protected void processInputValue(E value) {
        if (this.filter == null) {
            this.value = value;
        }
        else if(!this.filter.test(value)) {
            this.value = value;
            this.filter = null;
        }
    }
    @Override
    protected boolean hasOutputValue() {
        return filter == null;
    }
    @Override
    protected E retrieveOutputValue() {
        return value;
    }
    @Override
    protected void clearOutputValue() {
        value = null;
    }
    @Override
    protected boolean isInactive() {
        return false;
    }
}
