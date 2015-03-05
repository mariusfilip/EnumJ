/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
public interface Enumerator<E> extends Iterator<E> {

    public static <E> Enumerator<E> on(E... elements) {
        return of(Arrays.asList(elements));
    }

    public static <E> Enumerator<E> of(Iterator<E> source) {
        return (source != null && source instanceof Enumerator)
               ? (Enumerator<E>)source
               : new PipeEnumerator<>(source);
    }

    public static <E> Enumerator<E> of(Iterable<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return of(source.iterator());
    }

    public static <E> Enumerator<E> of(Enumeration<E> source) {
        return new PipeEnumerator(new EnumerationEnumerator(source));
    }

    public static <E> Enumerator<E> of(Stream<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return of(source.iterator());
    }

    public static <E> Enumerator<E> of(Spliterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return of(Spliterators.iterator(source));
    }

    public static <E> Enumerator<E> of(Supplier<Optional<E>> source) {
        return new PipeEnumerator(new SuppliedEnumerator(source));
    }

    public static <E> Enumerator<E> ofLazyIterator(
            Supplier<? extends Iterator<E>> source) {
        return new LazyEnumerator(source);
    }

    public static <E> Enumerator<E> ofLazyIterable(
            Supplier<? extends Iterable<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return new LazyEnumerator(() -> of(source.get()));
    }

    public static <E> Enumerator<E> ofLazyEnumeration(
            Supplier<? extends Enumeration<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return new LazyEnumerator(() -> of(source.get()));
    }

    public static <E> Enumerator<E> ofLazyStream(
            Supplier<? extends Stream<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return new LazyEnumerator(() -> of(source.get()));
    }

    public static <E> Enumerator<E> ofLazySpliterator(
            Supplier<? extends Spliterator<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return new LazyEnumerator(() -> of(source.get()));
    }

    // ---------------------------------------------------------------------- //

    public default <T> Enumerator<T> as(Class<T> clazz) {
        return (Enumerator<T>)this;
    }

    public default <T> Enumerator<T> asFiltered(Class<T> clazz) {
        return filter(e -> clazz.isInstance(e)).as(clazz);
    }

    public default Iterable<E> asIterable() {
        return new Enumerable<E>(this);
    }

    public default Enumeration<E> asEnumeration() {
        return new EnumerableEnumeration(this);
    }

