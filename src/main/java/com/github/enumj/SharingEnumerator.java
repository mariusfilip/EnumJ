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

/**
 * {@code Enumerator} sharing the elements of a common
 * {@code ShareableEnumerator}.
 * <p>
 * Sharing enumerators get spawned by {@link ShareableEnumerator} instances with
 * the purpose of sharing independently an enumerable sequence without
 * triggering the original enumeration multiple times.
 * <p>
 * A {@code ShareableEnumerator} enters its <em>shared mode</em> once it starts
 * spawning sharing enumerators. When spawned {@link SharingEnumerator}
 * instances start enumerating, the common {@code ShareableEnumerator} enters
 * its <em>shared enumerating mode</em> and further sharing is not possible.
 * <p>
 * {@code SharingEnumerator} instances can enumerate the shared elements
 * independently because the {@code ShareableEnumerator} behind them buffers
 * the elements being shared. Allowing too much divergence between the sharing
 * enumerators may lead to buffer overflows.
 * </p>
 *
 * @param <E> type of shared elements
 * @see Enumerator
 * @see AbstractEnumerator
 * @see ShareableEnumerator
 */
final class SharingEnumerator<E> extends AbstractEnumerator<E> {

    private ShareableEnumerator<E> sharedSource;
    private Enumerator<E>          cachedSource;
    private Nullable<E>            value;

    /**
     * Creates a {@code SharingEnumerator} instance that shares the elements
     * of {@code sharedSource} via {@code cachedSource}.
     *
     * @param sharedSource owner {@link ShareableEnumerator} instance that
     * needs to be notified that the shared enumeration has commenced.
     * @param cachedSource {@link Enumerator} instance resulted from calling
     * {@link CachedEnumerable#enumerator()}.
     */
    public SharingEnumerator(ShareableEnumerator<E> sharedSource,
                             Enumerator<E>          cachedSource) {
        Checks.ensureNotNull(sharedSource, Messages.NULL_ENUMERATOR_SOURCE);
        Checks.ensureNotNull(cachedSource, Messages.NULL_ENUMERATOR_SOURCE);
        this.sharedSource = sharedSource;
        this.cachedSource = cachedSource;
        this.value = cachedSource.hasNext()
                     ? Nullable.of(cachedSource.next())
                     : Nullable.empty();
    }

    @Override
    protected boolean internalHasNext() {
        sharedSource.startSharedEnumeration();
        return value.isPresent();
    }
    @Override
    protected E internalNext() {
        final E result = value.get();
        if (cachedSource.hasNext()) {
            value.set(cachedSource.next());
        } else {
            value.clear();
        }
        return result;
    }
    @Override
    protected void cleanup() {
        sharedSource = null;
        cachedSource = null;
        value.clear();
        value = null;
    }
}
