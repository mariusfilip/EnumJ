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
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.lang3.mutable.MutableLong;

/**
 * Utility class containing methods for {@code Enumerable}.
 *
 * @see Enumerable
 */
final class Reversible {

    private Reversible() {}

    /**
     * Applies a {@code Enumerator.distinct()} operation upon {@code source},
     * in reverse if necessary.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Enumerator} to apply the operation on.
     * @param reversed true if the operation is applied in reverse,
     * false otherwise.
     * @return distinct {@code Enumerator}.
     */
    public static <E> Enumerator<E> distinct(Enumerator<E> source,
                                             boolean       reversed) {
        final Set<E> existing = new HashSet<E>(256);
        if (reversed) {
            final PipeEnumerator pipe = (PipeEnumerator)source;
            return pipe.reversedFilter(e -> existing.add((E)e));
        }
        return source.filter(existing::add);
    }

    /**
     * Applies a {@code Enumerator.map(BiFunction)} operation
     * upon {@code source}, in reverse if necessary.
     *
     * @param <E> type of unmapped enumerated elements.
     * @param <R> type of mapped enumerated elements.
     * @param source {@link Enumerator} to apply the operation on.
     * @param mapper {@link BiFunction} to apply.
     * @param reversed true if the operation is applied in reverse,
     * false otherwise.
     * @return mapped {@code Enumerator}.
     */
    static <E,R> Enumerator<R> map(
            Enumerator<E> source,
            BiFunction<? super E, ? super Long, ? extends R> mapper,
            boolean reversed) {
        Checks.ensureNotNull(mapper, Messages.NULL_ENUMERATOR_MAPPER);
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

    /**
     * Applies a {@code Enumerator.peek(Consumer)} operation
     * upon {@code source}, in reverse if necessary.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Enumerator} to apply the operation on.
     * @param action {@link Consumer} to apply.
     * @param reversed true if the operation is applied in reverse,
     * false otherwise.
     * @return picked {@code Enumerator}.
     */
    public static <E> Enumerator<E> peek(Enumerator<E>       source,
                                         Consumer<? super E> action,
                                         boolean             reversed) {
        Checks.ensureNonEnumerating(source);
        Checks.ensureNotNull(action, Messages.NULL_ENUMERATOR_CONSUMER);
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
