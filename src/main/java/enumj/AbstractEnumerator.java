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

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.lang3.mutable.MutableLong;

abstract class AbstractEnumerator<E> implements Enumerator<E> {

    private boolean started;
    private boolean hasNextHasBeenCalled;
    private boolean hasNextHasThrown;
    private boolean done;

    @Override
    public final boolean enumerating() {
        return started;
    }

    @Override
    public final boolean hasNext() {
        if (done) {
            return false;
        }

        started = true;
        done = !safeHasNext();
        if (done) {
            safeCleanup();
        }

        hasNextHasThrown = false;
        hasNextHasBeenCalled = true;
        return !done;
    }

    private boolean safeHasNext() {
        try {
            return internalHasNext();
        } catch(Throwable err) {
            hasNextHasThrown = true;
            hasNextHasBeenCalled = true;
            throw err;
        }
    }
    private void safeCleanup() {
        try {
            cleanup();
        } catch(Throwable err) {
            hasNextHasThrown = true;
            hasNextHasBeenCalled = true;
            throw err;
        }
    }

    @Override
    public final E next() {
        if (!hasNextHasBeenCalled) {
            hasNext();
        }
        if (done || hasNextHasThrown) {
            throw new NoSuchElementException();
        }
        try {
            return internalNext();
        } finally {
            hasNextHasBeenCalled = false;
        }
    }

    protected abstract boolean internalHasNext();
    protected abstract E internalNext();
    protected void cleanup() {}

    // ---------------------------------------------------------------------- //

    public static <E> Enumerator<E> distinct(Enumerator<E> source,
                                             boolean       reversed) {
        final Set<E> existing = new HashSet<E>(256);
        if (reversed) {
            final PipeEnumerator pipe = (PipeEnumerator)source;
            return pipe.reversedFilter(e -> existing.add((E)e));
        }
        return source.filter(existing::add);
    }

    public static <E,R> Enumerator<R> indexedMap(
            Enumerator<E> source,
            BiFunction<? super E, ? super Long, ? extends R> mapper,
            boolean reversed) {
        Utils.ensureNotNull(mapper, Messages.NULL_ENUMERATOR_MAPPER);
        final MutableLong index = new MutableLong(0);
        final Function<E,R> fun = e -> {
            final R result = mapper.apply(e, index.toLong());
            index.increment();
            return result;
        };
        if (reversed) {
            final PipeEnumerator pipe = (PipeEnumerator)source;
            return pipe.reversedMap(fun);
        }
        return source.map(fun);
    }

    public static <E> Enumerator<E> peek(Enumerator<E>       source,
                                         Consumer<? super E> action,
                                         boolean             reversed) {
        Utils.ensureNonEnumerating(source);
        Utils.ensureNotNull(action, Messages.NULL_ENUMERATOR_CONSUMER);
        final Function<E,E> actionMapper = e -> {
            action.accept(e);
            return e;
        };
        if (reversed) {
            final PipeEnumerator pipe = (PipeEnumerator)source;
            return pipe.reversedMap(actionMapper);
        }
        return source.map(actionMapper);
    }
}
