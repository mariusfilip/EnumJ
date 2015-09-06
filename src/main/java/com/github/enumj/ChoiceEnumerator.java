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
package com.github.enumj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

/**
 * {@code Enumerator} implementation that enumerates by choosing among the
 * elements of given source {@link Iterator} instances.
 *
 * @param <E> Type of enumerated elements.
 */
final class ChoiceEnumerator<E> extends AbstractEnumerator<E> {

    private IntSupplier             indexSupplier;
    private IntUnaryOperator        nextIndexSupplier;
    private final List<Iterator<E>> sources;
    private final Nullable<E>       value;

    /**
     * Constructs a {@link ChoiceEnumerator} instance.
     *
     * @param indexSupplier {@link IntSupplier} instance that yields the index
     * of the source {@link Iterator} instance to get the next element from.
     * @param nextIndexSupplier {@link IntSupplier} instance that yields the
     * subsequent indexes of the source {@link Iterator} instance to get the
     * next element from, if the source indicated by {@code indexSupplier} runs
     * out of elements.
     * @param first first {@link Iterator} source to choose elements from.
     * @param second second {@link Iterator} source to choose elements from.
     * @param rest rest of {@link Iterator} sources to choose elements from.
     */
    public ChoiceEnumerator(IntSupplier       indexSupplier,
                            IntUnaryOperator  nextIndexSupplier,
                            Iterator<E>       first,
                            Iterator<E>       second,
                            List<Iterator<E>> rest) {
        Checks.ensureNotNull(indexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(nextIndexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(first, Messages.NULL_ENUMERATOR_SOURCE);
        Checks.ensureNotNull(second, Messages.NULL_ENUMERATOR_SOURCE);
        Checks.ensureNotNull(rest, Messages.NULL_ENUMERATOR_SOURCE);

        for(Iterator<E> source : rest) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        }
        this.indexSupplier = indexSupplier;
        this.nextIndexSupplier = nextIndexSupplier;

        this.sources = new ArrayList<>();
        this.sources.add(first);
        this.sources.add(second);
        this.sources.addAll(rest);

        this.value = Nullable.empty();
    }

    @Override
    protected boolean internalHasNext() {
        if (value.isPresent()) {
            return true;
        }
        if (sources.size() > 0) {
            int index = indexSupplier.getAsInt();
            int count = sources.size()-1;
            while (count >= 0) {
                if (sources.get(index) != null) {
                    if (sources.get(index).hasNext()) {
                        value.set(sources.get(index).next());
                        return true;
                    }
                    sources.set(index, null);
                }
                index = nextIndexSupplier.applyAsInt(index);
                --count;
            }
        }
        return false;
    }
    @Override
    protected E internalNext() {
        final E result = value.get();
        value.clear();
        return result;
    }
    @Override
    protected void cleanup() {
        indexSupplier = null;
        nextIndexSupplier = null;
        sources.clear();
        value.clear();
    }
}
