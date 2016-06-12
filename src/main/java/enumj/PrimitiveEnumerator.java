/*
 * The MIT License
 *
 * Copyright 2016 Marius Filip.
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

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public final class PrimitiveEnumerator {
    public interface OfInt extends Enumerator<Integer>,
                                   PrimitiveIterator.OfInt {
        @SafeVarargs
        public static OfInt on(int... elements) {
            return new PrimitiveArrayEnumerator.OfInt(elements);
        }
        public static OfInt of(int[] elements) {
            return new PrimitiveArrayEnumerator.OfInt(elements);
        }
        public static OfInt of(Iterator<Integer> source) {
            return PrimitiveIteratorEnumerator.ofGenericInt(source);
        }
        public static OfInt of(PrimitiveIterator.OfInt source) {
            return PrimitiveIteratorEnumerator.of(source);
        }
        public static OfInt of(IntStream source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            return of(source.iterator());
        }
        public static OfInt of(Spliterator.OfInt source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            return of(Spliterators.iterator(source));
        }
        public static OfInt ofLazyIterator(
                Supplier<? extends PrimitiveIterator.OfInt> source) {
            return LazyPrimitiveEnumerator.of(source);
        }
        public static OfInt ofLazyStream(
                Supplier<? extends IntStream> source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            return LazyPrimitiveEnumerator.of(() -> of(source.get()));
        }
        public static OfInt ofLazySpliterator(
                Supplier<? extends Spliterator.OfInt> source) {
            Checks.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
            return LazyPrimitiveEnumerator.of(() -> of(source.get()));
        }
        public static LateBindingPrimitiveEnumerator.OfInt ofLateBinding() {
            return new LateBindingPrimitiveEnumerator.OfInt();
        }

        // ------------------------------------------------------------------ //
        
        public default Enumerator<Integer> asGeneric() {
            return (Enumerator<Integer>)this;
        }
        public default IntStream asIntStream() {
            return StreamSupport.intStream(asIntSpliterator(), false);
        }
        public default Spliterator.OfInt asIntSpliterator() {
            return Spliterators.spliteratorUnknownSize(this,
                                                       Spliterator.ORDERED);
        }

        // ------------------------------------------------------------------ //

        public default boolean allMatch(IntPredicate predicate) {
            Checks.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
            while(hasNext()) {
                if (!predicate.test(nextInt())) {
                    return false;
                }
            }
            return true;
        }
        public default boolean anyMatch(IntPredicate predicate) {
            Checks.ensureNotNull(predicate, Messages.NULL_ENUMERATOR_PREDICATE);
            while(hasNext()) {
                if (predicate.test(nextInt())) {
                    return true;
                }
            }
            return false;
        }
        public default OfInt append(int... elements) {
            return concat(on(elements));
        }
        public default OfInt concat(PrimitiveIterator.OfInt elements) {
            return of(new PipeEnumerator(this).concat(elements));
        }
    }
}
