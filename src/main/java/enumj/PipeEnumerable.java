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

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;

class PipeEnumerable<T,E> extends AbstractEnumerable<E> {

    protected Optional<Iterable<T>> source;
    protected boolean isPipeSource;
    protected Function<Enumerator<T>, Enumerator<E>> operator;

    protected PipeEnumerable(Iterable<T> source,
                             Function<Enumerator<T>, Enumerator<E>> operator) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(operator, Messages.NULL_PIPE_PROCESSOR_REFERENCE);
        this.source = Optional.of(source);
        this.isPipeSource = false;
        this.operator = operator;
    }
    protected PipeEnumerable(Enumerable<E> source,
                             Function<Enumerator<T>, Enumerator<E>> operator) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNonEnumerating(source);
        Utils.ensureNotNull(operator, Messages.NULL_PIPE_PROCESSOR_REFERENCE);
        this.source = Optional.of((Iterable<T>)source);
        this.isPipeSource = true;
        this.operator = operator;
    }

    public static <T,E> PipeEnumerable<T,E> of(
            Iterable<? extends T> source,
            Function<Enumerator<T>, Enumerator<E>> operator) {
        return (source instanceof PipeEnumerable<?,?>)
                ? new PipeEnumerable((PipeEnumerable<T,E>)source, operator)
                : new PipeEnumerable(source, operator);        
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        LinkedList<Function<Enumerator<?>,Enumerator<?>>> operators =
                new LinkedList<>();

        PipeEnumerable<?,?> ptr = this;
        while(ptr.isPipeSource) {
            operators.addFirst((Function<Enumerator<?>,Enumerator<?>>)
                               (Object)ptr.operator);
            ptr = (PipeEnumerable<?,?>)ptr.source.get();
        }
        operators.addFirst((Function<Enumerator<?>,Enumerator<?>>)
                           (Object)ptr.operator);

        Enumerator<?> oldIt = Enumerator.of(ptr.source.get().iterator());
        Enumerator<?> newIt = null;
        for(Function<Enumerator<?>,Enumerator<?>> op: operators) {
            newIt = op.apply(oldIt);
            oldIt = newIt;
        }
        assert oldIt == newIt;

        return (Enumerator<E>)newIt;
    }

    // ---------------------------------------------------------------------- //

    static <E> Enumerable<E> asTolerant(
            Enumerable<E> enumerable,
            Consumer<? super Exception> handler,
            int retries) {
        return of(enumerable,
                  in -> in.asTolerant(handler, retries));
    }

    static <E,T> Enumerable<Pair<E,T>> cartesianProduct(
            Enumerable<E> enumerable,
            Iterable<T> other) {
        return (Enumerable<Pair<E,T>>)of(enumerable,
                in -> in.cartesianProduct(() -> other.iterator()));
    }

    static <E> Enumerable<E> concat(
            Enumerable<E> enumerable,
            Iterable<? extends E> elements) {
        return of(enumerable, in -> in.concat(elements.iterator()));
    }

    static <E> Enumerable<E> distinct(Enumerable<E> enumerable) {
        return of(enumerable, in -> in.distinct());
    }

    static <E> Enumerable<E> filter(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable, in -> in.filter(predicate));
    }

    static <E,R> Enumerable<R> flatMap(
            Enumerable<E> enumerable,
            Function<? super E, ? extends Iterable<? extends R>> mapper) {
        return of(enumerable,
                  in -> in.flatMap(e -> mapper.apply(e).iterator()));
    }

    static <E,R> Enumerable<R> indexedMap(
            Enumerable<E> enumerable,
            BiFunction<? super E, ? super Long, ? extends R> mapper) {
        return of(enumerable, in -> in.indexedMap(mapper));
    }

    static <E> Enumerable<E> limit(Enumerable<E> enumerable, long maxSize) {
        return of(enumerable, in -> in.limit(maxSize));
    }

    static <E> Enumerable<E> limitWhile(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable, in -> in.limitWhile(predicate));
    }

    static <E,R> Enumerable<R> map(
            Enumerable<E> enumerable,
            Function<? super E, ? extends R> mapper) {
        return of(enumerable, in -> in.map(mapper));
    }

    static <E> Enumerable<E> reverse(Enumerable<E> enumerable) {
        return of(enumerable, in -> in.reverse());
    }

    static <E> Enumerable<E> skip(Enumerable<E> enumerable, long n) {
        return of(enumerable, in -> in.skip(n));
    }

    static <E> Enumerable<E> skipWhile(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable, in -> in.skipWhile(predicate));
    }

    static <E> Enumerable<E> sorted(Enumerable<E> enumerable) {
        return of(enumerable, in -> in.sorted());
    }

    static <E> Enumerable<E> sorted(
            Enumerable<E> enumerable,
            Comparator<? super E> comparator) {
        return of(enumerable, in -> in.sorted(comparator));
    }

    static <E> Enumerable<E> takeWhile(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable, in -> in.takeWhile(predicate));
    }

    static <E> Enumerable<Optional<E>[]> zipAll(
            Enumerable<E> enumerable,
            Iterable<? extends E> first,
            Iterable<? extends E>... rest) {
        Utils.ensureNonEnumerating(enumerable);
        Utils.ensureNotNull(first, Messages.NULL_ITERATOR);
        for(Iterable<?> it: rest) {
            Utils.ensureNotNull(it, Messages.NULL_ITERATOR);
        }
        return of(enumerable,
                in -> {
                    final PipeEnumerator<E> pipeEnum =
                            in instanceof PipeEnumerator<?>
                            ? (PipeEnumerator<E>)in
                            : new PipeEnumerator((Iterator<E>)in);
                    return pipeEnum.zipAll(first.iterator(),
                            Enumerator.on(rest)
                                      .map(Iterable::iterator)
                                      .toList());
        });
    }
}
