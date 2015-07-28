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

import java.util.function.Consumer;

/**
 * Implementation of {@link Enumerable} that caches the elements being
 * enumerated.
 * @param <E> Type of enumerated elements.
 */
public final class CachedEnumerable<E> extends AbstractEnumerable<E> {

    static  final    Consumer<?>              noAction = self -> {};
    private volatile CachedEnumerableState<E> state;

    /**
     * Constructs a {@link CachedEnumerable} instance that caches the elements
     * of the given source {@link Enumerable}.
     * <p>
     * The resulted {@link CachedEnumerable} has no size limit and it's internal
     * cache does not get replaced.
     * </p>
     * @param source {@link Enumerable} to cache.
     */
    CachedEnumerable(Enumerable<E> source) {
        this(source, Long.MAX_VALUE, (Consumer<CachedEnumerable<E>>)noAction);
    }
    /**
     * Constructs a {@link CachedEnumerable} instance that caches the elements
     * of the given source {@link Enumerable} up to the given limit.
     * @param source {@link Enumerable} to cache.
     * @param limit maximum size of the internal cache.
     * @param onLimitCallback method to call when the cache reaches the limit.
     */
    CachedEnumerable(Enumerable<E> source,
                     long limit,
                     Consumer<CachedEnumerable<E>> onLimitCallback) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Checks.ensureLessThan(0, limit, Messages.ILLEGAL_ENUMERATOR_STATE);
        Checks.ensureNotNull(onLimitCallback, Messages.NULL_ENUMERATOR_HANDLER);

        this.state = new CachedEnumerableState(source,
                                               this,
                                               limit,
                                               onLimitCallback);
    }

    /**
     * Gets a {@link CachedEnumerableState} instance representing the state
     * of the current {@link CachedEnumerable}.
     * @return {@link CachedEnumerableState} instance
     */
    public CachedEnumerableState<E> state() {
        return this.state;
    }

    /**
     * Disables caching and returns the disabled state.
     * @return {@link CachedEnumerableState} representing the disabled state.
     */
    public CachedEnumerableState<E> disable() {
        final CachedEnumerableState<E> disabled = state().disable();
        this.state = disabled;
        return disabled;
    }
    /**
     * Enables caching and returns the enabled state.
     * @return {@link CachedEnumerableState} representing the enabled state.
     */
    public CachedEnumerableState<E> enable() {
        final CachedEnumerableState<E> enabled = state().enable();
        this.state = enabled;
        return enabled;
    }

    /**
     * Resets the cache and returns the new state of the current
     * {@link CachedEnumerable}.
     * @return {@link CachedEnumerableState} representing the reset state.
     */
    public CachedEnumerableState<E> reset() {
        final CachedEnumerableState<E> res = state().reset();
        this.state = res;
        return res;
    }
    /**
     * Resizes the cache to a larger limit.
     * @param newLimit larger cache size.
     * @return {@link CachedEnumerableState} representing the resized state.
     */
    public CachedEnumerableState<E> resize(long newLimit) {
        final CachedEnumerableState<E> res = state().resize(newLimit);
        this.state = res;
        return res;
    }

    @Override
    protected boolean internalOnceOnly() {
        final CachedEnumerableState<E> state = state();
        return state.isDisabled() && state.onceOnly();
    }
    @Override
    protected Enumerator<E> internalEnumerator() {
        return state().enumerator();
    }
}
