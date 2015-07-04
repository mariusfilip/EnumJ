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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

final class PipeEnumerable<T,E> extends AbstractEnumerable<E> {

    private final Iterable<T> source;
    private final Optional<PipeEnumerable<?,? extends T>> pipeSource;
    private final PipeOperator<E,T> reversedOperator;
    private final Lazy<Boolean> onceOnly;

    protected PipeEnumerable(Iterable<T> source,
                             Function<Enumerator<E>, Enumerator<T>> operator,
                             Supplier<Boolean> onceOnly) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(operator, Messages.NULL_PIPE_PROCESSOR_REFERENCE);
        this.source = source;
        this.pipeSource = Optional.empty();
        this.reversedOperator = new PipeOperator(operator, onceOnly);
        this.onceOnly = new Lazy(this::getOnceOnly);
    }
    protected PipeEnumerable(PipeEnumerable<?,T> source,
                             Function<Enumerator<E>, Enumerator<T>> operator,
                             Supplier<Boolean> onceOnly) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNonEnumerating(source);
        Utils.ensureNotNull(operator, Messages.NULL_PIPE_PROCESSOR_REFERENCE);
        this.source = source;
        this.pipeSource = Optional.of(source);
        this.reversedOperator = new PipeOperator(operator, onceOnly);
        this.onceOnly = new Lazy(this::getOnceOnly);
    }

    private Boolean getOnceOnly() {
        PipeEnumerable<?,?> ptr = this;
        while(true) {
            if (ptr.reversedOperator.onceOnly.get()) {
                return true;
            }
            if (ptr.pipeSource.isPresent()) {
                ptr = ptr.pipeSource.get();
            } else {
                break;
            }
        }
        return Enumerable.onceOnly(ptr.source);
    }

    @Override
    protected boolean internalOnceOnly() {
        return onceOnly.get();
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        PipeEnumerator en = new PipeEnumerator();
        PipeEnumerable<?,?> ptr = this;
        
        while(true) {
            en = (PipeEnumerator)ptr.reversedOperator.apply(en);
            if (ptr.pipeSource.isPresent()) {
                ptr = ptr.pipeSource.get();
            } else {
                break;
            }
        }
        return en.setSource(ptr.source.iterator());
    }

    // ---------------------------------------------------------------------- //

    public static <T,E> PipeEnumerable<T,E> of(
            Iterable<? extends T>                  source,
            Function<Enumerator<E>, Enumerator<T>> operator,
            Supplier<Boolean>                      onceOnly) {
        return (source instanceof PipeEnumerable<?,?>)
                ? new PipeEnumerable((PipeEnumerable<T,E>)source,
                                     operator,
                                     onceOnly)
                : new PipeEnumerable(source, operator, onceOnly);
    }

    // ---------------------------------------------------------------------- //

    static <E> Enumerable<E> concat(
            Enumerable<E> enumerable,
            Iterable<? extends E> elements) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedConcat(
                          elements.iterator()),
                  () -> Enumerable.onceOnly(elements));
    }

    static <E> Enumerable<E> distinct(Enumerable<E> enumerable) {
        return of(enumerable,
                  in -> AbstractEnumerator.distinct(in, true),
                  () -> false);                          
    }

    static <E> Enumerable<E> filter(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedFilter(predicate),
                  () -> false);
    }

    static <E,R> Enumerable<R> flatMap(
            Enumerable<E> enumerable,
            Function<? super E, ? extends Iterable<? extends R>> mapper) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedFlatMap(
                          e -> mapper.apply((E)e).iterator()),
                  () -> false);
    }

    static <E,R> Enumerable<R> indexedMap(
            Enumerable<E> enumerable,
            BiFunction<? super E, ? super Long, ? extends R> mapper) {
        return of(enumerable,
                  in -> AbstractEnumerator.indexedMap((Enumerator)in,
                                                      mapper,
                                                      true),
                  () -> false);
    }

    static <E> Enumerable<E> limit(Enumerable<E> enumerable, long maxSize) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedLimit(maxSize),
                  () -> false);
    }

    static <E> Enumerable<E> limitWhile(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedTakeWhile(predicate),
                  () -> false);
    }

    static <E,R> Enumerable<R> map(
            Enumerable<E> enumerable,
            Function<? super E, ? extends R> mapper) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedMap(mapper),
                  () -> false);
    }

    static <E> Enumerable<E> peek(
            Enumerable<E> enumerable,
            Consumer<? super E> action) {
        return of(enumerable,
                  in -> AbstractEnumerator.peek(in, action, true),
                  () -> false);
    }

    static <E> Enumerable<E> skip(Enumerable<E> enumerable, long n) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedSkip(n),
                  () -> false);
    }

    static <E> Enumerable<E> skipWhile(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedSkipWhile(predicate),
                  () -> false);
    }

    static <E> Enumerable<E> takeWhile(
            Enumerable<E> enumerable,
            Predicate<? super E> predicate) {
        return of(enumerable,
                  in -> ((PipeEnumerator)in).reversedTakeWhile(predicate),
                  () -> false);
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
                    final PipeEnumerator pipe = (PipeEnumerator)in;
                    final Iterator firstIt = first.iterator();
                    final List<Iterator> restList = 
                            Enumerator.of(Arrays.asList(rest))
                                      .map(Iterable::iterator)
                                      .map(it -> (Iterator)it)
                                      .toList();
                    return pipe.reversedZipAll(firstIt, restList);
                },
                () -> Enumerable.onceOnly(first)
                      || Enumerator.of(Arrays.asList(rest))
                                   .anyMatch(it -> Enumerable.onceOnly(it)));
    }
}
