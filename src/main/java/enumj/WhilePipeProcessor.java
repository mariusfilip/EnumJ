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
 * Pipe processor that lets enumeration through as long
 * as a predicate holds {@code true}.
 *
 * @param <E> type of enumerated elements.
 * @see SkipWhilePipeProcessor
 * @see SkipPipeProcessor
 * @see LimitPipeProcessor
 */
final class WhilePipeProcessor<E> extends AbstractPipeProcessor<E,E> {

    private E            value;
    private Predicate<E> filter;

    /**
     * Creates a {@code WhilePipeProcessor} that enumerates elements while
     * {@code filter} holds true.
     * <p>
     * The new {@link WhilePipeProcessor} holds its {@code filter} internally.
     * </p>
     *
     * @param filter {@link Predicate} to filter enumerated elements.
     */
    public WhilePipeProcessor(Predicate<E> filter) {
        super(false, true);
        Checks.ensureNotNull(filter, Messages.NULL_ENUMERATOR_PREDICATE);
        this.filter = filter;
    }

    @Override
    public void processInputValue(Value<E> value) {
        if (this.filter != null && this.filter.test(value.get())) {
            this.value = value.get();
        } else {
            this.filter = null;
        }
    }
    @Override
    public boolean hasOutputValue() {
        return filter != null;
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
    public boolean isInactive() {
        return filter == null;
    }
}
