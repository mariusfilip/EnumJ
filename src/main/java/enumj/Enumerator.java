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
               : new BasicEnumerator<>(source);
    }

    public static <E> Enumerator<E> of(Iterable<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        return of(source.iterator());
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
        return new SuppliedEnumerator(source);
    }

    public static <E> Enumerator<E> of(IntSupplier indexSupplier,
                                       Iterator<E> first,
                                       Iterator<E> second,
                                       Iterator<E>... rest) {
        return new ChoiceEnumerator(indexSupplier, first, second, rest);
    }

    public static <E> Enumerator<E> of(long count, E element) {
        Utils.ensureNonNegative(count, Messages.NegativeEnumeratorSize);
        return rangeLong(0, count).map(i -> element);
    }

    // ---------------------------------------------------------------------- //

    public default <T> Enumerator<T> as(Class<T> clazz) {
        return filter(e -> clazz.isInstance(e)).map(e -> (T)e);
    }
    
    public default Iterable<E> asIterable() {
        return new Enumerable<E>(this);
    }

    public default Stream<E> asStream() {
        return StreamSupport.stream(asSpliterator(), false);
    }

    public default Spliterator<E> asSpliterator() {
        return Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED);
    }

    public default Supplier<Optional<E>> asSupplier() {
        return () -> hasNext() ? Optional.of(next()) : Optional.empty();
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

    public default <R, A> R collect(Collector<? super E, A, R> collector) {
        return asStream().collect(collector);
    }

    public default Enumerator<E> concat(Iterator<E> elements) {
        return new ConcatEnumerator(this, elements);
    }

    public default Enumerator<E> concat(Iterable<E> elements) {
        return concat(of(elements));
    }

    public default Enumerator<E> concat(Stream<E> elements) {
        return concat(of(elements));
    }

    public default Enumerator<E> concat(Spliterator<E> elements) {
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
        return new EmptyEnumerator<E>();
    }

    public default Enumerator<E> filter(Predicate<? super E> predicate) {
        return new FilterEnumerator(this, predicate);
    }

    public default <R> Enumerator<R> flatMap(Function<? super E,
                                                      ? extends Iterator<? extends R>> mapper) {
        return new FlatteningEnumerator(map(mapper));
    }

    public default void forEach(Consumer<? super E> consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
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
        return limitWhile(e -> {
            if (size.longValue() >= maxSize) {
                return false;
            }
            size.setValue(1+size.longValue());
            return true;
        });
    }

    public default Enumerator<E> limitWhile(Predicate<? super E> predicate) {
        return new WhileEnumerator<>(this, predicate);
    }

    public default <R> Enumerator<R> map(Function<? super E, ? extends R> mapper) {
        return new MapEnumerator(this, mapper);
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

    public default Enumerator<E> prepend(E... elements) {
        return on(elements).concat(this);
    }

    public static <E> Enumerator<E> range(E startInclusive,
                                          E endExclusive,
                                          UnaryOperator<E> succ,
                                          Comparator<E> cmp) {
        return cmp.compare(startInclusive, endExclusive) == 0
               ? Enumerator.empty()
               : iterate(startInclusive, succ)
                        .limitWhile(e -> cmp.compare(e, endExclusive) < 0);
    }

    public static <E> Enumerator<E> rangeClosed(E startInclusive,
                                                E endInclusive,
                                                UnaryOperator<E> succ,
                                                Comparator<E> cmp) {
        return range(startInclusive, endInclusive, succ, cmp)
                    .append(endInclusive);
    }

    public static Enumerator<Integer> rangeInt(int startInclusive,
                                               int endExclusive) {
        return range(startInclusive, endExclusive,
                     i -> i+1, Comparator.comparingInt(i -> i));
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

    public default Enumerator<E> reverse() {
        final List<E> elements = collect(Collectors.toList());
        Collections.reverse(elements);
        return of(elements);
    }

    public default E single() {
        if (!hasNext()) {
            throw new InputMismatchException(Messages.NoSingleEnumeratorElement);
        }
        E result = next();
        if (hasNext()) {
            throw new InputMismatchException(Messages.NoSingleEnumeratorElement);
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
            size.setValue(1+size.longValue());
            return true;
        });
    }

    public default Enumerator<E> skipWhile(Predicate<? super E> predicate) {
        return new SkipWhileEnumerator(this, predicate);
    }

    public default Enumerator<E> sorted() {
        return of(asStream().sorted());
    }

    public default Enumerator<E> sorted(Comparator<? super E> comparator) {
        return of(asStream().sorted(comparator));
    }

    public default E[] toArray(Class<E> clazz) {
        return toList().toArray((E[])Array.newInstance(clazz, 0));
    }

    public default List<E> toList() {
        return collect(Collectors.toList());
    }

    public default <K, V> Map<K, V> toMap(Function<? super E, ? extends K> keyMapper,
                                          Function<? super E, ? extends V> valueMapper) {
        return collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public default Set<E> toSet() {
        return collect(Collectors.toSet());
    }

    public default Enumerator<E> union(Iterator<E> others) {
        return concat(others).distinct();
    }

    public default <V> Enumerator<Pair<E, V>> zipBoth(Iterator<V> others) {
        return new ZipBothEnumerator(this, others);
    }

    public default <V>
                   Enumerator<Pair<E, Optional<Mutable<V>>>>
                   zipLeft(Iterator<V> elements) {
        return new ZipLeftEnumerator(this, elements);
    }

    public default <V>
                   Enumerator<Pair<Optional<Mutable<E>>, V>>
                   zipRight(Iterator<V> elements) {
        return new ZipRightEnumerator(this, elements);
    }

    public default <V>
                   Enumerator<Pair<Optional<Mutable<E>>, Optional<Mutable<E>>>>
                   zipAny(Iterator<V> elements) {
        return new ZipAnyEnumerator(this, elements);
    }
}
