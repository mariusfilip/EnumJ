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

import java.util.function.Supplier;

/**
 * {@code Enumerable} that retrieves its source of elements lazily.
 *
 * @param <E> type of enumerated elements.
 * @see Enumerable
 * @see AbstractEnumerable
 */
final class LazyEnumerable<E> extends AbstractEnumerable<E> {

    private Lazy<Enumerable<E>> enumerable;

    private LazyEnumerable(Supplier<Iterable<E>> source) {
        enumerable = new Lazy(() -> Enumerable.of(source.get()));
    }

    @Override
    protected boolean internalOnceOnly() {
        return enumerable.get().onceOnly();
    }
    @Override
    protected Enumerator<E> internalEnumerator() {
        return enumerable.get().enumerator();
    }

    /**
     * Creates a {@code LazyEnumerable} instance that uses {@code source} to
     * get its source of elements.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Supplier} instance providing the source of elements
     * lazily.
     * @return {@link LazyEnumerable} instance.
     */
    public static <E> LazyEnumerable<E> of(
            Supplier<? extends Iterable<E>> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return new LazyEnumerable(source);
    }
}
