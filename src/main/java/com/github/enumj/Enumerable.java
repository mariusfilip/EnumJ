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
package com.github.enumj;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;

/**
 * {@code Iterable} with high composability that returns an
 * {@code Enumerator} instance for its {@code Iterator}.
 *
 * @param <E> the type of elements returned by this enumerable.
 * @see Enumerator
 * @see Iterable
 * @see Iterator
 */
public interface Enumerable<E> extends Iterable<E> {

    /**
     * Returns a new {@code Iterator} instance that can traverse the current
     * {@code Enumerable}.
     * <p>
     * The default implementation of this method returns the result of
     * {@link #enumerator()}.
     * </p>
     *
     * @return {@link Iterator} instance iterating over the current
     * {@link Enumerable} instance.
     * @see #enumerator()
     * @see Iterable
     */
    @Override
    public default Iterator<E> iterator() {
        return enumerator();
    }

    /**
     * Returns a new {@code Enumerator} instance that enumerates over the
     * current {@code Enumerable}.
     * <p>
     * The default implementation of {@link #iterator()} calls this method.
     * </p>
     *
     * @return {@link Enumerator} instance enumerating over the current
     * {@link Enumerable} instance.
     * @see #iterator()
     */
    public Enumerator<E> enumerator();

    /**
     * Returns whether {@code enumerator()} has been called at least once.
     * <p>
     * Most non-static methods of {@link Enumerable} require that this method
     * returns false.
     * </p>
     *
     * @return true if {@link #enumerating()} has been called, false otherwise.
     */
    public boolean enumerating();

    /**
     * Returns whether the enumerable may generate only one iterator or not.
     *
     * @return true if the enumerable may generate only one iterator, false
     * otherwise.
     */
    public boolean onceOnly();

    /**
     * Returns whether the given {@code iterable} may generated only
     * one iterator or not.
     * <p>
     * This method assumes that non-{@link Enumerable} iterables may return
     * more than one iterator.
     * </p>
     *
     * @param iterable {@link Iterable} to check.
     * @return true if {@code iterable} may return only one {@link Iterator},
     * false otherwise.
     */
    public static boolean onceOnly(Iterable<?> iterable) {
        return (iterable instanceof Enumerable)
                ? ((Enumerable<?>)iterable).onceOnly()
                : true;
    }

    // ---------------------------------------------------------------------- //

    /**
     * Returns an {@code Enumerable} enumerating over the given elements.
     *
     * @param <E> type of enumerated elements.
     * @param elements succession of elements to enumerate over.
     * @return {@link Enumerable} instance enumerating over {@code elements}.
     */
    @SafeVarargs
    public static <E> Enumerable<E> on(E... elements) {
        return of(Arrays.asList(elements));
    }

