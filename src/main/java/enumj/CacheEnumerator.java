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

/**
 * {@code Enumerator} over cached enumerated elements.
 * <p>
 * The enumerated elements get cached by {@link CachedEnumerable} into
 * a linked list of {@link CachedElementWrapper} instances. The
 * {@link CacheEnumerator} instances enumerated over these linked lists.
 * </p>
 *
 * @param <E> Type of enumerated elements.
 */
final class CacheEnumerator<E> extends AbstractEnumerator<E> {

    private Optional<CachedElementWrapper<E>> cached;
    
    /**
     * Constructs a {@code CacheEnumerator} encapsulating an optional
     * {@code CachedElementWrapper}.
     * 
     * @param cached Optional {@link CachedElementWrapper} representing
     * the head of the list to enumerate.
     * @see CachedEnumerable
     */
    public CacheEnumerator(Optional<CachedElementWrapper<E>> cached) {
        this.cached = cached;
    }

    @Override
    protected boolean internalHasNext() {
        return cached.isPresent();
    }
    @Override
    protected void internalNext(Out<E> value) {
        value.set(cached.get().getElement());
        cached = cached.get().getNextWrapper();
    }
    @Override
    protected void cleanup() {
        cached = null;
    }
}
