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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A type of {@link Iterable} with high composability.
 *
 * @param <E> the type of elements returned by this enumerable.
 */
public interface Enumerable<E> extends Iterable<E> {

    @Override
    public default Iterator<E> iterator() {
        return enumerator();
    }

    public Enumerator<E> enumerator();
    public boolean consumed();

    // ---------------------------------------------------------------------- //

    @SafeVarargs
    public static <E> Enumerable<E> on(E... elements) {
        return of(Arrays.asList(elements));
    }

    public static <E> Enumerable<E> of(Iterable<E> source) {
        return IterableEnumerable.of(source);
    }

    public static <E> OnceEnumerable<E> of(Iterator<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    public static <E> OnceEnumerable<E> of(Enumeration<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    public static <E> OnceEnumerable<E> of(Stream<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    public static <E> OnceEnumerable<E> of(Spliterator<E> source) {
        return Enumerator.of(source).asEnumerable();
    }

    public static <E> OnceEnumerable<E> of(Supplier<Optional<E>> source) {
        return Enumerator.of(source).asEnumerable();
    }

    public static <E> Enumerable<E> ofSuppliedIterators(
            Supplier<Optional<Iterator<E>>> iteratorSupplier) {
        return RepeatableEnumerable.of(iteratorSupplier);
    }

    public static <E> Enumerable<E> ofLazyIterable(
            Supplier<? extends Iterable<E>> source) {
        return LazyEnumerable.of(source);
    }

    public static <E> OnceEnumerable<E> ofLazyIterator(
            Supplier<? extends Iterator<E>> source) {
        return of(Enumerator.ofLazyIterator(source));
    }

    public static <E> OnceEnumerable<E> ofLazyEnumeration(
            Supplier<? extends Enumeration<E>> source) {
        return of(Enumerator.ofLazyEnumeration(source));
    }

    public static <E> OnceEnumerable<E> ofLazyStream(
            Supplier<? extends Stream<E>> source) {
        return of(Enumerator.ofLazyStream(source));
    }

    public static <E> OnceEnumerable<E> ofLazySpliterator(
            Supplier<? extends Spliterator<E>> source) {
        return of(Enumerator.ofLazySpliterator(source));
    }
}
