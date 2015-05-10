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
import java.util.function.Supplier;

/**
 *
 */
public final class CachedEnumerable<E> extends AbstractEnumerable<E> {

    private final Enumerable<E> source;
    private final LazySupplier<Enumerator<E>> enumerator;
    private final LazySupplier<Optional<CachedElementWrapper<E>>> cache;
    private final Supplier<Optional<CachedElementWrapper<E>>> cacheSupplier;

    private volatile long limit;
    private volatile AtomicBoolean disabled;

    CachedEnumerable(Enumerable<E> source) {
        this(source, Long.MAX_VALUE, () -> {});
    }
    CachedEnumerable(Enumerable<E> source,
                     long limit,
                     Runnable onLimit) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureLessThan(0, limit, Messages.ILLEGAL_ENUMERATOR_STATE);

        this.source = source;
        this.enumerator = new LazySupplier(() -> this.source.enumerator());
        this.limit = limit;
        this.disabled = new AtomicBoolean(false);
        this.cacheSupplier = () -> {
            final Enumerator<E> en = this.enumerator.get();
            final long lim = this.limit;
            final AtomicBoolean dis = this.disabled;

            if (!en.hasNext()) {
                return Optional.empty();
            }
            final E e = en.next();
            return Optional.of(
                    new CachedElementWrapper(
                            e,
                            () -> en.hasNext()
                                    ? Nullable.of(en.next())
                                    : Nullable.empty(),
                            lim,
                            1,
                            () -> {
                                dis.set(true);
                                try {
                                    onLimit.run();
                                } catch (Exception ex) {
                                    // do nothing
                                };
                            }));
        };
        this.cache = new LazySupplier(this.cacheSupplier);
    }

    public long reset() {
        return resize(this.limit, true);
    }
    public long resize(long newLimit) {
        return resize(newLimit, false);
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        synchronized(cache) {
            AtomicBoolean dis = disabled;
            return dis.get()
                    ? source.enumerator()
                    : new CacheEnumerator(cache.get());
        }
    }

    private long resize(long newLimit, boolean resetting) {
        final long result = limit;
        if (resetting) {
            Utils.ensureLessThan(0,
                                 newLimit,
                                 Messages.ILLEGAL_ENUMERATOR_STATE);
        } else {
            Utils.ensureLessThan(result,
                                 newLimit,
                                 Messages.ILLEGAL_ENUMERATOR_STATE);
        }

        synchronized(cache) {
            disabled = new AtomicBoolean(true);
            try {
                limit = resetting ? result : newLimit;

                enumerator.refresh(() -> source.enumerator());
                cache.refresh(cacheSupplier);

                return result;
            } finally {
                disabled.set(false);
            }
        }
    }    
}
