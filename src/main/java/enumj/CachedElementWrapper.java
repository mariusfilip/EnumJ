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

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Wrapper of cached enumerated elements.
 * <p>
 * {@link CachedEnumerable} caches the enumerated elements in linked lists
 * made of {@link CachedElementWrapper} instances and iterated upon
 * by {@link CacheEnumerator} instances.
 * </p>
 * 
 * @param <E> Type of enumerated elements.
 */
final class CachedElementWrapper<E> {

    private final E elem;
    private final Lazy<Optional<CachedElementWrapper<E>>> next;

    /**
     * Constructs a {@code CachedElementWrapper} instance that wraps the given
     * element while allowing the generation of the next element up to the
     * given limit.
     *
     * @param elem wrapped element.
     * @param nextSupplier supplier of the next element in the cached list.
     * @param limit maximum number of elements in the cached list.
     * @param ordinal position of the current element in the cached list. The
     * first element in the list has an {@code ordinal} of {@code 1}.
     * @param disableProc procedure to call when the cached list reaches limit.
     * @see CachedElementWrapper
     * @see CachedEnumerable
     * @see CacheEnumerator
     */
    public CachedElementWrapper(E elem,
                                Supplier<Nullable<E>> nextSupplier,
                                long limit,
                                long ordinal,
                                Runnable disableProc) {
        this.elem = elem;
        this.next = new Lazy(() -> {
            final long count = ordinal;
            final Nullable<E> e = nextSupplier.get();
            final Optional<CachedElementWrapper<E>> result = e.isPresent()
                    ? Optional.of(new CachedElementWrapper(e.get(),
                                                           nextSupplier,
                                                           limit,
                                                           ordinal+1,
                                                           disableProc))
                    : Optional.empty();
            if (count == limit && result.isPresent()) {
                disableProc.run();
            }
            return result;
        });
    }

    /**
     * Gets the wrapped element.
     *
     * @return wrapped element.
     */
    public E getElement() {
        return elem;
    }

    /**
     * Gets the wrapper of the next element in the cached list, if any.
     * 
     * @return {@link Optional} wrapper for the next element.
     */
    public Optional<CachedElementWrapper<E>> getNextWrapper() {
        return next.get();
    }
}
