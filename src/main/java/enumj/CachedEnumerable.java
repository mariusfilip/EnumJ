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

public final class CachedEnumerable<E> extends AbstractEnumerable<E> {

    private final Enumerable<E> source;
    private final LazySupplier<Enumerator<E>> enumerator;
    private final LazySupplier<Optional<CachedElementWrapper<E>>> cache;
    private final Runnable callback;

    private volatile long limit;
    private volatile AtomicBoolean disabled;

    CachedEnumerable(Enumerable<E> source) {
        this(source, Long.MAX_VALUE, () -> {});
    }
    CachedEnumerable(Enumerable<E> source,
                     long limit,
                     Runnable onLimitCallback) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureLessThan(0, limit, Messages.ILLEGAL_ENUMERATOR_STATE);
        Utils.ensureNotNull(onLimitCallback, Messages.NULL_ENUMERATOR_HANDLER);

        this.source = source;
        this.enumerator = new LazySupplier(() -> this.source.enumerator());
        this.callback = onLimitCallback;
        this.limit = limit;
        this.disabled = new AtomicBoolean(false);
        this.cache = new LazySupplier(getCacheSupplier());
    }

    public void disable() {
        resize(0, true, true);
    }
    public void enable() {
        reset();
    }

    public long reset() {
        return resize(0, true, false);
    }
    public long resize(long newLimit) {
        return resize(newLimit, false, false);
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        final AtomicBoolean dis = disabled;
        if (dis.get()) {
            return source.enumerator();
        }
        synchronized(cache) {
            final AtomicBoolean dis1 = disabled;
            return dis1.get()
                    ? source.enumerator()
                    : new CacheEnumerator(cache.get());
        }
    }

    private Supplier<Optional<CachedElementWrapper<E>>> getCacheSupplier() {
        return () -> {
            final Enumerator<E> en = this.enumerator.get();
            final long lim = this.limit;
            final AtomicBoolean dis = this.disabled;
            final Runnable clbk = this.callback;

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
                                    clbk.run();
                                } catch (Exception ex) {
                                    // do nothing
                                };
                            }));
        };
    }

    private long resize(long newLimit, boolean resetting, boolean disable) {
        synchronized(cache) {
            final long result = limit;
            if (resetting) {
                newLimit = result;
            } else {
                Utils.ensureLessThan(result,
                                     newLimit,
                                     Messages.ILLEGAL_ENUMERATOR_STATE);
            }

            disabled = new AtomicBoolean(true);
            try {
                limit = newLimit;

                enumerator.refresh(() -> source.enumerator());
                cache.refresh(getCacheSupplier());

                return result;
            } finally {
                disabled.set(disable);
            }
        }
    }
}
