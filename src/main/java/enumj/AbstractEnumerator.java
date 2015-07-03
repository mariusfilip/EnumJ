/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.mutable.MutableBoolean;
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

    public static <E> Enumerator<E> skip(Enumerator<E> source,
                                         long          n,
                                         boolean       reversed) {
        Utils.ensureNonEnumerating(source);
        Utils.ensureNonNegative(n, Messages.NEGATIVE_ENUMERATOR_SIZE);
        final MutableLong size = new MutableLong(0);
        return skipWhile(
                source,
                e -> {
                    if (size.longValue() >= n) {
                        return false;
                    }
                    size.add(1);
                    return true;
                },
                reversed);
    }

    public static <E> Enumerator<E> skipWhile(
            Enumerator<E>        source,
            Predicate<? super E> predicate,
            boolean              reversed) {
        Utils.ensureNonEnumerating(source);
        final MutableBoolean skip = new MutableBoolean(true);
        final Function<E,Nullable<E>> mapper = e -> {
            if (skip.isTrue()) {
                skip.setValue(predicate.test(e));
            }
            return skip.isTrue() ? Nullable.empty() : Nullable.of(e);
        };
        if (reversed) {
            final PipeEnumerator pipe = (PipeEnumerator)source;
            return pipe.reversedMap(e -> ((Nullable<E>)e).get())
                       .reversedFilter(e -> ((Nullable<E>)e).isPresent())
                       .reversedMap(mapper);
        }
        return source.map(mapper)
                     .filter(e -> e.isPresent())
                     .map(e -> e.get());
    }
}
