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
import java.util.function.Consumer;

/**
 *
 */
public final class CachedEnumerable<E> extends AbstractEnumerable<E> {

    private final Enumerable<E> source;
    private final LazySupplier<Enumerator<E>> enumerator;
    private final LazySupplier<Optional<CachedElementWrapper<E>>> cache;

    public CachedEnumerable(Enumerable<? extends E> source, long limit) {
        this(source, limit, null);
    }

    public CachedEnumerable(Enumerable<? extends E> source,
                            long limit,
                            Consumer<Enumerable<E>> onReset) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNonNegative(limit, Messages.NEGATIVE_ENUMERATOR_SIZE);

        this.source = (Enumerable<E>)source;
        this.enumerator = new LazySupplier(() -> this.source.enumerator());
        this.cache = new LazySupplier(() ->
        {
            Enumerator<E> en = this.enumerator.get();
            if (!en.hasNext()) {
                return Optional.empty();
            }
            E e = en.next();
            return Optional.of(
                    new CachedElementWrapper(
                            e,
                            () -> en.hasNext()
                                    ? Nullable.of(en.next())
                                    : Nullable.empty()));
        });
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        return new CacheEnumerator(cache.get());
    }
}
