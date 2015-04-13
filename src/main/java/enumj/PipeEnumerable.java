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
import java.util.function.UnaryOperator;
import org.apache.commons.lang3.tuple.Pair;

class PipeEnumerable<E> extends AbstractEnumerable<E> {

    protected Optional<Enumerable<E>> enumerableSource;
    protected Optional<Pair<E,UnaryOperator<E>>> iterationSource;
    protected Optional<Pair<E,Long>> repetitionSource;
    protected LinkedList<Function<Enumerator<?>,
                                  Enumerator<?>>> builders;

    PipeEnumerable(Enumerable<? extends E> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNonEnumerating(source);
        Initialise(Optional.of((Enumerable<E>)source),
                   Optional.empty(),
                   Optional.empty());
    }
    PipeEnumerable(E seed, UnaryOperator<E> f) {
        Utils.ensureNotNull(f, Messages.NULL_ENUMERATOR_GENERATOR);
        Initialise(Optional.empty(),
                   Optional.of(Pair.of(seed, f)),
                   Optional.empty());
    }
    PipeEnumerable(E element, long count) {
        Utils.ensureNonNegative(count, Messages.NEGATIVE_ENUMERATOR_SIZE);
        Initialise(Optional.empty(),
                   Optional.empty(),
                   Optional.of(Pair.of(element, count)));
    }
    private PipeEnumerable() {}

    private void Initialise(
            Optional<Enumerable<E>>            enumerableSource,
            Optional<Pair<E,UnaryOperator<E>>> iterationSource,
            Optional<Pair<E,Long>>             repetitionSource) {
        this.enumerableSource = enumerableSource;
        this.iterationSource = iterationSource;
        this.repetitionSource = repetitionSource;
        this.builders = new LinkedList<>();
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        Enumerator<?> result = initialEnumerator();
        for(Function<Enumerator<?>,Enumerator<?>> builder: builders) {
            result = builder.apply(result);
        }
        return (Enumerator<E>)result;
    }

    protected Enumerator<E> initialEnumerator() {
        if (enumerableSource.isPresent()) {
            return enumerableSource.get().enumerator();
        }
        if (iterationSource.isPresent()) {
            return Enumerator.iterate(iterationSource.get().getLeft(),
                                      iterationSource.get().getRight());
        }
        if (repetitionSource.isPresent()) {
            return Enumerator.repeat(repetitionSource.get().getLeft(),
                                     repetitionSource.get().getRight());
        }
        assert false;
        return null;
    }

    @Override
    protected AbstractEnumerable<E> internalNewClone() {
        return new PipeEnumerable<E>();
    }
    @Override
    protected void internalCopyClone(AbstractEnumerable<E> source) {
        PipeEnumerable<E> src = (PipeEnumerable<E>)source;
        this.Initialise(src.enumerableSource,
                        src.iterationSource,
                        src.repetitionSource);
    }
    @Override
    protected boolean internalCloneable() { return true; }

    // ---------------------------------------------------------------------- //

    @Override
    public Enumerable<E> asTolerant(Consumer<? super Exception> handler,
                                    int retries) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNonNegative(retries, Messages.NEGATIVE_RETRIES);
        builders.add(in -> in.asTolerant(handler, retries));
        return this;
    }

    @Override
    public <T> Enumerable<Pair<E,T>> cartesianProduct(Iterable<T> other) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(other, Messages.NULL_ENUMERATOR_SOURCE);
        builders.add(in -> in.cartesianProduct(() -> other.iterator()));
        return (Enumerable<Pair<E,T>>)this;
    }

    @Override
    public Enumerable<E> concat(Iterable<? extends E> elements) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(elements, Messages.NULL_ENUMERATOR_SOURCE);
        builders.add(in -> ((Enumerator<E>)in).concat(elements.iterator()));
        return this;
    }
    
    @Override
    public Enumerable<E> distinct() {
        Utils.ensureNonEnumerating(this);
        builders.add(in -> in.distinct());
        return this;
    }

    @Override
    public Enumerable<E> filter(Predicate<? super E> predicate) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        builders.add(in -> in.filter(e -> predicate.test((E)e)));
        return this;
    }

    @Override
    public <R> Enumerable<R> flatMap(
            Function<? super E, ? extends Iterator<? extends R>> mapper) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(mapper, Messages.NULL_ENUMERATOR_MAPPER);
        builders.add(in -> in.flatMap(e -> mapper.apply((E)e)));
        return (Enumerable<R>)this;
    }
    
    @Override
    public <R> Enumerable<R> indexedMap(
            BiFunction<? super E, ? super Long, ? extends R> mapper) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(mapper, Messages.NULL_ENUMERATOR_MAPPER);
        builders.add(in -> ((Enumerator<E>)in).indexedMap(mapper));
        return (Enumerable<R>)this;
    }

    @Override
    public Enumerable<E> limit(long maxSize) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNonNegative(maxSize, Messages.NEGATIVE_ENUMERATOR_SIZE);
        builders.add(in -> in.limit(maxSize));
        return this;
    }

    @Override
    public Enumerable<E> limitWhile(Predicate<? super E> predicate) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        builders.add(in -> ((Enumerator<E>)in).limitWhile(predicate));
        return this;
    }

    @Override
    public <R> Enumerable<R> map(
            Function<? super E, ? extends R> mapper) {
        Utils.ensureNonEnumerating(this);
        builders.add(in -> in.map(e -> mapper.apply((E)e)));
        return (Enumerable<R>)this;
    }

    @Override
    public Enumerable<E> reverse() {
        Utils.ensureNonEnumerating(this);
        builders.add(in -> in.reverse());
        return this;
    }

    @Override
    public Enumerable<E> skip(long n) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNonNegative(n, Messages.NEGATIVE_ENUMERATOR_SIZE);
        builders.add(in -> in.skip(n));
        return this;
    }

    @Override
    public Enumerable<E> skipWhile(Predicate<? super E> predicate) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        builders.add(in -> ((Enumerator<E>)in).skipWhile(predicate));
        return this;
    }

    @Override
    public Enumerable<E> sorted() {
        Utils.ensureNonEnumerating(this);
        builders.add(in -> in.sorted());
        return this;
    }

    @Override
    public Enumerable<E> sorted(Comparator<? super E> comparator) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(comparator, Messages.NULL_ENUMERATOR_COMPARATOR);
        builders.add(in -> ((Enumerator<E>)in).sorted(comparator));
        return this;
    }
    
    @Override
    public Enumerable<E> takeWhile(Predicate<? super E> predicate) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        builders.add(in -> ((Enumerator<E>)in).takeWhile(predicate));
        return this;
    }

    @Override
    public Enumerable<Optional<E>[]> zipAll(Iterable<? extends E> first,
                                            Iterable<? extends E>... rest) {
        Utils.ensureNonEnumerating(this);
        Utils.ensureNotNull(first, Messages.NULL_ITERATOR);
        for(Iterable<?> it: rest) {
            Utils.ensureNotNull(it, Messages.NULL_ITERATOR);
        }
        builders.add(in -> {
            final PipeEnumerator<E> pipeEnum =
                    in instanceof PipeEnumerator<?>
                    ? (PipeEnumerator<E>)in
                    : new PipeEnumerator((Iterator<E>)in);
            return pipeEnum.zipAll(first.iterator(),
                                   Enumerator.on(rest)
                                             .map(Iterable::iterator)
                                             .toList());
        });
        return (Enumerable<Optional<E>[]>)this;
    }
}