    public default Spliterator<E> asSpliterator() {
        return Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED);
    }

    public default Stream<E> asStream() {
        return StreamSupport.stream(asSpliterator(), false);
    }

    public default Supplier<Optional<E>> asSupplier() {
        return () -> hasNext() ? Optional.of(next()) : Optional.empty();
    }

    public default ShareableEnumerator<E> asShareable() {
        return new ShareableEnumerator<E>(this);
    }

    // ---------------------------------------------------------------------- //

    public default boolean allMatch(Predicate<? super E> predicate) {
        while(hasNext()) {
            if (!predicate.test(next())) {
                return false;
            }
        }
        return true;
    }

    public default boolean anyMatch(Predicate<? super E> predicate) {
        while(hasNext()) {
            if (predicate.test(next())) {
                return true;
            }
        }
        return false;
    }

    public default Enumerator<E> append(E... elements) {
        return concat(on(elements));
    }

    public static <E> Enumerator<E> choiceOf(
            IntSupplier indexSupplier,
            Iterator<E> first,
            Iterator<? extends E> second,
            Iterator<? extends E>... rest) {
        final int size = 1 + 1 + rest.length;
        final IntUnaryOperator altIndexSupplier = i -> (i+1)%size;
        return choiceOf(indexSupplier,
                        altIndexSupplier,
                        first,
                        second,
                        rest);
    }

    public static <E> Enumerator<E> choiceOf(
            IntSupplier indexSupplier,
            IntUnaryOperator altIndexSupplier,
            Iterator<E> first,
            Iterator<? extends E> second,
            Iterator<? extends E>... rest) {
        return new ChoiceEnumerator(indexSupplier,
                                    altIndexSupplier,
                                    first,
                                    second,
                                    rest);
    }

    public default <R, A> R collect(Collector<? super E, A, R> collector) {
        return asStream().collect(collector);
    }

    public default Enumerator<E> concatOn(E... elements) {
        return concat(on(elements));
    }

    public default Enumerator<E> concat(Iterator<? extends E> elements) {
        return new FlatteningEnumerator(Collections.emptyIterator())
                   .concat(this)
                   .concat(elements);
    }

    public default Enumerator<E> concat(Iterable<? extends E> elements) {
        return concat(of(elements));
    }

    public default Enumerator<E> concat(Enumeration<? extends E> elements) {
        return concat(of(elements));
    }

    public default Enumerator<E> concat(Stream<? extends E> elements) {
        return concat(of(elements));
    }

    public default Enumerator<E> concat(Spliterator<? extends E> elements) {
        return concat(of(elements));
    }

    public default Enumerator<E> concat(Supplier<Optional<E>> elements) {
        return concat(of(elements));
    }

    public default boolean contains(E element) {
        while (hasNext()) {
            if (Objects.equals(element, next())) {
                return true;
            }
        }
        return false;
    }

    public default long count() {
        long cnt = 0;
        while (hasNext()) {
            ++cnt;
            next();
        }
        return cnt;
    }

    public default Enumerator<E> distinct() {
        return of(asStream().distinct());
    }

    public default Optional<E> elementAt(long index) {
        Utils.ensureNonNegative(index, Messages.NegativeEnumeratorIndex);
        Enumerator<E> skipped = skip(index);
        return Optional.ofNullable(skipped.hasNext()
                                   ? skipped.next()
                                   : null);
    }

    public default <U> boolean elementsEqual(Iterator<U> elements) {
        Utils.ensureNotNull(elements, Messages.NullIterator);
        while (hasNext() || elements.hasNext()) {
            if (hasNext() != elements.hasNext()) {
                return false;
            }
            final E thisElement = next();
            final U otherElement = elements.next();
            if (!Objects.equals(thisElement, otherElement)) {
                return false;
            }
        }
        return true;
    }

    public static <E> Enumerator<E> empty() {
        return of(Collections.emptyIterator());
    }

    public default Enumerator<E> filter(Predicate<? super E> predicate) {
        return new PipeEnumerator(this).filter(predicate);
    }

    public default Optional<E> first() {
        return hasNext() ? Optional.of(next()) : Optional.empty();
    }

    public default <R> Enumerator<R> flatMap(
            Function<? super E, ? extends Iterator<? extends R>> mapper) {
        return new FlatteningEnumerator(map(mapper));
    }

    public default void forEach(Consumer<? super E> consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
    }

    public default <R> Enumerator<R> indexedMap(
            Function<? super Pair<? super Long, ? super E>,
                     ? extends R> mapper) {
        final MutableLong index = new MutableLong(0);
        return map(e -> {
            final R result = mapper.apply(Pair.of(index.toLong(), e));
            index.add(1);
            return result;
        });
    }

    public static <E> Enumerator<E> iterate(E seed, UnaryOperator<E> f) {
        Utils.ensureNotNull(f, Messages.NullEnumeratorGenerator);
        final Mutable<E> result = new MutableObject(seed);
        final MutableBoolean first = new MutableBoolean(true);
        return of(() -> {
            if (first.booleanValue()) {
                first.setValue(false);
            } else {
                result.setValue(f.apply(result.getValue()));
            }
            return Optional.of(result.getValue());
        });
    }

    public default Optional<E> last() {
        E result = null;
        while (hasNext()) {
            result = next();
        }
        return Optional.ofNullable(result);
    }

    public default Enumerator<E> limit(long maxSize) {
        Utils.ensureNonNegative(maxSize, Messages.NegativeEnumeratorSize);
        final MutableLong size = new MutableLong(0);
        return takeWhile(e -> {
            if (size.longValue() >= maxSize) {
                return false;
            }
            size.setValue(1+size.longValue());
            return true;
        });
    }

    public default Enumerator<E> limitWhile(Predicate<? super E> predicate) {
        return takeWhile(predicate);
    }

    public default <R> Enumerator<R> map(
            Function<? super E, ? extends R> mapper) {
        return new PipeEnumerator(this).map(mapper);
    }

    public default Optional<E> max(Comparator<? super E> comparator) {
        E m = null;
        while (hasNext()) {
            E e = next();
            m = (m == null || comparator.compare(e, m) > 0) ? e : m;
        }
        return Optional.ofNullable(m);
    }

    public default Optional<E> min(Comparator<? super E> comparator) {
        E m = null;
        while (hasNext()) {
            E e = next();
            m = (m == null || comparator.compare(e, m) < 0) ? e : m;
        }
        return Optional.ofNullable(m);
    }

    public default boolean noneMatch(Predicate<? super E> predicate) {
        while (hasNext()) {
            if (predicate.test(next())) {
                return false;
            }
        }
        return true;
    }

    public default Enumerator<E> peek(Consumer<? super E> action) {
        return map(e -> { action.accept(e); return e; });
    }

    public default Enumerator<E> prependOn(E... elements) {
        return prepend(on(elements));
    }

    public default Enumerator<E> prepend(Iterator<? extends E> elements) {
        return new FlatteningEnumerator(Collections.emptyIterator())
                   .concat(elements)
                   .concat(this);
    }

    public default Enumerator<E> prepend(Iterable<? extends E> elements) {
        return prepend(of(elements));
    }

    public default Enumerator<E> prepend(Enumeration<? extends E> elements) {
        return prepend(of(elements));
    }

    public default Enumerator<E> prepend(Stream<? extends E> elements) {
        return prepend(of(elements));
    }

    public default Enumerator<E> prepend(Spliterator<? extends E> elements) {
        return prepend(of(elements));
    }

    public default Enumerator<E> prepend(Supplier<Optional<E>> elements) {
        return prepend(of(elements));
    }

    public static <E> Enumerator<E> range(E startInclusive,
                                          E endExclusive,
                                          UnaryOperator<E> succ,
                                          Comparator<? super E> cmp) {
        return cmp.compare(startInclusive, endExclusive) >= 0
               ? Enumerator.empty()
               : iterate(startInclusive, succ)
                        .takeWhile(e -> cmp.compare(e, endExclusive) < 0);
    }

    public static <E> Enumerator<E> rangeClosed(E startInclusive,
                                                E endInclusive,
                                                UnaryOperator<E> succ,
                                                Comparator<? super E> cmp) {
        return cmp.compare(startInclusive, endInclusive) > 0
               ? Enumerator.empty()
               : iterate(startInclusive, succ)
                        .takeWhile(e -> cmp.compare(e, endInclusive) <= 0);
    }

    public static Enumerator<Integer> rangeInt(int startInclusive,
                                               int endExclusive) {
        return range(startInclusive, endExclusive,
                     i -> i+1, Comparator.comparingInt(n -> n));
    }

    public static Enumerator<Integer> rangeIntClosed(int startInclusive,
                                                     int endInclusive) {
        return rangeClosed(startInclusive, endInclusive,
                           i -> i+1, Comparator.comparingInt(i -> i));
    }

    public static Enumerator<Long> rangeLong(long startInclusive,
                                             long endExclusive) {
        return range(startInclusive, endExclusive,
                     i -> i+1, Comparator.comparingLong(i -> i));
    }

    public static Enumerator<Long> rangeLongClosed(long startInclusive,
                                                   long endInclusive) {
        return rangeClosed(startInclusive, endInclusive,
                           i -> i+1, Comparator.comparingLong(i -> i));
    }

    public default Optional<E> reduce(BinaryOperator<E> accumulator) {
        if (!hasNext()) {
            return Optional.empty();
        }
        E first = next();
        return Optional.of(reduce(first, accumulator));
    }

    public default E reduce(E identity, BinaryOperator<E> accumulator) {
        E result = identity;
        while (hasNext()) {
            result = accumulator.apply(result, next());
        }
        return result;
    }

    public static <E> Enumerator<E> repeat(E element, int count) {
        Utils.ensureNonNegative(count, Messages.NegativeEnumeratorSize);
        return of(Collections.nCopies(count, element));
    }

    public default Enumerator<E> reverse() {
        final List<E> elements = collect(Collectors.toList());
        Collections.reverse(elements);
        return of(elements);
    }

    public default E single() {
        if (!hasNext()) {
            throw new InputMismatchException(
                    Messages.NoSingleEnumeratorElement);
        }
        E result = next();
        if (hasNext()) {
            throw new InputMismatchException(
                    Messages.NoSingleEnumeratorElement);
        }
        return result;
    }

    public default Enumerator<E> skip(long n) {
        Utils.ensureNonNegative(n, Messages.NegativeEnumeratorSize);
        final MutableLong size = new MutableLong(0);
        return skipWhile(e -> {
            if (size.longValue() >= n) {
                return false;
            }
            size.add(1);
            return true;
        });
    }

    public default Enumerator<E> skipWhile(Predicate<? super E> predicate) {
        return new PipeEnumerator(this).skipWhile(predicate);
    }

    public default Enumerator<E> sorted() {
        return of(asStream().sorted());
    }

    public default Enumerator<E> sorted(Comparator<? super E> comparator) {
        return of(asStream().sorted(comparator));
    }

    public default Enumerator<E> take(long n) {
        return limit(n);
    }

    public default Enumerator<E> takeWhile(Predicate<? super E> predicate) {
        return new PipeEnumerator(this).takeWhile(predicate);
    }

    public default E[] toArray(Class<E> clazz) {
        return toList().toArray((E[])Array.newInstance(clazz, 0));
    }

    public default List<E> toList() {
        return collect(Collectors.toList());
    }

    public default <K, V> Map<K, V> toMap(Function<? super E, K> keyMapper,
                                          Function<? super E, V> valueMapper) {
        return collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public default Set<E> toSet() {
        return collect(Collectors.toSet());
    }

    public default Enumerator<E> unionOn(E... others) {
        return union(on(others));
    }

    public default Enumerator<E> union(Iterator<? extends E> others) {
        return concat(others).distinct();
    }

    public default Enumerator<E> union(Iterable<? extends E> others) {
        return union(of(others));
    }

    public default Enumerator<E> union(Enumeration<? extends E> others) {
        return prepend(of(others));
    }

    public default Enumerator<E> union(Stream<? extends E> others) {
        return prepend(of(others));
    }

    public default Enumerator<E> union(Spliterator<? extends E> others) {
        return prepend(of(others));
    }

    public default Enumerator<E> union(Supplier<Optional<E>> others) {
        return prepend(of(others));
    }

    public default <V>
                   Enumerator<Pair<Optional<E>, Optional<V>>>
                   zipAny(Iterator<V> elements) {
        return new ZipAnyEnumerator(this, elements);
    }

    public default <V> Enumerator<Pair<E, V>> zipBoth(Iterator<V> others) {
        return new ZipBothEnumerator(this, others);
    }

    public default <V>
                   Enumerator<Pair<E, Optional<V>>>
                   zipLeft(Iterator<V> elements) {
        return new ZipLeftEnumerator(this, elements);
    }

    public default <V>
                   Enumerator<Pair<Optional<E>, V>>
                   zipRight(Iterator<V> elements) {
        return new ZipRightEnumerator(this, elements);
    }
}
