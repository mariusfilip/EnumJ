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

import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * Pipe processor that knows how to filter elements in
 * the pipeline.
 *
 * @param <E> type of enumerated elements.
 */
final class FilterPipeProcessor<E> extends AbstractPipeProcessor<E,E> {

    private       ValuePredicate<E>             filter;
    private       LinkedList<ValuePredicate<E>> filters;
    private final InOut<E>                      value;

    /**
     * Constructs a {@code FilterPipeProcessor} instance.
     * <p>
     * The new {@link FilterPipeProcessor} stores its {@code filter} internally.
     * </p>
     * 
     * @param filter {@link Predicate} to apply on enumerated elements.
     */
    public FilterPipeProcessor(ValuePredicate<E> filter) {
        super(true, true);
        this.filter = filter;
        this.filters = null;
        this.value = new InOut<>();
    }

    // ---------------------------------------------------------------------- //

    @Override
    public boolean pushFrontFilter(ValuePredicate<? super E> predicate) {
        ensureFilters();
        this.filters.addFirst((ValuePredicate)predicate);
        return true;
    }
    @Override
    public boolean enqueueFilter(ValuePredicate<? super E> predicate) {
        ensureFilters();
        this.filters.addLast((ValuePredicate)predicate);
        return true;
    }
    private void ensureFilters() {
        if (this.filters == null) {
            this.filters = new LinkedList<>();
            this.filters.add(this.filter);
            this.filter = null;
        }
    }

    // ---------------------------------------------------------------------- //

    @Override
    public void processInputValue(In<E> value) {
        this.value.setValue(value);
    }
    @Override
    public boolean hasOutputValue() {
        if (testOutputValue()) {
            return true;
        }
        this.value.clear();
        return false;
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

    private boolean testOutputValue() {
        if (this.filter != null) {
            return this.filter.test(this.value);
        }
        for(ValuePredicate<E> pred : this.filters) {
            if (!pred.test(this.value)) {
                return false;
            }
        }
        return true;
    }
}
