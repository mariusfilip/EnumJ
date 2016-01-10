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
import java.util.function.Consumer;

/**
 * Atomic composite state of {@code CachedEnumerable}.
 *
 * @param <E> Type of enumerated elements.
 * @see CachedEnumerable
 */
public final class CachedEnumerableState<E> {

    /**
     * {@code Enumerable} instance providing the elements to cache.
     */
    public final Enumerable<E> source;
    /**
     * {@code CachedEnumerable} caching the instances of {@code source}.
     * 
     * @see #source
     */
    public final CachedEnumerable<E> cachedSource;
    /**
     * Maximum size of the cache.
     *
     * @see #callback
     */
    public final long limit;
    /**
     * Method to call when the cache limit is reached.
     * <p>
     * This method gets {@link #cachedSource} as a parameter.
     * </p>
     * 
     * @see #limit
     */
    public final Consumer<CachedEnumerable<E>> callback;

    private final AtomicBoolean disabled;
    private final Lazy<Enumerator<E>> enumerator;
    private final Lazy<Optional<CachedElementWrapper<E>>> cache;

    /**
     * Constructs a {@code CachedEnumerableState} containing the given
     * source, cache size and callback.
     * <p>
     * This constructor builds a {@link CachedEnumerableState} that is
     * enabled by default.
     * </p>
     *
     * @param source {@link Enumerable} instance providing the elements to
     * cache.
     * @param cachedSource {@link CachedEnumerable} caching the elements of
     * {@code source}.
     * @param limit maximum cache size.
     * @param callback method to call when the cache limit is reached.
     */
    public CachedEnumerableState(Enumerable<E>                 source,
                                 CachedEnumerable<E>           cachedSource,
                                 long                          limit,
                                 Consumer<CachedEnumerable<E>> callback) {
        this(source, cachedSource, limit, callback, false);
    }
    private CachedEnumerableState(Enumerable<E>                 source,
                                  CachedEnumerable<E>           cachedSource,
                                  long                          limit,
                                  Consumer<CachedEnumerable<E>> callback,
                                  boolean                       disabled) {
        this.source = source;
        this.cachedSource = (limit < Long.MAX_VALUE
                             && callback != CachedEnumerable.noAction)
                            ? cachedSource
                            : null;
        this.limit = limit;
        this.callback = callback;

        this.disabled = new AtomicBoolean(disabled);
        this.enumerator = new Lazy(() -> this.source.enumerator());
        this.cache = new Lazy(() -> {
            final Enumerator<E> en = this.enumerator.get();
            final long lim = this.limit;
            final AtomicBoolean dis = this.disabled;
            final Consumer<CachedEnumerable<E>> clbk = this.callback;

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
                            clbk.accept(cachedSource);
                        } catch(Throwable ex) {
                            // do nothing
                        }
                    }));
        });
    }

    /**
     * Gets whether the caching is disabled.
     *
     * @return true if caching is disabled, false otherwise.
     * @see #disable()
     * @see #enable()
     */
    public boolean isDisabled() {
        return disabled.get();
    }
    /**
     * Gets a new {@code CachedEnumerableState} instance identical to the
     * current one with the exception of having caching <em>enabled</em>.
     *
     * @return {@link CachedEnumerableState} with caching enabled.
     * @see #isDisabled()
     * @see #disable()
     */
    public CachedEnumerableState<E> enable() {
        return new CachedEnumerableState(
                        source,
                        cachedSource,
                        limit,
                        callback,
                        false);
    }
    /**
     * Gets a new {@code CachedEnumerableState} instance identical to the
     * current one with the exception of having caching <em>disabled</em>.
     * 
     * @return {@link CachedEnumerableState} with caching disabled.
     * @see #isDisabled()
     * @see #enable()
     */
    public CachedEnumerableState<E> disable() {
        return new CachedEnumerableState(
                        source,
                        cachedSource,
                        limit,
                        callback,
                        true);
    }

    /**
     * Gets a new {@code CachedEnumerableState} instance identical to the
     * current one with the exception that it is <em>reset</em>.
     * 
     * @return reset {@link CachedEnumerableState} limit.
     * @see #resize(long)
     */
    public CachedEnumerableState<E> reset() {
        return new CachedEnumerableState(source, cachedSource, limit, callback);
    }
    /**
     * Gets a new {@code CachedEnumerableState} instance identical to the
     * current one with the exception of a greater {@code limit}.
     *
     * @param newLimit value for the cache limit, larger than the current one.
     * @return resized {@link CachedEnumerableState} limit.
     * @throws IllegalArgumentException {@code newLimit} is not larger than
     * the current limit.
     * @see #reset()
     */
    public CachedEnumerableState<E> resize(long newLimit) {
        Checks.ensureLessThan(limit,
                             newLimit,
                             Messages.ILLEGAL_ENUMERATOR_STATE);
        return new CachedEnumerableState(source,
                                         cachedSource,
                                         newLimit,
                                         callback);
    }

    /**
     * Gets whether the source {@code Enumerable} is a <em>once only</em>
     * enumerator.
     *
     * @return value of {@code source.onceOnly()}.
     * @see Enumerable#onceOnly()
     */
    public boolean onceOnly() {
        return source.onceOnly();
    }

    /**
     * Gets an {@code Enumerator} instance that enumerates over the cached
     * elements if caching is enabled or over the source {@code Enumerable}
     * if caching is disabled.
     *
     * @return potentially caching {@link Enumerator} instance.
     * @see CacheEnumerator
     * @see Enumerable
     * @see CachedEnumerable
     */
    public Enumerator<E> enumerator() {
        return disabled.get()
                ? source.enumerator()
                : new CacheEnumerator(cache.get());
    }
}
