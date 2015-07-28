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

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

/**
 * {@link Enumerable} implementation that enumerates by choosing among the
 * elements of given source {@link Iterable} instances.
 * @param <E> Type of enumerated elements.
 */
final class ChoiceEnumerable<E> extends AbstractEnumerable<E> {

    private final Supplier<IntSupplier> indexSupplier;
    private final Supplier<IntUnaryOperator> nextIndexSupplier;
    private final Iterable<E> first;
    private final Iterable<? extends E> second;
    private final List<Iterable<E>> rest;
    private final Lazy<Boolean> onceOnly;

    /**
     * Constructs a {@link ChoiceEnumerable} instance.
     * @param indexSupplier lazily provided {@link IntSupplier} instance that
     * yields the index of the source {@link Iterable} instance to get the next
     * element from.
     * @param nextIndexSupplier lazily provided {@link IntSupplier} instance
     * that yields the subsequent indexes of the source {@link Iterable}
     * instance to get the next element from, if the source indicated by
     * {@code indexSupplier} runs out of elements.
     * @param first first {@link Iterable} source to choose elements from.
     * @param second second {@link Iterable} source to choose elements from.
     * @param rest  rest of {@link Iterable} sources to choose elements from.
     */
    public ChoiceEnumerable(Supplier<IntSupplier> indexSupplier,
                            Supplier<IntUnaryOperator> nextIndexSupplier,
                            Iterable<E> first,
                            Iterable<? extends E> second,
                            List<Iterable<E>> rest) {
        Checks.ensureNotNull(indexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(nextIndexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(first, Messages.NULL_ENUMERATOR_SOURCE);
        Checks.ensureNotNull(second, Messages.NULL_ENUMERATOR_SOURCE);
        Checks.ensureNotNull(rest, Messages.NULL_ENUMERATOR_SOURCE);
        for(Iterable<? extends E> source : rest) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        }
        this.indexSupplier = indexSupplier;
        this.nextIndexSupplier = nextIndexSupplier;
        this.first = first;
        this.second = second;
        this.rest = rest;
        this.onceOnly = new Lazy(() ->
                Enumerable.onceOnly(this.first)
                || Enumerable.onceOnly(this.second)
                || Enumerator.of(rest)
                             .anyMatch(Enumerable::onceOnly));
    }

    @Override
    protected boolean internalOnceOnly() {
        return onceOnly.get();
    }
    @Override
    protected Enumerator<E> internalEnumerator() {
        return new ChoiceEnumerator(indexSupplier.get(),
                                    nextIndexSupplier.get(),
                                    first.iterator(),
                                    second.iterator(),
                                    Enumerator.of(rest)
                                              .map(Iterable::iterator)
                                              .toList());
    }
}
