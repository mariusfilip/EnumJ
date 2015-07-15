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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Atomic composite state of {@link CachedEnumerable}.
 * @param <E> Type of enumerated elements.
 */
final class CachedEnumerableState<E> {

    /**
     * {@link Enumerable} instance providing the elements to cache.
     */
    public final Enumerable<E> source;
    /**
     * Maximum size of the cache.
     */
    public final long limit;
    /**
     * Method to call when the cache limit is reached.
     */
    public final Runnable callback;

    private final AtomicBoolean disabled;
    private final Lazy<Enumerator<E>> enumerator;
    private final Lazy<Optional<CachedElementWrapper<E>>> cache;

    /**
     * Constructs a {@link CachedEnumerableState} containing the given
     * source, cache size and callback.
     * <p>
     * This constructor builds a {@link CachedEnumerableState} that is
     * enabled by default.
     * </p>
     * @param source {@link Enumerable} instance providing the elements to
     * cache.
     * @param limit maximum cache size.
     * @param callback method to call when the cache limit is reached.
     */
    public CachedEnumerableState(Enumerable<E> source,
                                 long limit,
                                 Runnable callback) {
        this(source, limit, callback, false);
    }
    private CachedEnumerableState(Enumerable<E> source,
                                  long limit,
                                  Runnable callback,
                                  boolean disabled) {
        this.source = source;
        this.limit = limit;
        this.callback = callback;

        this.disabled = new AtomicBoolean(disabled);
        this.enumerator = new Lazy(() -> this.source.enumerator());
        this.cache = new Lazy(() ->
        {
            final Enumerator<E> en = this.enumerator.get();
            final long lim = this.limit;
            final AtomicBoolean dis = this.disabled;
            final Runnable clbk = this.callback;

            if (!en.hasNext()) {
                return Optional.empty();
            }
            final E e = en.next();
            return Optional.of(new CachedElementWrapper(
                    e,
                    () -> en.hasNext()
                            ? Nullable.of(en.next())
                            : Nullable.empty(),
                    lim,
                    1,
                    () -> {
                        dis.set(true);
                        try {
                            clbk.run();
                        } catch(Throwable ex) {
                            // do nothing
                        }
                    }));
        });
    }

    /**
     * Gets whether the caching is disabled.
     * @return {@code true} if caching is disabled, {@code false} otherwise.
     */
    public boolean isDisabled() {
        return disabled.get();
    }
    /**
     * Gets a new {@link CachedEnumerableState} instance identical to the
     * current one with the exception of having caching enabled.
     * @return {@link CachedEnumerableState} with caching enabled.
     */
    public CachedEnumerableState<E> enable() {
        return new CachedEnumerableState(
                        source,
                        limit,
                        callback,
                        false);
    }
    /**
     * Gets a new {@link CachedEnumerableState} instance identical to the
     * current one with the exception of having caching disabled.
     * @return {@link CachedEnumerableState} with caching disabled.
     */
    public CachedEnumerableState<E> disable() {
        return new CachedEnumerableState(
                        source,
                        limit,
                        callback,
                        true);
    }

    /**
     * Gets a new {@link CachedEnumerableState} instance identical to the
     * current one with the exception that it is enabled.
     * @return reset {@link CachedEnumerableState} limit.
     */
    public CachedEnumerableState<E> reset() {
        return new CachedEnumerableState(source, limit, callback);
    }
    /**
     * Gets a new {@link CachedEnumerableState} instance identical to the
     * current one with the exception of a greater limit.
     * @param newLimit value for the cache limit, larger than the current one.
     * @return resized {@link CachedEnumerableState} limit.
     * @throws IllegalArgumentException {@code newLimit} is not larger than
     * the current limit.
     */
    public CachedEnumerableState<E> resize(long newLimit) {
        Utils.ensureLessThan(limit,
                             newLimit,
                             Messages.ILLEGAL_ENUMERATOR_STATE);
        return new CachedEnumerableState(source, newLimit, callback);
    }

    /**
     * Gets whether the source {@link Enumerable} is a <em>once only</em>
     * enumerator.
     * @return {@code true} if {@code source.onceOnly()} returns {@code true},
     * {@code false} otherwise.
     * @see Enumerable#onceOnly()
     */
    public boolean onceOnly() {
        return source.onceOnly();
    }

    /**
     * Gets an {@link Enumerator} instance that enumerates over the cached
     * elements if caching is enabled or over the source {@link Enumerable}
     * if caching is disabled.
     * @return Potentially caching {@link Enumerator} instance.
     * @see CacheEnumerator
     */
    public Enumerator<E> enumerator() {
        return disabled.get()
                ? source.enumerator()
                : new CacheEnumerator(cache.get());
    }
}
