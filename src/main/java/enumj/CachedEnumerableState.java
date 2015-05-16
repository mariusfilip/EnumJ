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

public final class CachedEnumerableState<E> {

    public final Enumerable<E> source;
    public final long limit;
    public final Runnable callback;

    private final AtomicBoolean disabled;
    private final Lazy<Enumerator<E>> enumerator;
    private final Lazy<Optional<CachedElementWrapper<E>>> cache;

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
                        } catch(Exception ex) {
                            // do nothing
                        }
                    }));
        });
    }

    public boolean isDisabled() {
        return disabled.get();
    }
    public CachedEnumerableState<E> enable() {
        return new CachedEnumerableState(
                        source,
                        limit,
                        callback,
                        false);
    }
    public CachedEnumerableState<E> disable() {
        return new CachedEnumerableState(
                        source,
                        limit,
                        callback,
                        true);
    }

    public CachedEnumerableState<E> reset() {
        return new CachedEnumerableState(source, limit, callback);
    }
    public CachedEnumerableState<E> resize(long newLimit) {
        Utils.ensureLessThan(limit,
                             newLimit,
                             Messages.ILLEGAL_ENUMERATOR_STATE);
        return new CachedEnumerableState(source, newLimit, callback);
    }

    public Enumerator<E> enumerator() {
        return disabled.get()
                ? source.enumerator()
                : new CacheEnumerator(cache.get());
    }
}
