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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
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
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * {@code Iterator} with high composability.
 * <p>
 * Enumerators are {@link Iterator} objects with the following characteristics:
 * </p>
 * <ul>
 *   <li><em>High composability</em>: some operations can be composed
 * iteratively</li>
 *   <li><em>Shareability</em>: shareable enumerators may participate in
 * multiple pipelines, processed independently</li>
 *   <li><em>Fault tolerance</em>: fault-tolerant enumerators allow for
 * exceptions in the middle of the pipeline without stopping the whole
 * computation
 *   <li><em>Lazy evaluations</em>: enumerators allow for lazy evaluation in
 * both returned values and input sequences
 *   <li><em>Choice composition and zipping</em>: construction of enumerators
 * from multiple sub-sequences by choosing or zipping
 *   <li><em>Compatibility and fluent syntax</em>: enumerators support fluent
 * syntax like {@link Stream} classes and are compatible with most major
 * collection and sequence constructions: {@link Iterator}, {@link Iterable},
 * {@link Enumeration}, {@link Stream}, {@link Spliterator} and {@link Supplier}
 * of elements.
 * </ul>
 * <p>
 * <strong>High composability</strong>
 * </p>
 * <p>
 * The following operations can be composed iteratively many times and the
 * resulting enumerator will not overflow the stack when enumerating:
 * </p>
 * <ul>
 *   <li>{@link #as(java.lang.Class)}</li>
 *   <li>{@link #asFiltered(java.lang.Class)}</li>
 *   <li>{@link #asOptional()}</li>
 *   <li>{@link #append(java.lang.Object...)}</li>
 *   <li>{@link #concatOn(java.lang.Object...)}</li>
 *   <li>{@link #concat(java.util.Enumeration)}</li>
 *   <li>{@link #concat(java.lang.Iterable)}</li>
 *   <li>{@link #concat(java.util.Iterator)}</li>
 *   <li>{@link #concat(java.util.Spliterator)}</li>
 *   <li>{@link #concat(java.util.stream.Stream)}</li>
 *   <li>{@link #concat(java.util.function.Supplier)}</li>
 *   <li>{@link #filter(java.util.function.Predicate)}</li>
 *   <li>{@link #flatMap(java.util.function.Function)}</li>
 *   <li>{@link #limit(long)}</li>
 *   <li>{@link #limitWhile(java.util.function.Predicate)}</li>
 *   <li>{@link #map(java.util.function.Function)}</li>
 *   <li>{@link #map(java.util.function.BiFunction)}</li>
 *   <li>{@link #peek(java.util.function.Consumer)}</li>
 *   <li>{@link #prependOn(java.lang.Object...)}</li>
 *   <li>{@link #prepend(java.util.Enumeration)}</li>
 *   <li>{@link #prepend(java.lang.Iterable)}</li>
 *   <li>{@link #prepend(java.util.Iterator)}</li>
 *   <li>{@link #prepend(java.util.Spliterator)}</li>
 *   <li>{@link #prepend(java.util.stream.Stream)}</li>
 *   <li>{@link #prepend(java.util.function.Supplier)}</li>
 *   <li>{@link #repeatEach(int)}</li>
 *   <li>{@link #skip(long)}</li>
 *   <li>{@link #skipWhile(java.util.function.Predicate)}</li>
 *   <li>{@link #take(long)}</li>
 *   <li>{@link #takeWhile(java.util.function.Predicate)}</li>
 *   <li>{@link #zipAll(java.util.Iterator, java.util.Iterator...)}</li>
 *   <li>{@link #zipAny(java.util.Iterator)}</li>
 *   <li>{@link #zipBoth(java.util.Iterator)}</li>
 *   <li>{@link #zipLeft(java.util.Iterator)}</li>
 *   <li>{@link #zipRight(java.util.Iterator)}</li>
 * </ul>
 * <p>
 * <strong>Shareability</strong>
 * </p>
 * <p>
 * {@link ShareableEnumerator} can spawn instances
 * of {@link SharingEnumerator} which in turn can share the same sequence
 * without traversing it more than once. {@link #asShareable()} converts any
 * enumerator into a {@link ShareableEnumerator}.
 * </p>
 * <p>
 * <strong>Fault tolerance</strong>
 * </p>
 * <p>
 * Fault-tolerant enumerators accept an error handler which
 * is being called whenever the tryPipelineOut of enumerating throws an
 * exception. The error handler consumes the error and the pipeline doesn't
 * stop. {@link #asTolerant(java.util.function.Consumer)} converts any
 * enumerator into a fault-tolerant enumerator.
 * </p>
 * <p>
 * <strong>Lazy evaluations</strong>
 * </p>
 * <p>
 * The following calls allow lazy specification of input sequences:
 * </p>
 * <ul>
 *   <li>{@link #ofLazyEnumeration(java.util.function.Supplier)}</li>
 *   <li>{@link #ofLazyIterable(java.util.function.Supplier)}</li>
 *   <li>{@link #ofLazyIterator(java.util.function.Supplier)}</li>
 *   <li>{@link #ofLazySpliterator(java.util.function.Supplier)}</li>
 *   <li>{@link #ofLazyStream(java.util.function.Supplier)}</li>
 * </ul>
 * <p>
 * <strong>Choice composition and zipping</strong>
 * </p>
 * <p>
 * Enumerators can be constructed of sub-sequences by choosing elements
 * from them:
 * </p>
 * <ul>
 *   <li>{@link #choiceOf(java.util.function.IntSupplier,
 *                        java.util.Iterator,
 *                        java.util.Iterator,
 *                        java.util.Iterator...)}</li>
 *   <li>{@link #choiceOf(java.util.function.IntSupplier,
 *                        java.util.function.IntUnaryOperator,
 *                        java.util.Iterator,
 *                        java.util.Iterator,
 *                        java.util.Iterator...)}</li>
 * </ul>
 * <p>
 * Enumerators also have four zipping operations:
 * </p>
 * <ul>
 *   <li>{@link #zipAny(java.util.Iterator)}</li>
 *   <li>{@link #zipBoth(java.util.Iterator)}</li>
 *   <li>{@link #zipLeft(java.util.Iterator)}</li>
 *   <li>{@link #zipRight(java.util.Iterator)}</li>
 * </ul>
 * @param <E> type of enumerated elements.
 */
public interface Enumerator<E> extends Iterator<E> {

    /**
     * Returns whether the enumerator has started enumerating.
     * <p>
     * This method returns false before the first call
     * to {@link hasNext} and true afterwards.
     * </p>
     *
     * @return true if the enumerator has started enumerating, false otherwise.
     * @see #hasNext()
     * @see #next()
     */
    public boolean enumerating();

    /**
     * Returns an enumerator returning the given elements.
     *
     * @param <E> the type of elements being enumerated.
     * @param elements the elements returned by the enumerator.
     * @return the new {@link Enumerator}.
     */
    @SafeVarargs
    public static <E> Enumerator<E> on(E... elements) {
        return new ArrayEnumerator(elements);
    }

    /**
     * Returns an enumerator iterating over the given array.
     *
     * @param <E> type of enumerated elements.
     * @param elements array to iterate upon.
     * @return the new {@link Enumerator}.
     */
    public static <E> Enumerator<E> of(E[] elements) {
        return new ArrayEnumerator(elements);
    }

    /**
     * Returns an enumerator enumerating over an existing
     * {@code Iterator}.
     * <p>
     * If {@code source} is of type {@link Enumerator} then this method
     * returns {@code source}.
     * </p>
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@link Iterator} being enumerated upon.
     * @return the new enumerator.
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> of(Iterator<E> source) {
        return IteratorEnumerator.of(source);
    }

    /**
     * Returns an enumerator enumerating over the elements of an
     * existing {@code Iterable}.
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@link Iterable} being enumerated upon.
     * @return the new enumerator.
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> of(Iterable<E> source) {
        return of(source.iterator());
    }

    /**
     * Returns an enumerator enumerating over the elements of an
     * existing {@code Enumeration}.
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@link Enumeration} being enumerated upon
     * @return the new enumerator.
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> of(Enumeration<E> source) {
        return new EnumerationEnumerator(source);
    }

    /**
     * Returns an enumerator enumerating over the elements of an existing
     * {@code Stream}.
     * <p>
     * If {@link Stream#isParallel()} returns true on
     * {@code source} then the order of elements is undetermined.
     * </p>
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@link Stream} being enumerated upon.
     * @return the new enumerator.
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> of(Stream<E> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return of(source.iterator());
    }

    /**
     * Returns an enumerator enumerating over the elements of an existing
     * {@code Spliterator}.
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@link Spliterator} being enumerated upon.
     * @return the new enumerator.
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> of(Spliterator<E> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return of(Spliterators.iterator(source));
    }

    /**
     * Returns an enumerator enumerating over the elements supplied by an
     * existing {@code Supplier}.
     * <p>
     * The enumerator extracts and returns elements from <code>source</code>
     * up to the first empty {@link Optional} value.
     * </p>
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@link Supplier} of elements being enumerated.
     * Enumeration continues up to the first empty {@link Optional}.
     * @return the new enumerator.
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> of(Supplier<Optional<E>> source) {
        return new SuppliedEnumerator(source);
    }

    /**
     * Returns an enumerator enumerating over an {@link Iterator} supplied
     * lazily.
     * <p>
     * This method works like {@link #of(java.util.Iterator)} with the
     * distinction that <code>source</code> is supplied by a {@link Supplier}.
     * </p>
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@link Supplier} supplying the {@link Iterator} to
     * enumerate upon.
     * @return the new enumerator.
     * @see #of(java.util.Iterator)
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> ofLazyIterator(
            Supplier<? extends Iterator<E>> source) {
        return LazyEnumerator.of(source);
    }

    /**
     * Returns an enumerator enumerating over an {@code Iterable} supplied
     * lazily.
     * <p>
     * This method works like {@link #of(java.lang.Iterable)} with the
     * distinction that <code>source</code> is supplied by a {@link Supplier}.
     * </p>
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@code Supplier} supplying the {@link Iterable} to
     * enumerate upon.
     * @return the new enumerator.
     * @see #of(java.lang.Iterable)
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> ofLazyIterable(
            Supplier<? extends Iterable<E>> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return LazyEnumerator.of(() -> of(source.get()));
    }

    /**
     * Returns an enumerator enumerating over an {@code Enumeration} supplied
     * lazily.
     * <p>
     * This method works like {@link #of(java.util.Enumeration)} with the
     * distinction that {@code source} is supplied by a {@link Supplier}.
     * </p>
     *
     * @param <E> the type of elements being enumerated
     * @param source the {@code Supplier} supplying the {@link Enumeration} to
     * enumerate upon
     * @return the new enumerator
     * @see #of(java.util.Enumeration)
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> ofLazyEnumeration(
            Supplier<? extends Enumeration<E>> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return LazyEnumerator.of(() -> of(source.get()));
    }

    /**
     * Returns an enumerator enumerating over a {@code Stream} supplied lazily.
     * <p>
     * This method works like {@link #of(java.util.stream.Stream)} with the
     * distinction that {@code source} is supplied by a {@link Supplier}.
     * </p>
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@code Supplier} supplying the {@link Stream} to
     * enumerate upon.
     * @return the new enumerator.
     * @see #of(java.util.stream.Stream)
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> ofLazyStream(
            Supplier<? extends Stream<E>> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return LazyEnumerator.of(() -> of(source.get()));
    }

    /**
     * Returns an enumerator enumerating over a {@code Spliterator} supplied
     * lazily.
     * <p>
     * This method works like {@link #of(java.util.Spliterator)} with the
     * distinction that {@code source} is supplied by a {@link Supplier}.
     * </p>
     *
     * @param <E> the type of elements being enumerated.
     * @param source the {@code Supplier} supplying the {@link Spliterator} to
     * enumerate upon.
     * @return the new enumerator.
     * @see #of(java.util.Spliterator)
     * @exception IllegalArgumentException {@code source} is null.
     */
    public static <E> Enumerator<E> ofLazySpliterator(
            Supplier<? extends Spliterator<E>> source) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return LazyEnumerator.of(() -> of(source.get()));
    }

    /**
     * Returns an enumerator that supports late-binding.
     *
     * @param <E> type of enumerated elements.
     * @param clazz class of enumerated elements.
     * @return the new enumerator.
     * @see LateBindingEnumerator
     */
    public static <E> LateBindingEnumerator<E> ofLateBinding(Class<E> clazz) {
        return new LateBindingEnumerator<E>();
    }

    // ---------------------------------------------------------------------- //

    /**
     * Casts the current enumerator to an enumerator of a different parameter
     * type.
     * <p>
     * Casting takes place upon the enumerator only. Element casting is
     * considered implicit.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param <T> type of elements of the resulted enumerator.
     * @param clazz class of the type to cast to.
     * @return the type-casted enumerator.
     * @see #asFiltered(java.lang.Class)
     */
    public default <T> Enumerator<T> as(Class<T> clazz) {
        Checks.ensureNonEnumerating(this);
        return (Enumerator<T>)this;
    }

    /**
     * Casts the current enumerator to a different parameter type and filters
     * the enumerated elements based on that type.
     * <p>
     * This method works exactly like
     * {@code this.filter(e -> clazz.isInstance(e)).as(clazz)}.
     * </p>
     *
     * @param <T> type of elements of the resulted enumerator.
     * @param clazz class of the type to cast and filter to.
     * @return the resulted enumerator.
     * @see #as(java.lang.Class)
     * @see #filter(java.util.function.Predicate)
     */
    public default <T> Enumerator<T> asFiltered(Class<T> clazz) {
        Checks.ensureNotNull(clazz, Messages.NULL_ENUMERATOR_CLASS);
        return filter(clazz::isInstance).as(clazz);
    }

    /**
     * Returns a {@code OnceEnumerable} instance enumerating over the current
     * enumerator.
     * <p>
     * This method satisfies the condition
     * <code>this.asEnumerable().iterator() == this</code>.
     * </p>
     *
     * @return the new {@link OnceEnumerable}.
     */
    public default OnceEnumerable<E> asEnumerable() {
        Checks.ensureNonEnumerating(this);
        return new OnceEnumerable<E>(this);
    }

    /**
     * Returns an {@code Enumeration} enumerating over the current enumerator.
     *
     * @return the new {@link Enumeration}.
     * @see #asEnumerable()
     */
    public default Enumeration<E> asEnumeration() {
        Checks.ensureNonEnumerating(this);
        return new EnumerableEnumeration(this);
    }

    /**
     * Returns an infinite enumerator of non-empty {@code Optional} instances
     * containing the current elements as long as they last or empty
     * {@code Optional} instances if the current enumerator has no more
     * elements.
     * <p>
     * The current enumerator may not return null elements.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     * @return the infinite enumerator of {@link Optional} instances.
     */
    public default Enumerator<Optional<E>> asOptional() {
        return Enumerator.this.map(e -> Optional.of(e))
                .concat(Enumerator.of(() -> Optional.of(Optional.empty())));
    }

    /**
     * Returns a sequential {@code Spliterator} iterating over the current
     * enumerator.
     *
     * @return the new {@link Spliterator}.
     * @see #asStream()
     */
    public default Spliterator<E> asSpliterator() {
        Checks.ensureNonEnumerating(this);
        return Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED);
    }

    /**
     * Returns a sequential {@code Stream} streaming over the current
     * enumerator.
     *
     * @return the new {@link Stream}.
     * @see #asSpliterator()
     * @see #asSupplier()
     */
    public default Stream<E> asStream() {
        Checks.ensureNonEnumerating(this);
        return StreamSupport.stream(asSpliterator(), false);
    }

    /**
     * Returns a {@code Supplier} supplying the elements of the current
     * enumerator.
     * <p>
     * The resulted {@link Supplier} supplies non-empty {@link Optional}
     * instances as long as the current enumerator has elements. The current
     * enumerator may not yield null elements.
     * </p>
     *
     * @return the new {@code Supplier}.
     * @see #asStream()
     */
    public default Supplier<Optional<E>> asSupplier() {
        Checks.ensureNonEnumerating(this);
        return () -> hasNext() ? Optional.of(next()) : Optional.empty();
    }

    /**
     * Returns a {@code ShareableEnumerator} sharing the elements of the
     * current enumerator.
     * <p>
     * A {@link ShareableEnumerator} can either enumerate the elements of the
     * current enumerator itself or it can produce a collection of
     * {@link Enumerator} instances that will share the elements of the
     * current iterator when enumerating independently of each other. The two
     * modes are mutually exclusive.
     * </p>
     * @return the new {@link ShareableEnumerator}.
     * @see SharingEnumerator
     */
    public default ShareableEnumerator<E> asShareable() {
        Checks.ensureNonEnumerating(this);
        return new ShareableEnumerator<E>(this);
    }

    /**
     * Returns a fault-tolerant enumerator with no retries. The resulted
     * enumerator has the following characteristics:
     * <ul>
     *   <li>any exception thrown by {@link #next()} gets handled by
     * calling <code>handler.accept(? super Exception)</code> and it doesn't
     * stop enumeration</li>
     *   <li>any exception thrown by {@link #hasNext()} gets handled by
     * calling <code>handler.accept(? super Exception)</code> and it stops
     * enumeration</li>
     *   <li>any exception thrown by
     * <code>handler.accept(? super Exception)</code> stops enumeration and
     * does not re-throw</li>
     * </ul>
     *
     * @param handler {@link Consumer} handling {@link Exception} instances
     * thrown by {@code hasNext()} or {@code next()}.
     * @return the new fault-tolerant enumerator.
     * @exception IllegalArgumentException {@code handler} is null.
     * @see #asTolerant(java.util.function.Consumer, int)
     */
    public default Enumerator<E> asTolerant(
            Consumer<? super Exception> handler) {
        return new TolerantEnumerator(this, handler, 0);
    }

    /**
     * Returns a fault-tolerant enumerator with retries. The resulted
     * enumerator has the following characteristics:
     * <ul>
     *   <li>any exception thrown by {@link #next()} gets handled by
     * calling <code>handler.accept(? super Exception)</code> and it doesn't
     * stop enumeration</li>
     *   <li>any exception thrown by {@link #hasNext()} gets handled by
     * calling <code>handler.accept(? super Exception)</code> and it stops
     * enumeration after a number of retries.</li>
     *   <li>any exception thrown by
     * <code>handler.accept(? super Exception)</code> stops enumeration and
     * does not re-throw</li>
     * </ul>
     *
     * @param handler {@link Consumer} handling {@link Exception} instances
     * thrown by {@code hasNext()} or {@code next()}.
     * @param retries number of retries to {@code hasNext()} before returning
     * false if {@code hasNext()} throws.
     * @return the new fault-tolerant enumerator.
     * @exception IllegalArgumentException {@code handler} is
     * null or {@code retries} is negative.
     * @see #asTolerant(java.util.function.Consumer)
     */
    public default Enumerator<E> asTolerant(
            Consumer<? super Exception> handler,
            int retries) {
        return new TolerantEnumerator(this, handler, retries);
    }

    // ---------------------------------------------------------------------- //

    /**
     * Returns whether all the elements of the current enumerator match the
     * provided predicate.
     *
     * @param predicate state-less predicate to apply to the enumerated
     * elements.
     * @return true if all the elements satisfy.
     * {@code predicate}, false otherwise
     * @see #anyMatch(java.util.function.Predicate)
     * @see #noneMatch(java.util.function.Predicate)
     * @exception IllegalArgumentException {@code predicate} is null.
     */
    public default boolean allMatch(Predicate<? super E> predicate) {
        Checks.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        while(hasNext()) {
            if (!predicate.test(next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether any elements of the current enumerator match the
     * provided predicate.
     *
     * @param predicate state-less predicate to apply to the enumerated
     * elements.
     * @return true if some elements satisfy
     * {@code predicate}, false otherwise.
     * @see #allMatch(java.util.function.Predicate)
     * @see #noneMatch(java.util.function.Predicate)
     * @exception IllegalArgumentException {@code predicate} is null.
     */
    public default boolean anyMatch(Predicate<? super E> predicate) {
        Checks.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        while(hasNext()) {
            if (predicate.test(next())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Appends the provided elements to the end of the current enumerator.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements values to append.
     * @return the appended enumerator.
     * @see #concatOn(java.lang.Object...)
     */
    public default Enumerator<E> append(E... elements) {
        return concat(on(elements));
    }

    /**
     * Returns an enumerator that chooses elements from the provided iterators.
     * <p>
     * This method works like
     * {@link #choiceOf(java.util.function.IntSupplier,
     *                  java.util.function.IntUnaryOperator,
     *                  java.util.Iterator,
     *                  java.util.Iterator,
     *                  java.util.Iterator...)}
     * with the distinction that {@code altIndexSupplier} is
     * {@code i -> (i+1)%(2+rest.length)}.
     * </p>
     * @param <E> the type of elements being enumerated.
     * @param indexSupplier {@link Supplier} instance supplying the index of
     * the iterator to get the next element from.
     * @param first the first iterator to choose elements from.
     * @param second the second iterator to choose elements from.
     * @param rest other iterators to choose elements from.
     * @return the new enumerator.
     * @see #choiceOf(java.util.function.IntSupplier,
     *                java.util.function.IntUnaryOperator,
     *                java.util.Iterator,
     *                java.util.Iterator, java.util.Iterator...)
     * @see #zipAny(java.util.Iterator)
     * @see #zipBoth(java.util.Iterator)
     * @see #zipLeft(java.util.Iterator)
     * @see #zipRight(java.util.Iterator)
     * @exception IllegalArgumentException any argument is null.
     */
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

    /**
     * Returns an enumerator that chooses elements from the provided iterators.
     * <p>
     * The pair formed of {@code indexSupplier} and
     * {@code altIndexSupplier} makes the choice algorithm. The resulted
     * enumerator chooses its next element in a multi-step tryPipelineOut:
     * </p>
     * <ul>
     *   <li>it calls {@code indexSupplier.get()} to get the index of a
     * provided iterator. That iterator will provide the next element.
     * Index {@code 0} means the first iterator, index {@code 1}
     * means the second iterator and so on.</li>
     *   <li>if the chosen iterator has no elements, the resulted enumerator
     * calls <code>altIndexSupplier.apply(index)</code> repeatedly until
     * reaching a provided iterator that has elements.</li>
     *   <li>it calls {@link #next()} on the chosen iterator and it returns the
     * element</li>
     * </ul>
     * <p>
     * The resulted enumerator will always stop when all the supplied iterators
     * have no elements. Continuing to extract elements requires that a
     * non-finished participant iterator can be found in
     * 2+<code>rest.length</code> steps or less. If such an iterator cannot
     * be found in the required number of steps, then the resulted enumerator
     * stops even though there may be elements left.
     * </p>
     * <p>To avoid early stopping it is necessary that
     * <code>altIndexSupplier</code> leads to a non-finished iterator in
     * timely manner. It is the responsibility of the caller to provide the
     * choice algorithm in such a way that no element is lost because the
     * resulted enumerator stops too early.
     * </p>
     * <pre>
     * Example:
     * <code>
     * Enumerator.choiceOf(() -&gt; 0,
     *                     i -&gt; (i+1)%4,
     *                     Enumerator.empty&lt;Integer&gt;(),
     *                     Enumerator.on(1, 2, 3),
     *                     Enumerator.on(4, 5),
     *                     Enumerator.on(6))
     * </code>
     * will produce the sequence 1, 2, 3, 4, 5 and 6.
     * </pre>
     * @param <E> type of elements being enumerated.
     * @param indexSupplier {@link Supplier} instance supplying the index of the
     * provided iterator to get the next element from.
     * @param altIndexSupplier {@link IntUnaryOperator} instance supplying the
     * index of the next provided iterator if the iterator chosen by
     * <code>indexSupplier</code> has no more elements.
     * @param first the first iterator to choose elements from.
     * @param second the second iterator to choose elements from.
     * @param rest other iterators to choose elements from.
     * @return the new enumerator.
     * @exception IllegalArgumentException any argument is null.
     */
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
                                    Arrays.asList(rest));
    }

    /**
     * Collects the elements of the current enumerator according to the
     * provided {@code Collector}.
     * <p>
     * This methods works exactly like
     * <code>this.asStream().collect(collector)</code>.
     * </p>
     * @param <R> the collected type.
     * @param <A> the type of the intermediate accumulator during collection.
     * @param collector {@link Collector} instance collecting the enumerated
     * elements.
     * @return the result of the collection.
     * @see #asStream()
     * @see Stream
     * @see Collector
     * @exception IllegalArgumentException {@code collector} is null.
     */
    public default <R, A> R collect(Collector<? super E, A, R> collector) {
        Checks.ensureNotNull(collector, Messages.NULL_ENUMERATOR_HANDLER);
        return asStream().collect(collector);
    }

    /**
     * Concatenates the current enumerator with the provided {@code Iterator}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Iterator} instance to concatenate with
     * @return the concatenated enumerator.
     * @exception IllegalArgumentException {@code elements} is null.
     */
    public default Enumerator<E> concat(Iterator<? extends E> elements) {
        return new PipeEnumerator(this).concat(elements);
    }

    /**
     * Concatenates the current enumerator with the provided {@code Iterable}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Iterable} instance to concatenate with.
     * @return the concatenated enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> concat(Iterable<? extends E> elements) {
        return concat(of(elements));
    }

    /**
     * Concatenates the current enumerator with the provided
     * {@code Enumeration}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Enumeration} instance to concatenate with
     * @return the concatenated enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> concat(Enumeration<? extends E> elements) {
        return concat(of(elements));
    }

    /**
     * Concatenates the current enumerator with the provided sequential
     * {@code Stream}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Stream} instance to concatenate with
     * @return the concatenated enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> concat(Stream<? extends E> elements) {
        return concat(of(elements));
    }

    /**
     * Concatenates the current enumerator with the provided sequential
     * {@code Spliterator}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Spliterator} instance to concatenate with
     * @return the concatenated enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> concat(Spliterator<? extends E> elements) {
        return concat(of(elements));
    }

    /**
     * Concatenates the current enumerator with the sequence formed of
     * the elements supplied by the provided {@code Supplier}.
     * <p>
     * The first value supplied by <code>elements</code> that is an empty
     * {@link Optional} marks the end of the sequence to concatenate to.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Supplier} instance supplying the elements to
     * concatenate to.
     * @return the concatenated enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> concat(Supplier<Optional<E>> elements) {
        return concat(of(elements));
    }

    /**
     * Concatenates the current enumerator with the sequence formed of the
     * provided elements
     * <p>
     * This method works exactly like {@link #append(java.lang.Object...)}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements the values to concatenate at the end of the current
     * enumerator.
     * @return the concatenated enumerator.
     */
    public default Enumerator<E> concatOn(E... elements) {
        return concat(on(elements));
    }

    /**
     * Returns whether the current enumerator has an element equalling the
     * provided value.
     *
     * @param element value to look for.
     * @return true if value equals an element in the current
     * enumerator, false otherwise.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default boolean contains(E element) {
        while (hasNext()) {
            if (Objects.equals(element, next())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of elements in the current enumerator.
     *
     * @return the number of enumerated elements.
     */
    public default long count() {
        long cnt = 0;
        while (hasNext()) {
            ++cnt;
            next();
        }
        return cnt;
    }

    /**
     * Returns an enumerator containing the elements of the current enumerator
     * in the same order by without duplicates.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @return the new enumerator.
     * @see #sorted()
     */
    public default Enumerator<E> distinct() {
        return Reversible.distinct(this, false);
    }

    /**
     * Returns the element at the provided index within the sequence
     * of enumerated elements.
     * <p>
     * An <code>index</code> equal to <code>0</code> means the first element.
     * This method returns an empty {@link Optional} if <code>index</code> is
     * larger than or equal to <code>this.count()</code>.
     * </p>
     *
     * @param index {@code 0}-based index for the element to return.
     * @return {@link Optional} instance containing the element at the
     * required <code>index</code>.
     * @exception IllegalArgumentException <code>index</code> is negative.
     */
    public default Optional<E> elementAt(long index) {
        Checks.ensureNonNegative(index, Messages.NEGATIVE_ENUMERATOR_INDEX);
        Enumerator<E> skipped = skip(index);
        return Optional.ofNullable(skipped.hasNext()
                                   ? skipped.next()
                                   : null);
    }

    /**
     * Returns whether the current enumerator and the provided iterator
     * have equal elements and in the same order.
     * <pre>
     * This method works exactly like:
     * <code>
     * this.zipAny(elements)
     *     .matchAll(p -&gt; p.getLeft().isPresent() == p.getRight().isPresent()
     *                    &amp;&amp; p.getLeft.isPresent()
     *                    &amp;&amp; Objects.equsls(p.getLeft().get(),
     *                                      p.getRight().get()))
     * </code>
     * </pre>
     *
     * @param <T> type of elements to match the enumerated elements against.
     * @param elements {@link Iterator} instance whose elements get matched
     * against the enumerated elements.
     * @return true if the enumerated elements match the
     * elements of {@code Iterator} in both value and position,
     * false otherwise
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default <T> boolean elementsEqual(Iterator<T> elements) {
        Checks.ensureNotNull(elements, Messages.NULL_ITERATOR);
        boolean thisHasNext;
        boolean elementsHasNext;
        while (true) {
            thisHasNext = hasNext();
            elementsHasNext = elements.hasNext();
            if (!thisHasNext || !elementsHasNext) {
                return thisHasNext == elementsHasNext;
            }
            final E thisElement = next();
            final T otherElement = elements.next();
            if (!Objects.equals(thisElement, otherElement)) {
                return false;
            }
        }
    }

    /**
     * Returns en enumerator with no elements.
     *
     * @param <E> type of the enumerated elements.
     * @return the empty enumerator.
     */
    public static <E> Enumerator<E> empty() {
        return of(Collections.emptyIterator());
    }

    /**
     * Returns an enumerator consisting of the elements of the current
     * enumerator that match the provided predicate.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param predicate state-less predicate to apply on each element.
     * @return the filtered enumerator.
     */
    public default Enumerator<E> filter(Predicate<? super E> predicate) {
        Checks.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        return new PipeEnumerator(this).filter(predicate);
    }

    /**
     * Returns the first element of the current enumerator, if any.
     *
     * @return {@link Optional} instance containing the first element.
     */
    public default Optional<E> first() {
        return hasNext() ? Optional.of(next()) : Optional.empty();
    }

    /**
     * Returns an enumerator consisting of the results of replacing each
     * enumerated element with the content of a mapped enumerator obtained
     * by applying the provided mapper on each enumerated element.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     * <pre>
     * Example:
     * <code>
     * Enumerator.on(1, 2, 3)
     *           .flatMap(i -&gt; Enumerator.on(i*i, i*(i+1)))
     * </code>
     * will produce the sequence 1, 2, 4, 6, 9 and 12.
     * </pre>
     *
     * @param <R> the element type of the new enumerator.
     * @param mapper {@link Function} instance to apply on each enumerated
     * element of the current enumerator.
     * @return the flattened enumerator.
     * @exception IllegalArgumentException <code>mapper</code> is null.
     */
    public default <R> Enumerator<R> flatMap(
            Function<? super E, ? extends Iterator<? extends R>> mapper) {
        Checks.ensureNotNull(mapper, Messages.NULL_ENUMERATOR_MAPPER);
        return new PipeEnumerator(this).flatMap(mapper);
    }

    /**
     * Performs an action on each enumerated element.
     *
     * @param consumer action to perform on each enumerated element.
     * @exception IllegalArgumentException <code>consumer</code> is null.
     */
    public default void forEach(Consumer<? super E> consumer) {
        Checks.ensureNotNull(consumer, Messages.NULL_ENUMERATOR_CONSUMER);
        while (hasNext()) {
            consumer.accept(next());
        }
    }

    /**
     * Returns an infinite enumerator obtained by applying repeatedly the
     * provided unary operator.
     * <p>
     * The resulted enumerator returns <code>seed</code>,
     * <code>f(seed)</code>, <code>f(f(seed))</code> ...
     * </p>
     *
     * @param <E> the type of enumerated elements
     * @param seed the initial element
     * @param f state-less {@link Function} instance to apply on the previous
     * element to obtain the next element
     * @return the iterated enumerator
     * @exception IllegalArgumentException <code>f</code> is null.
     */
    public static <E> Enumerator<E> iterate(E seed, UnaryOperator<E> f) {
        Checks.ensureNotNull(f, Messages.NULL_ENUMERATOR_GENERATOR);
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

    /**
     * Returns the last element of the current enumerator, if any.
     *
     * @return {@link Optional} instance containing the last element.
     */
    public default Optional<E> last() {
        E result = null;
        while (hasNext()) {
            result = next();
        }
        return Optional.ofNullable(result);
    }

    /**
     * Returns the current enumerator truncated to the given size.
     * <p>
     * This method works exactly like <code>take(long)</code>.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param maxSize the maximum number of elements in the resulted
     * enumerator.
     * @return the truncated enumerator.
     * @exception IllegalArgumentException <code>maxSize</code> is negative/
     */
    public default Enumerator<E> limit(long maxSize) {
        return new PipeEnumerator(this).limit(maxSize);
    }

    /**
     * Returns an enumerator enumerating over the elements of the current
     * enumerator while stopping at the first element that does not match
     * the provided predicate.
     * <p>
     * this method works exactly like
     * {@link #takeWhile(java.util.function.Predicate)}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param predicate state-less {@link Predicate} instance to apply on
     * enumerated elements.
     * @return the truncated enumerator.
     * @exception IllegalArgumentException <code>predicate</code> is null.
     */
    public default Enumerator<E> limitWhile(Predicate<? super E> predicate) {
        return takeWhile(predicate);
    }

    /**
     * Returns an enumerator consisting of the results of applying the given
     * function on the enumerated elements.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param <R> the element type of the new enumerator.
     * @param mapper state-less {@link Function} to apply on each enumerated
     * element.
     * @return the mapped enumerator
     * @see #map(java.util.function.BiFunction)
     */
    public default <R> Enumerator<R> map(
            Function<? super E, ? extends R> mapper) {
        return new PipeEnumerator(this).map(mapper);
    }

    /**
     * Returns an enumerator consisting of the results of applying the
     * provided function on the enumerated elements.
     * <p>
     * This method works like {@link #map(java.util.function.Function) } with
     * the distinction that each enumerated element is accompanied by its
     * <code>0</code>-based index.
     * </p>
     *
     * @param <R> the element type of the resulted enumerator.
     * @param mapper state-less {@link  BiFunction} to apply on each enumerated
     * element.
     * @return the mapped enumerator.
     * @exception IllegalArgumentException <code>mapper</code> is null.
     * @see #map(java.util.function.Function)
     */
    public default <R> Enumerator<R> map(
            BiFunction<? super E, ? super Long, ? extends R> mapper) {
        return Reversible.map(this, mapper, false);
    }

    /**
     * Returns the maximum of the enumerated elements according to the provided
     * comparator.
     *
     * @param comparator state-less {@link Comparator} instance
     * to compare enumerated elements.
     * @return {@link Optional} instance containing the maximal enumerated
     * element.
     * @exception IllegalArgumentException <code>comparator</code>
     * is null.
     * @see #min(java.util.Comparator)
     */
    public default Optional<E> max(Comparator<? super E> comparator) {
        Checks.ensureNotNull(comparator, Messages.NULL_ENUMERATOR_COMPARATOR);
        E m = null;
        while (hasNext()) {
            E e = next();
            m = (m == null || comparator.compare(e, m) > 0) ? e : m;
        }
        return Optional.ofNullable(m);
    }

    /**
     * Returns the minimum of the enumerated elements according to the provided
     * comparator.
     *
     * @param comparator state-less {@link Comparator} instance
     * to compare enumerated elements.
     * @return {@link Optional} instance containing the minimal enumerated
     * element.
     * @exception IllegalArgumentException <code>comparator</code>
     * is null.
     * @see #max(java.util.Comparator)
     */
    public default Optional<E> min(Comparator<? super E> comparator) {
        E m = null;
        while (hasNext()) {
            E e = next();
            m = (m == null || comparator.compare(e, m) < 0) ? e : m;
        }
        return Optional.ofNullable(m);
    }

    /**
     * Returns whether no enumerated element matches the provided predicate.
     *
     * @param predicate state-less {@link Predicate} instance to apply
     * to enumerated elements.
     * @return true if no enumerated element matches
     * <code>predicate</code>, false otherwise.
     * @exception IllegalArgumentException <code>predicate</code> is null.
     * @see #allMatch(java.util.function.Predicate)
     * @see #anyMatch(java.util.function.Predicate)
     */
    public default boolean noneMatch(Predicate<? super E> predicate) {
        Checks.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
        while (hasNext()) {
            if (predicate.test(next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an enumerator with the same enumerated elements as the current
     * stream, additionally performing the provided action on each enumerated
     * element.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param action {@link Consumer} instance to apply on the elements as they
     * get enumerated.
     * @return the peeked enumerator.
     * @exception IllegalArgumentException <code>action</code> is null.
     */
    public default Enumerator<E> peek(Consumer<? super E> action) {
        return Reversible.peek(this, action, false);
    }

    /**
     * Prepends the current enumerator with the provided {@link Iterator}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Iterator} instance to prepend in front of the
     * current enumerator.
     * @return the prepended enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> prepend(Iterator<? extends E> elements) {
        return new PipeEnumerator(this).reversedConcat(elements);
    }

    /**
     * Prepends the current enumerator with the provided {@link Iterable}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Iterable} instance to prepend in front of the
     * current enumerator.
     * @return the prepended enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> prepend(Iterable<? extends E> elements) {
        return prepend(of(elements));
    }

    /**
     * Prepends the current enumerator with the provided
     * {@link Enumeration}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Enumeration} instance to prepend in front of the
     * current enumerator.
     * @return the prepended enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> prepend(Enumeration<? extends E> elements) {
        return prepend(of(elements));
    }

    /**
     * Prepends the current enumerator with the provided sequential
     * {@link Stream}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Stream} instance to prepend in front of the
     * current enumerator.
     * @return the prepended enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> prepend(Stream<? extends E> elements) {
        return prepend(of(elements));
    }

    /**
     * Prepends the current enumerator with the provided sequential
     * {@link Spliterator}.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Spliterator} instance to prepend in front of the
     * current enumerator.
     * @return the prepended enumerator.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> prepend(Spliterator<? extends E> elements) {
        return prepend(of(elements));
    }

    /**
     * Prepends the current enumerator with the sequence formed of
     * the elements supplied by the provided {@code Supplier}.
     * <p>
     * The first value supplied by <code>elements</code> that is an empty
     * {@link Optional} marks the end of the sequence to prepend with.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements {@link Supplier} instance supplying the elements to
     * prepend in front of the current enumerator.
     * @return the prepended enumerator.
     * @exception IllegalStateException the current enumerator is enumerating.
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default Enumerator<E> prepend(Supplier<Optional<E>> elements) {
        return prepend(of(elements));
    }

    /**
     * Prepends the current enumerator with the sequence formed of the provided
     * elements.
     * <p>
     * This method works like <code>concatOn(elements)</code> but at the
     * opposite end.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param elements the values to prepend at the beginning of the current
     * enumerator.
     * @return the prepended enumerator.
     */
    public default Enumerator<E> prependOn(E... elements) {
        return prepend(on(elements));
    }

    /**
     * Returns an enumerator enumerating over an ordered sequence between
     * a start and an exclusive end.
     *
     * @param <E> type of enumerated elements.
     * @param startInclusive the first element in the sequence.
     * @param endExclusive the exclusive sequence end.
     * @param succ {@link UnaryOperator} instance returning the successor of
     * a given element.
     * @param cmp {@link Comparator} instance comparing enumerated elements
     * @return the enumerator covering the range.
     * @exception IllegalArgumentException <code>succ</code> or <code>cmp</code>
     * is null.
     */
    public static <E> Enumerator<E> range(E startInclusive,
                                          E endExclusive,
                                          UnaryOperator<E> succ,
                                          Comparator<? super E> cmp) {
        Checks.ensureNotNull(succ, Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(cmp, Messages.NULL_ENUMERATOR_COMPARATOR);
        return cmp.compare(startInclusive, endExclusive) >= 0
               ? Enumerator.empty()
               : iterate(startInclusive, succ)
                        .takeWhile(e -> cmp.compare(e, endExclusive) < 0);
    }

    /**
     * Returns an enumerator enumerating over an ordered sequence between
     * a start and an inclusive end.
     *
     * @param <E> type of enumerated elements.
     * @param startInclusive the first element in the sequence.
     * @param endInclusive the last element in the sequence.
     * @param succ {@link UnaryOperator} instance returning the successor of
     * a given element.
     * @param cmp {@link Comparator} instance comparing enumerated elements
     * @return the enumerator covering the range.
     * @exception IllegalArgumentException <code>succ</code> or <code>cmp</code>
     * is null.
     */
    public static <E> Enumerator<E> rangeClosed(E startInclusive,
                                                E endInclusive,
                                                UnaryOperator<E> succ,
                                                Comparator<? super E> cmp) {
        Checks.ensureNotNull(succ, Messages.NULL_ENUMERATOR_GENERATOR);
        Checks.ensureNotNull(cmp, Messages.NULL_ENUMERATOR_COMPARATOR);
        return cmp.compare(startInclusive, endInclusive) > 0
               ? Enumerator.empty()
               : iterate(startInclusive, succ)
                        .takeWhile(e -> cmp.compare(e, endInclusive) <= 0);
    }

    /**
     * Returns an enumerator enumerating over an integral range, upper limit
     * excluded.
     *
     * @param startInclusive the lower bound of the range.
     * @param endExclusive the exclusive upper bound of the range.
     * @return the enumerator covering the range.
     */
    public static IntRangeEnumerator rangeInt(int startInclusive,
                                              int endExclusive) {
        return new IntRangeEnumerator(startInclusive, endExclusive, false);
    }

    /**
     * Returns an enumerator enumerating over an integral range, upper limit
     * included.
     *
     * @param startInclusive the lower bound of the range.
     * @param endInclusive the inclusive upper bound of the range.
     * @return the enumerator covering the range.
     */
    public static IntRangeEnumerator rangeIntClosed(int startInclusive,
                                                    int endInclusive) {
        return new IntRangeEnumerator(startInclusive, endInclusive, true);
    }

    /**
     * Returns an enumerator enumerating over an long integral range, upper
     * limit excluded.
     *
     * @param startInclusive the lower bound of the range.
     * @param endExclusive the exclusive upper bound of the range.
     * @return the enumerator covering the range.
     */
    public static LongRangeEnumerator rangeLong(long startInclusive,
                                                long endExclusive) {
        return new LongRangeEnumerator(startInclusive, endExclusive, false);
    }

    /**
     * Returns an enumerator enumerating over an long integral range, upper
     * limit included.
     *
     * @param startInclusive the lower bound of the range.
     * @param endInclusive the inclusive upper bound of the range.
     * @return the enumerator covering the range.
     */
    public static LongRangeEnumerator rangeLongClosed(long startInclusive,
                                                      long endInclusive) {
        return new LongRangeEnumerator(startInclusive, endInclusive, true);
    }

    /**
     * Performs a reduction on enumerated elements using an associative
     * accumulation function and returns the reduced value, if any.
     *
     * @param accumulator associative {@link BinaryOperator} combining two
     * enumerated elements.
     * @return {@link Optional} instance containing the reduced value.
     * @exception IllegalArgumentException <code>accumulator</code> is null.
     */
    public default Optional<E> reduce(BinaryOperator<E> accumulator) {
        Checks.ensureNotNull(accumulator, Messages.NULL_ENUMERATOR_ACCUMULATOR);
        if (!hasNext()) {
            return Optional.empty();
        }
        final E first = next();
        return Optional.of(reduce(first, accumulator));
    }

    /**
     * Performs a reduction on enumerated elements using an identity element
     * and an associative accumulation function and returns the reduced value.
     *
     * @param identity starting point for the reduction.
     * @param accumulator associative {@link BinaryOperator} combining
     * two enumerated elements.
     * @return the reduced value.
     * @exception IllegalArgumentException <code>accumulator</code> is null.
     */
    public default E reduce(E identity, BinaryOperator<E> accumulator) {
        Checks.ensureNotNull(accumulator, Messages.NULL_ENUMERATOR_ACCUMULATOR);
        E result = identity;
        while (hasNext()) {
            result = accumulator.apply(result, next());
        }
        return result;
    }

    /**
     * Returns an enumerator that enumerates the same element the given number
     * of times.
     *
     * @param <E> type of enumerated element.
     * @param element the element to return.
     * @param count the number of elements to return.
     * @return the enumerator repeating the element.
     * @exception IllegalArgumentException <code>count</code> is negative.
     * @see #repeatAll(java.util.function.Supplier, long)
     * @see #repeatAll(int)
     * @see #repeatEach(int)
     */
    public static <E> Enumerator<E> repeat(E element, long count) {
        Checks.ensureNonNegative(count, Messages.NEGATIVE_ENUMERATOR_SIZE);
        return rangeLong(0, count).map(l -> element);
    }

    /**
     * Returns an enumerator that enumerates over the iterator supplied lazily
     * the given number of times.
     *
     * @param <E> type of enumerated element.
     * @param source supplier of the iterator to repeat.
     * @param count the number of times to repeat.
     * @return the repeated enumerator.
     * @exception IllegalArgumentException <code>source</code> is
     * null or <code>count</code> is negative.
     * @see #repeat(java.lang.Object, long)
     * @see #repeatAll(int)
     * @see #repeatEach(int)
     */
    public static <E> Enumerator<E> repeatAll(Supplier<Iterator<E>> source,
                                              long count) {
        Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        Checks.ensureNonNegative(count,
                                Messages.NEGATIVE_ENUMERATOR_EXPECTED_COUNT);
        return rangeLong(0, count).flatMap(l -> source.get());
    }

    /**
     * Returns an enumerator that keeps enumerating over the current sequence,
     * a given number of times.
     * <p>
     * The current enumerator gets iterated only once.
     * </p>
     *
     * @param count the number of times to repeatAll the <code>elements</code>
     * sequence.
     * @return the repeating enumerator.
     * @exception IllegalArgumentException <code>elements</code> is
     * <code>null</code> or <code>count</code> is negative.
     * @see #repeat(java.lang.Object, long)
     * @see #repeatAll(java.util.function.Supplier, long)
     * @see #repeatEach(int)
     */
    public default Enumerator<E> repeatAll(int count) {
        Checks.ensureNonNegative(count,
                                Messages.NEGATIVE_ENUMERATOR_EXPECTED_COUNT);
        final Enumerator<E>[] sharing = asShareable().share(count);
        return Enumerator.rangeInt(0, sharing.length)
                         .flatMap(i -> sharing[i]);
    }

    /**
     * Enumerates over the elements of the current enumerator the given number
     * of times.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param count the number of times to repeatAll the elements in the
     * sequence.
     * @return the enumerator of repeated elements.
     * @exception IllegalArgumentException <code>elements</code> is
     * <code>null</code> or <code>count</code> is negative.
     * @see #repeat(java.lang.Object, long)
     * @see #repeatAll(java.util.function.Supplier, long)
     * @see #repeatAll(int)
     */
    public default Enumerator<E> repeatEach(int count) {
        Checks.ensureNonNegative(count,
                                Messages.NEGATIVE_ENUMERATOR_EXPECTED_COUNT);
        return flatMap(e -> repeat(e, count));
    }

    /**
     * Returns an enumerator that reverses the order of the enumerated elements.
     *
     * @return the reversed enumerator.
     */
    public default Enumerator<E> reverse() {
        final LinkedList<E> elements = new LinkedList();
        while(hasNext()) {
            elements.add(next());
        }
        return Enumerator.of(elements.descendingIterator());
    }

    /**
     * Returns the one and only element of the current enumerator.
     *
     * @return the single element.
     * @exception InputMismatchException the current enumerator has
     * <code>0</code> or more than <code>1</code> elements.
     */
    public default E single() {
        if (!hasNext()) {
            throw new InputMismatchException(
                    Messages.NO_SINGLE_ENUMERATOR_ELEMENT);
        }
        final E result = next();
        if (hasNext()) {
            throw new InputMismatchException(
                    Messages.NO_SINGLE_ENUMERATOR_ELEMENT);
        }
        return result;
    }

    /**
     * Returns the current enumerator with the given number of front elements
     * dropped.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param n the number of front elements to drop.
     * @return the truncated enumerator.
     */
    public default Enumerator<E> skip(long n) {
        return new PipeEnumerator(this).skip(n);
    }

    /**
     * Returns the current enumerator with front elements dropped as long as
     * they match a given predicate.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param predicate {@link Predicate} instance to match the enumerated
     * elements against.
     * @return the enumerator with front elements dropped.
     */
    public default Enumerator<E> skipWhile(Predicate<? super E> predicate) {
        return new PipeEnumerator(this).skipWhile(predicate);
    }

    /**
     * Returns an enumerator the sorts the enumerated elements.
     *
     * @return the sorted enumerator.
     */
    public default Enumerator<E> sorted() {
        return of(asStream().sorted());
    }

    /**
     * Returns an enumerator that sorts the current elements according to the
     * given comparator.
     *
     * @param comparator {@link Comparator} instance to compare enumerated
     * elements.
     * @return the sorted enumerator.
     * @exception IllegalArgumentException <code>comparator</code> is null.
     */
    public default Enumerator<E> sorted(Comparator<? super E> comparator) {
        Checks.ensureNotNull(comparator, Messages.NULL_ENUMERATOR_COMPARATOR);
        return of(asStream().sorted(comparator));
    }

    /**
     * Returns the current enumerator truncated to the given size.
     * <p>
     * This method works exactly like <code>limit(long)</code>.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param n the maximum number of elements in the resulted enumerator.
     * @return the truncated enumerator.
     * @exception IllegalArgumentException <code>n</code> is negative.
     */
    public default Enumerator<E> take(long n) {
        return limit(n);
    }

    /**
     * Returns an enumerator enumerating over the elements of the current
     * enumerator while stopping at the first element that does not match
     * the provided predicate.
     * <p>
     * this method works exactly like <code>limitWhile(Predicate)</code>.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param predicate state-less {@link Predicate} instance to apply on
     * enumerated elements.
     * @return the truncated enumerator.
     * @exception IllegalArgumentException <code>predicate</code> is null.
     */
    public default Enumerator<E> takeWhile(Predicate<? super E> predicate) {
        return new PipeEnumerator(this).takeWhile(predicate);
    }

    /**
     * Collects the current enumerator into an array of elements of given class.
     *
     * @param clazz {@link Class} of array elements.
     * @return the array of elements.
     * @exception IllegalArgumentException <code>clazz</code> is null.
     */
    public default E[] toArray(Class<E> clazz) {
        Checks.ensureNotNull(clazz, Messages.NULL_ENUMERATOR_CLASS);
        return toList().toArray((E[])Array.newInstance(clazz, 0));
    }

    /**
     * Collects the current enumerator into a list of elements.
     *
     * @return the list of elements.
     */
    public default List<E> toList() {
        return collect(Collectors.toList());
    }

    /**
     * Collects the current enumerator into a {@code Map} according to
     * a key mapping function and a value mapping function.
     *
     * @param <K> the map key type.
     * @param <V> the map value type.
     * @param keyMapper {@link Function} instance extracting the map key
     * from enumerated elements.
     * @param valueMapper {@code Function} instance extracting the map value
     * from enumerated elements.
     * @return {@link Map} instance containing the results of key and value
     * mappings.
     */
    public default <K, V> Map<K, V> toMap(Function<? super E, K> keyMapper,
                                          Function<? super E, V> valueMapper) {
        return collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * Collects the current enumerator into a set.
     *
     * @return the set of elements.
     */
    public default Set<E> toSet() {
        return collect(Collectors.toSet());
    }

    /**
     * Returns an enumerator enumerating over the current enumerator and
     * the provided enumerator, all duplicates removed.
     * <p>
     * This method works exactly like {@code this.concat(others).distinct()}.
     * </p>
     *
     * @param others enumerator to perform union with.
     * @return the unified enumerator.
     * @exception IllegalArgumentException <code>others</code> is null.
     */
    public default Enumerator<E> union(Iterator<? extends E> others) {
        return concat(others).distinct();
    }

    /**
     * Returns an enumerator enumerating over the current enumerator and
     * the provided {@code Iterable}, all duplicates removed.
     * <p>
     * This method works exactly like {@code this.union(on(others))}.
     * </p>
     *
     * @param others {@link Iterable} to perform union with.
     * @return the unified enumerator.
     * @exception IllegalArgumentException <code>others</code> is null.
     */
    public default Enumerator<E> union(Iterable<? extends E> others) {
        return union(of(others));
    }

    /**
     * Returns an enumerator enumerating over the current enumerator and
     * the provided {@code Enumeration}, all duplicates removed.
     * <p>
     * This method works exactly like {@code this.union(on(others))}.
     * </p>
     *
     * @param others {@link Enumeration} to perform union with.
     * @return the unified enumerator.
     * @exception IllegalArgumentException <code>others</code> is null.
     */
    public default Enumerator<E> union(Enumeration<? extends E> others) {
        return union(of(others));
    }

    /**
     * Returns an enumerator enumerating over the current enumerator and
     * the provided {@code Stream}, all duplicates removed.
     * <p>
     * This method works exactly like {@code this.union(on(others))}.
     * </p>
     *
     * @param others {@link Stream} to perform union with.
     * @return the unified enumerator.
     * @exception IllegalArgumentException <code>others</code> is null.
     */
    public default Enumerator<E> union(Stream<? extends E> others) {
        return union(of(others));
    }

    /**
     * Returns an enumerator enumerating over the current enumerator and
     * the provided {@code Spliterator}, all duplicates removed.
     * <p>
     * This method works exactly like {@code this.union(on(others))}.
     * </p>
     *
     * @param others {@link Spliterator} to perform union with.
     * @return the unified enumerator.
     * @exception IllegalArgumentException <code>others</code> is null.
     */
    public default Enumerator<E> union(Spliterator<? extends E> others) {
        return union(of(others));
    }

    /**
     * Returns an enumerator enumerating over the current enumerator and
     * the elements supplied by the provided {@code Supplier}, all duplicates
     * removed.
     * <p>
     * This method works exactly like {@code this.union(on(others))}.
     * </p>
     *
     * @param others {@link Supplier} to perform union with.
     * @return the unified enumerator.
     * @exception IllegalArgumentException <code>others</code> is null.
     */
    public default Enumerator<E> union(Supplier<Optional<E>> others) {
        return union(of(others));
    }

    /**
     * Returns an enumerator enumerating over the current enumerator
     * and the given elements, all duplicates removed.
     * <p>
     * This method works exactly like {@code this.union(on(others))}.
     * </p>
     *
     * @param others elements to perform union with.
     * @return the unified enumerator.
     */
    public default Enumerator<E> unionOn(E... others) {
        return union(on(others));
    }

    /**
     * Returns an enumerator consisting of pairs of elements from the
     * current enumerator and the given {@code Iterator}, while any
     * of them has elements.
     * <p>
     * The resulted enumerator stops when both the current enumerator and the
     * provided {@link Iterator} stop. The first element of a returned
     * pair is empty {@link Optional} if the current enumerator has no
     * elements. Likewise, the second element of a returned pair is empty
     * {@code Optional} if the provided {@code Iterator} has no elements.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param <T> the of elements to zip with.
     * @param elements {@code Iterator} to zip with.
     * @return the zipped enumerator.
     * @see #zipBoth(java.util.Iterator)
     * @see #zipLeft(java.util.Iterator)
     * @see #zipRight(java.util.Iterator)
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default <T>
                   Enumerator<Pair<Optional<E>, Optional<T>>>
                   zipAny(Iterator<T> elements) {
        return zipAll((Iterator<E>)elements)
               .map(arr -> Pair.of(arr[0], (Optional<T>)arr[1]));
    }

    /**
     * Returns an enumerator consisting of pairs of elements from the
     * current enumerator and the given {@code Iterator}, while both
     * have elements.
     * <p>
     * The resulted enumerator stops when aby of the current enumerator or the
     * provided {@link Iterator} stop.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     *
     * @param <T> the of elements to zip with.
     * @param elements {@code Iterator} to zip with.
     * @return the zipped enumerator.
     * @see #zipAny(java.util.Iterator)
     * @see #zipLeft(java.util.Iterator)
     * @see #zipRight(java.util.Iterator)
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default <T>
                   Enumerator<Pair<E, T>>
                   zipBoth(Iterator<T> elements) {
        return zipAll((Iterator<E>)elements)
               .takeWhile(arr -> arr[0].isPresent() && arr[1].isPresent())
               .map(arr -> Pair.of(arr[0].get(), ((Optional<T>)arr[1]).get()));
    }

    /**
     * Returns an enumerator consisting of pairs of elements from the
     * current enumerator and the given {@code Iterator}, while the current
     * enumerator has elements.
     * <p>
     * The resulted enumerator stops when the current enumerator stops.
     * The second element of a returned pair is empty {@link Optional} if
     * the provided {@link Iterator} has no elements.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     * @param <T> the of elements to zip with.
     * @param elements {@code Iterator} to zip with.
     * @return the zipped enumerator.
     * @see #zipAny(java.util.Iterator)
     * @see #zipBoth(java.util.Iterator)
     * @see #zipRight(java.util.Iterator)
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default <T>
                   Enumerator<Pair<E, Optional<T>>>
                   zipLeft(Iterator<T> elements) {
        return zipAll((Iterator<E>)elements)
               .takeWhile(arr -> arr[0].isPresent())
               .map(arr -> Pair.of(arr[0].get(), (Optional<T>)arr[1]));
    }

    /**
     * Returns an enumerator consisting of pairs of elements from the
     * current enumerator and the given {@code Iterator}, while the provided
     * iterator has elements.
     * <p>
     * The resulted enumerator stops when the provided {@link Iterator} stops.
     * The first element of a returned pair is empty {@link Optional} if the
     * current enumerator has no elements.
     * </p>
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     * @param <T> the of elements to zip with.
     * @param elements {@code Iterator} to zip with.
     * @return the zipped enumerator.
     * @see #zipAny(java.util.Iterator)
     * @see #zipBoth(java.util.Iterator)
     * @see #zipLeft(java.util.Iterator)
     * @exception IllegalArgumentException <code>elements</code> is null.
     */
    public default <T>
                   Enumerator<Pair<Optional<E>, T>>
                   zipRight(Iterator<T> elements) {
        return zipAll((Iterator<E>)elements)
               .takeWhile(arr -> arr[1].isPresent())
               .map(arr -> Pair.of(arr[0], ((Optional<T>)arr[1]).get()));
    }

    /**
     * Returns an enumerator consisting of an array of {@code Optional}
     * objects containing elements from the current enumerator and
     * the given {@code Iterator} instances, while any has elements.
     * <p>
     * <em>This operation is highly composable.</em>
     * </p>
     * @param first first {@link Iterator} to zip with.
     * @param rest the rest of iterators to zip with.
     * @return the zipped enumerator of arrays of {@link Optional} instances.
     */
    public default Enumerator<Optional<E>[]>
                   zipAll(Iterator<? extends E> first,
                          Iterator<? extends E>... rest) {
        Checks.ensureNotNull(first, Messages.NULL_ITERATOR);
        for(Iterator<?> it: rest) {
            Checks.ensureNotNull(it, Messages.NULL_ITERATOR);
        }
        return new PipeEnumerator(this).zipAll(first, rest);
    }
}
