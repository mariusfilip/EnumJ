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

final class ChoiceEnumerable<E> extends AbstractEnumerable<E> {

    private final IntSupplier indexSupplier;
    private final IntUnaryOperator nextIndexSupplier;
    private final Iterable<E> first;
    private final Iterable<? extends E> second;
    private final List<Iterable<E>> rest;
    private final Lazy<Boolean> onceOnly;

    ChoiceEnumerable(IntSupplier indexSupplier,
                     IntUnaryOperator nextIndexSupplier,
                     Iterable<E> first,
                     Iterable<? extends E> second,
                     List<Iterable<E>> rest) {
        Utils.ensureNotNull(indexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Utils.ensureNotNull(nextIndexSupplier,
                            Messages.NULL_ENUMERATOR_GENERATOR);
        Utils.ensureNotNull(first, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(second, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(rest, Messages.NULL_ENUMERATOR_SOURCE);
        for(Iterable<? extends E> source : rest) {
            Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
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
        return new ChoiceEnumerator(indexSupplier,
                                    nextIndexSupplier,
                                    first.iterator(),
                                    second.iterator(),
                                    Enumerator.of(rest)
                                              .map(Iterable::iterator)
                                              .toList());
    }
}