    /**
     * Returns an {@code Enumerable} enumerating over the given {@link Iterable}
     * source.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Iterable} providing the elements to enumerate upon.
     * @return {@link Enumerable} enumerating over {@code source}. 
     */
    public static <E> Enumerable<E> of(Iterable<E> source) {
        return IterableEnumerable.of(source);
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over the given
     * {@code Iterator} source.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Iterator} providing the elements to enumerate upon.
     * @return {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> of(Iterator<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over the given
     * {@code Enumeration}.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Enumeration} providing the elements to enumerate
     * upon.
     * @return  {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> of(Enumeration<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over the given
     * {@code Stream}.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Stream} providing the elements to enumerate upon.
     * @return {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> of(Stream<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over the given
     * {@code Spliterator}.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Spliterator} providing the elements to enumerate
     * upon.
     * @return {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> of(Spliterator<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over a sequence of
     * elements supplied lazily.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Supplier} providing the elements to enumerate. The
     * sequence ends with an empty {@link Optional}.
     * @return {@link OnceEnumerable} enumerating over the sequence of elements.
     */
    public static <E> OnceEnumerable<E> of(Supplier<Optional<E>> source) {
        return Enumerator.of(source).asEnumerable();
    }

    /**
     * Returns an {@code Enumerable} enumerating over an {@code Iterable}
     * supplied lazily.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Supplier} of {@link Iterable}.
     * @return {@link Enumerable} enumerating over {@code source}.
     */
    public static <E> Enumerable<E> ofLazyIterable(
            Supplier<? extends Iterable<E>> source) {
        return LazyEnumerable.of(source);
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over an {@code Iterator}
     * supplied lazily.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Supplier} of {@link Iterator}.
     * @return {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> ofLazyIterator(
            Supplier<? extends Iterator<E>> source) {
        return of(Enumerator.ofLazyIterator(source));
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over an
     * {@code Enumeration} supplied lazily.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Supplier} of {@link Iterator}.
     * @return {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> ofLazyEnumeration(
            Supplier<? extends Enumeration<E>> source) {
        return of(Enumerator.ofLazyEnumeration(source));
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over a
     * {@code Stream} supplied lazily.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Supplier} of {@link Stream}.
     * @return {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> ofLazyStream(
            Supplier<? extends Stream<E>> source) {
        return of(Enumerator.ofLazyStream(source));
    }

    /**
     * Returns an {@code OnceEnumerable} enumerating over a {@link Spliterator}
     * supplied lazily.
     *
     * @param <E> type of enumerated elements.
     * @param source {@link Supplier} of {@link Spliterator}.
     * @return {@link OnceEnumerable} enumerating over {@code source}.
     */
    public static <E> OnceEnumerable<E> ofLazySpliterator(
            Supplier<? extends Spliterator<E>> source) {
        return of(Enumerator.ofLazySpliterator(source));
    }

    /**
     * Returns a {@code LateBindingEnumerable} of elements of the given type.
     *
     * @param <E> type of enumerated elements.
     * @param clazz class of enumerated elements.
     * @return {@link LateBindingEnumerable} that can be bound to elements
     * of type {@code clazz}.
     */
    public static <E> LateBindingEnumerable<E> ofLateBinding(Class<E> clazz) {
        return new LateBindingEnumerable<E>();
    }

    /**
     * Converts the current enumerable to another type.
     *
     * @param <T> new type of enumerated elements.
     * @param clazz new class of enumerated elements.
     * @return {@link Enumerable} of another type.
     */
    public default <T> Enumerable<T> as(Class<T> clazz) {
        return (Enumerable<T>)this;
    }

    /**
     * Converts and filters the current enumerable to another type.
     * <p>
     * The new enumerable contains only elements of the given type.
     * </p>
     *
     * @param <T> new type of enumerated and filtered elements.
     * @param clazz new class of enumerated and filtered elements.
     * @return filtered {@link Enumerable} of another type.
     */
    public default <T> Enumerable<T> asFiltered(Class<T> clazz) {
        return filter(clazz::isInstance).as(clazz);
    }

    /**
     * Returns an {@code Enumeration} enumerating the elements of the current
     * {@link Enumerable}.
     *
     * @return {@link Enumeration} instance.
     */
    public default Enumeration<E> asEnumeration() {
        return enumerator().asEnumeration();
    }

    /**
     * Returns a {@code Spliterator} iterating the elements of the current
     * {@code Enumerable}.
     *
     * @return {@link Spliterator} instance.
     */
    public default Spliterator<E> asSpliterator() {
        return enumerator().asSpliterator();
    }

    /**
     * Returns a {@code Stream} streaming the elements of the current
     * {@code Enumerable}.
     *
     * @return {@link Stream} instance.
     */
    public default Stream<E> asStream() {
        return enumerator().asStream();
    }

    /**
     * Returns a tolerant {@code Enumerable} with {@code 0} retries enumerating
     * over the elements of the current {@code Enumerable}.
     *
     * @param handler error handler.
     * @return tolerant {@link Enumerable}.
     */
    public default Enumerable<E> asTolerant(
            Consumer<? super Exception> handler) {
        return asTolerant(handler, 0);
    }

    /**
     * Returns a tolerant {@code Enumerable} enumerating over the elements
     * of the current {@code Enumerable}.
     *
     * @param handler error handler.
     * @param retries positive number of retries on error.
     * @return tolerant {@link Enumerable}.
     */
    public default Enumerable<E> asTolerant(
            Consumer<? super Exception> handler,
            int retries) {
        return new SuppliedEnumerable(
                () -> enumerator().asTolerant(handler, retries));
    }

    // ---------------------------------------------------------------------- //

    /**
     * Appends the given elements to the end of the current {@code Enumerable}.
     *
     * @param elements succession of elements to append.
     * @return appended {@link Enumerable}.
     */
    public default Enumerable<E> append(E... elements) {
        return concat(on(elements));
    }

    /**
     * Returns a {@code CachedEnumerable} that caches the enumerated elements.
     *
     * @return {@link CachedEnumerable} instance.
     * @see #cached(long, java.util.function.Consumer)
     */
    public default CachedEnumerable<E> cached() {
        return new CachedEnumerable(this);
    }

    /**
     * Returns a {@code CachedEnumerable} that caches the enumerated elements
     * up to a given {@code limit}.
     * <p>
     * Enumeration does not stop at the given {@code limit} but the overflow
     * callback may call {@link CachedEnumerable} methods to cancel caching
     * on overflowing the limit.
     * </p>
     *
     * @param limit cache limit.
     * @param onLimitCallback handler called on surpassing the limit.
     * @return {@link CachedEnumerable} instance.
     * @see #cached()
     */
    public default CachedEnumerable<E> cached(
            long limit,
            Consumer<CachedEnumerable<E>> onLimitCallback) {
        return new CachedEnumerable(this, limit, onLimitCallback);
    }

    /**
     * Returns an {@code Enumerable} that chooses its elements from other
     * {@code Iterable} instances.
     *
     * @param <E> type of enumerated elements.
     * @param indexSupplier {@link Supplier} of {@link IntSupplier}. The
     * supplied index indicates the source providing the next enumerated
     * element.
     * @param first first {@link Iterable} source.
     * @param second second {@code Iterable} source.
     * @param rest rest of {@code Iterable} sources.
     * @return {@link Enumerable} that chooses its elements from other
     * {@code Iterable} instances.
     */
    public static <E> Enumerable<E> choiceOf(
            Supplier<IntSupplier> indexSupplier,
            Iterable<E> first,
            Iterable<? extends E> second,
            Iterable<? extends E>... rest) {
        final int size = 1 + 1 + rest.length;
        final IntUnaryOperator altIndexSupplier = i -> (i+1)%size;
        return choiceOf(indexSupplier,
                        () -> altIndexSupplier,
                        first,
                        second,
                        rest);
    }

    /**
     * Returns an {@code Enumerable} that chooses its elements from other
     * {@code Iterable} instances.
     *
     * @param <E> Type of enumerated elements.
     * @param indexSupplier {@link Supplier} of {@link IntSupplier}. The
     * supplied index indicates the source of the next enumerated element.
     * @param altIndexSupplier {@code Supplier} of {@code IntSupplier}. The
     * supplied index indicates the source of the next enumerated element if
     * the {@link Iterable} indicated by {@code indexSupplier} runs out of
     * elements.
     * @param first first {@code Iterable} source.
     * @param second second {@code Iterable} source.
     * @param rest rest of {@code Iterable} sources.
     * @return {@link Enumerable} that chooses its elements from other
     * {@code Iterable} instances.
     */
    public static <E> Enumerable<E> choiceOf(
            Supplier<IntSupplier> indexSupplier,
            Supplier<IntUnaryOperator> altIndexSupplier,
            Iterable<E> first,
            Iterable<? extends E> second,
            Iterable<? extends E>... rest) {
        return new ChoiceEnumerable(indexSupplier,
                                    altIndexSupplier,
                                    first,
                                    second,
                                    Arrays.asList(rest));
    }

    /**
     * Concatenates the given {@code Iterable} to the current
     * {@code Enumerable}.
     *
     * @param elements {@link Iterable} to concatenate.
     * @return concatenated {@link Enumerable}.
     */
    public default Enumerable<E> concat(Iterable<? extends E> elements) {
        return PipeEnumerable.concat(this, elements);
    }

    /**
     * Concatenates the given elements to the current {@code Enumerable}.
     *
     * @param elements elements to concatenate.
     * @return concatenated {@link Enumerable}.
     */
    public default Enumerable<E> concatOn(E... elements) {
        return concat(on(elements));
    }

    /**
     * Returns an {@code Enumerable} enumerating over the distinct elements
     * of the current {@code Enumerable}.
     *
     * @return distinct {@link Enumerable}.
     */
    public default Enumerable<E> distinct() {
        Checks.ensureNonEnumerating(this);
        return PipeEnumerable.distinct(this);
    }

    /**
     * Checks whether the current {@code Enumerable} matches element-wise
     * the given {@code Iterable}.
     *
     * @param <T> type of elements to match against.
     * @param elements {@link Iterable} to match against.
     * @return true if the elements match, false otherwise.
     */
    public default <T> boolean elementsEqual(Iterable<T> elements) {
        return enumerator().elementsEqual(elements.iterator());
    }

    /**
     * Returns an {@code Enumerable} with no elements.
     *
     * @param <E> Type of enumerated elements.
     * @return empty {@link Enumerable}.
     */
    public static <E> Enumerable<E> empty() {
        return of(Collections.emptyList());
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements
     * satisfying the given {@code Predicate}.
     *
     * @param predicate predicate to filter by.
     * @return filtered {@link Enumerable}.
     */
    public default Enumerable<E> filter(Predicate<? super E> predicate) {
        return PipeEnumerable.filter(this, predicate);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the iterables produced
     * by applying the given mapper over the elements of the current
     * {@code Enumerable}.
     *
     * @param <R> type of resulted elements.
     * @param mapper {@link Function} instance mapping the current elements
     * to iterables.
     * @return flat-mapped {@link Enumerable}.
     */
    public default <R> Enumerable<R> flatMap(
            Function<? super E, ? extends Iterable<? extends R>> mapper) {
        return PipeEnumerable.flatMap(this, mapper);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the elements generated by
     * repeatedly applying the given unary operator on the given seed.
     *
     * @param <E> type of enumerated elements.
     * @param seed seed element.
     * @param f unary operator.
     * @return iterated {@link Enumerable}.
     */
    public static <E> Enumerable<E> iterate(E seed, UnaryOperator<E> f) {
        return new SuppliedEnumerable(() -> Enumerator.iterate(seed, f));
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements up to
     * the given limit.
     *
     * @param maxSize limit to iterate up to.
     * @return limited {@link Enumerable}.
     */
    public default Enumerable<E> limit(long maxSize) {
        return PipeEnumerable.limit(this, maxSize);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements while
     * the given predicate holds true.
     *
     * @param predicate condition to continue enumeration. 
     * @return limited {@link Enumerable}.
     */
    public default Enumerable<E> limitWhile(Predicate<? super E> predicate) {
        return PipeEnumerable.limitWhile(this, predicate);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements
     * mapped by the given mapper.
     *
     * @param <R> type of resulted elements.
     * @param mapper {@link Function} instance mapping the current elements.
     * @return mapped {@link Enumerable}.
     */
    public default <R> Enumerable<R> map(
            Function<? super E, ? extends R> mapper) {
        return PipeEnumerable.map(this, mapper);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current indexed
     * elements mapped by the given mapper.
     *
     * @param <R> type of resulted elements.
     * @param mapper {@link BiFunction} instance mapping the current indexed
     * elements.
     * @return  mapped {@link Enumerable}.
     */
    public default <R> Enumerable<R> map(
            BiFunction<? super E, ? super Long, ? extends R> mapper) {
        return PipeEnumerable.map(this, mapper);
    }

    /**
     * Returns an {@code Enumerable} enumerating the current elements while
     * feeding them to the given {@code Consumer}.
     *
     * @param action {@link Consumer} instance to apply upon each element.
     * @return peeked {@link Enumerable}.
     */
    public default Enumerable<E> peek(Consumer<? super E> action) {
        return PipeEnumerable.peek(this, action);
    }

    /**
     * Prepends the current {@code Enumerable} by the given {@code Iterable}.
     *
     * @param elements {@link Iterable} to prepend.
     * @return prepended {@link Enumerable}.
     */
    public default Enumerable<E> prepend(Iterable<? extends E> elements) {
        Checks.ensureNonEnumerating(this);
        return of((Iterable<E>)elements).concat(this);
    }

    /**
     * Prepends the current {@code Enumerable} by the given elements.
     *
     * @param elements elements to prepend.
     * @return prepended {@link Enumerable}.
     */
    public default Enumerable<E> prependOn(E... elements) {
        return prepend(on(elements));
    }

    /**
     * Returns an {@code Enumerable} enumerating over a range of elements.
     *
     * @param <E> type of enumerated elements.
     * @param startInclusive inclusive start of range.
     * @param endExclusive exclusive end of range.
     * @param succ successor unary operator.
     * @param cmp comparison binary operator.
     * @return range {@link Enumerable}.
     */
    public static <E> Enumerable<E> range(E startInclusive,
                                          E endExclusive,
                                          UnaryOperator<E> succ,
                                          Comparator<? super E> cmp) {
        Checks.ensureNotNull(succ, Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(cmp, Messages.NULL_ENUMERATOR_COMPARATOR);
        return new SuppliedEnumerable(() ->
                Enumerator.range(startInclusive,
                                 endExclusive,
                                 succ,
                                 cmp));
    }

    /**
     * Returns an {@code Enumerable} enumerating over a range of elements.
     *
     * @param <E> type of enumerated elements.
     * @param startInclusive inclusive start of range.
     * @param endInclusive inclusive end of range.
     * @param succ successor unary operator.
     * @param cmp comparison binary operator.
     * @return range {@link Enumerable}.
     */
    public static <E> Enumerable<E> rangeClosed(E startInclusive,
                                                E endInclusive,
                                                UnaryOperator<E> succ,
                                                Comparator<? super E> cmp) {
        Checks.ensureNotNull(succ, Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(cmp, Messages.NULL_ENUMERATOR_COMPARATOR);
        return new SuppliedEnumerable(() ->
                Enumerator.rangeClosed(startInclusive,
                                       endInclusive,
                                       succ,
                                       cmp));
    }

    /**
     * Returns an {@code Enumerable} enumerating over a range of integers.
     *
     * @param startInclusive inclusive start of range.
     * @param endExclusive exclusive end of range.
     * @return range {@link Enumerable}.
     */
    public static Enumerable<Integer> rangeInt(int startInclusive,
                                               int endExclusive) {
        return new SuppliedEnumerable(() ->
                Enumerator.rangeInt(startInclusive, endExclusive));
    }

    /**
     * Returns an {@code Enumerable} enumerating over a range of integers.
     *
     * @param startInclusive inclusive start of range.
     * @param endInclusive inclusive end of range.
     * @return range {@link Enumerable}.
     */
    public static Enumerable<Integer> rangeIntClosed(int startInclusive,
                                                     int endInclusive) {
        return new SuppliedEnumerable(() ->
                Enumerator.rangeIntClosed(startInclusive, endInclusive));
    }

    /**
     * Returns an {@code Enumerable} enumerating over a range of long integers.
     *
     * @param startInclusive inclusive start of range.
     * @param endExclusive exclusive end of range.
     * @return range {@link Enumerable}.
     */
    public static Enumerable<Long> rangeLong(long startInclusive,
                                             long endExclusive) {
        return new SuppliedEnumerable(() ->
                Enumerator.rangeLong(startInclusive, endExclusive));
    }

    /**
     * Returns an {@code Enumerable} enumerating over a range of long integers.
     *
     * @param startInclusive inclusive start of range.
     * @param endInclusive inclusive end of range.
     * @return range {@link Enumerable}.
     */
    public static Enumerable<Long> rangeLongClosed(long startInclusive,
                                                   long endInclusive) {
        return new SuppliedEnumerable(() ->
                Enumerator.rangeLongClosed(startInclusive, endInclusive));
    }

    /**
     * Returns an {@code Enumerable} repeating the given element the given
     * number of time.
     *
     * @param <E> type of enumerated elements.
     * @param element element to repeat.
     * @param count number of times to repeat.
     * @return repeated {@link Enumerable}.
     */
    public static <E> Enumerable<E> repeat(E element, long count) {
        return new SuppliedEnumerable(() -> Enumerator.repeat(element, count));
    }

    /**
     * Returns an {@code Enumerable} repeating all the current elements the
     * given number of times.
     *
     * @param count number of times to repeat.
     * @return repeated {@link Enumerable}.
     */
    public default Enumerable<E> repeatAll(int count) {
        return rangeInt(0, count)
                .map(i -> new OnceEnumerable(enumerator()))
                .flatMap(e -> e);
    }

    /**
     * Returns an {@code Enumerable} repeating each current element the given
     * number of times.
     *
     * @param count number of times to repeat.
     * @return repeated {@link Enumerable}.
     */
    public default Enumerable<E> repeatEach(long count) {
        Checks.ensureNonNegative(count, Messages.NEGATIVE_ENUMERATOR_SIZE);
        return flatMap(e -> repeat(e, count));
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements in
     * reversed order.
     *
     * @return reversed {@link Enumerable}.
     */
    public default Enumerable<E> reverse() {
        return new SuppliedEnumerable(() -> enumerator().reverse());
    }

    /**
     * Returns an {@code Enumerable} that skips the given number of current
     * elements.
     *
     * @param n number of elements to skip.
     * @return skipped {@link Enumerable}.
     */
    public default Enumerable<E> skip(long n) {
        return PipeEnumerable.skip(this, n);
    }

    /**
     * Returns an {@code Enumerable} that skips while the given condition
     * holds true.
     *
     * @param predicate skipping condition.
     * @return skipped {@link Enumerable}.
     */
    public default Enumerable<E> skipWhile(Predicate<? super E> predicate) {
        return PipeEnumerable.skipWhile(this, predicate);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements
     * in sorted order.
     *
     * @return sorted {@link Enumerable}.
     */
    public default Enumerable<E> sorted() {
        return new SuppliedEnumerable(() -> enumerator().sorted());
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements
     * in sorted order according to the given comparator.
     *
     * @param comparator sorting criterion.
     * @return sorted {@link Enumerable}.
     */
    public default Enumerable<E> sorted(Comparator<? super E> comparator) {
        return new SuppliedEnumerable(() -> enumerator().sorted(comparator));
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements up to
     * the given limit.
     *
     * @param n limit to iterate up to.
     * @return limited {@link Enumerable}.
     */
    public default Enumerable<E> take(long n) {
        return limit(n);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements while
     * the given predicate holds true.
     *
     * @param predicate condition to continue enumeration. 
     * @return limited {@link Enumerable}.
     */
    public default Enumerable<E> takeWhile(Predicate<? super E> predicate) {
        return PipeEnumerable.takeWhile(this, predicate);
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements and
     * over the given {@code Iterable} while avoiding duplicates.
     *
     * @param others {@link Iterable} to perform union with.
     * @return union {@link Enumerable}.
     */
    public default Enumerable<E> union(Iterable<E> others) {
        return concat(others).distinct();
    }

    /**
     * Returns an {@code Enumerable} enumerating over the current elements and
     * over the given elements while avoiding duplicates.
     *
     * @param others elements to perform union with.
     * @return union {@link Enumerable}.
     */
    public default Enumerable<E> unionOn(E... others) {
        return union(on(others));
    }

    /**
     * Returns an {@code Enumerable} consisting of pairs of elements from the
     * current enumerator and the given {@code Iterable}, while any
     * of them has elements.
     * <p>
     * The resulted enumerator stops when both the current enumerable and the
     * provided {@link Iterable} stop. The first element of a returned
     * pair is empty {@link Optional} if the current enumerator has no
     * elements. Likewise, the second element of a returned pair is empty
     * {@link Optional} if the provided {@link Iterable} has no elements.
     * </p>
     *
     * @param <T> the of elements to zip with.
     * @param elements {@link Iterable} to zip with.
     * @return zipped {@link Enumerable}.
     */
    public default <T>
                   Enumerable<Pair<Optional<E>, Optional<T>>>
                   zipAny(Iterable<T> elements) {
        return zipAll((Iterable<E>)elements)
               .map(arr -> Pair.of(arr[0], (Optional<T>)arr[1]));
    }

    /**
     * Returns an {@code Enumerable} consisting of pairs of elements from the
     * current enumerator and the given {@code Iterable}, while both
     * have elements.
     * <p>
     * The resulted enumerator stops when aby of the current enumerator or the
     * provided {@link Iterable} stop.
     * </p>
     *
     * @param <T> the of elements to zip with.
     * @param elements {@link Iterable} to zip with.
     * @return zipped {@link Enumerable}.
     */
    public default <T>
                   Enumerable<Pair<E, T>>
                   zipBoth(Enumerable<T> elements) {
        return zipAll((Iterable<E>)elements)
               .takeWhile(arr -> arr[0].isPresent() && arr[1].isPresent())
               .map(arr -> Pair.of(arr[0].get(), ((Optional<T>)arr[1]).get()));
    }

    /**
     * Returns an {@code Enumerable} consisting of pairs of elements from the
     * current enumerator and the given {@code Iterable}, while the current
     * enumerator has elements.
     * <p>
     * The resulted enumerator stops when the current enumerator stops.
     * The second element of a returned pair is empty {@link Optional} if
     * the provided {@link Iterable} has no elements.
     * </p>
     *
     * @param <T> the of elements to zip with
     * @param elements {@code Iterable} to zip with
     * @return zipped {@link Enumerable}
     */
    public default <T>
                   Enumerable<Pair<E, Optional<T>>>
                   zipLeft(Iterable<T> elements) {
        return zipAll((Iterable<E>)elements)
               .takeWhile(arr -> arr[0].isPresent())
               .map(arr -> Pair.of(arr[0].get(), (Optional<T>)arr[1]));
    }

    /**
     * Returns an {@code Enumerable} consisting of pairs of elements from the
     * current enumerator and the given {@code Iterable}, while the provided
     * iterator has elements.
     * <p>
     * The resulted enumerator stops when the provided {@link Iterable} stops.
     * The first element of a returned pair is empty {@link Optional} if the
     * current enumerator has no elements.
     * </p>
     *
     * @param <T> the of elements to zip with
     * @param elements {@code Iterable} to zip with
     * @return zipped {@link Enumerable}
     */
    public default <T>
                   Enumerable<Pair<Optional<E>, T>>
                   zipRight(Iterable<T> elements) {
        Checks.ensureNonEnumerating(this);
        return zipAll((Iterable<E>)elements)
               .takeWhile(arr -> arr[1].isPresent())
               .map(arr -> Pair.of(arr[0], ((Optional<T>)arr[1]).get()));
    }

    /**
     * Returns an {@code Enumerable} consisting of an array of {@code Optional}
     * objects containing elements from the current enumerator and
     * the given {@code Iterable} instances, while any has elements.
     *
     * @param first first iterable to zip with.
     * @param rest the rest of iterables to zip with.
     * @return zipped {@link Enumerable}.
     */
    public default Enumerable<Optional<E>[]>
                   zipAll(Iterable<? extends E> first,
                          Iterable<? extends E>... rest) {
        return PipeEnumerable.zipAll(this, first, rest);
    }
}
