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
import java.util.Optional;
import java.util.Random;
import java.util.function.IntSupplier;
import org.apache.commons.lang3.tuple.Pair;

public class EnumerableGenerator {

    public static final int SIZE = 100;

    public final Random rnd;

    public EnumerableGenerator(long seed) {
        this.rnd = new Random(seed);
    }

    public static Enumerable<EnumerableGenerator> generators() {
        final Random rnd = new Random(9691);
        return Enumerable.of(
                () -> Optional.of(new EnumerableGenerator(rnd.nextLong())));
    }

    public static Enumerable<Pair<EnumerableGenerator, EnumerableGenerator>>
            generatorPairs() {
        return generators().zipBoth(generators());
    }

    public Double[] elems() {
        final Double[] ret = new Double[SIZE];
        final Random rnd = new Random(this.rnd.nextLong());
        for(int i=0; i<SIZE; ++i) {
            ret[i] = rnd.nextDouble();
        }
        return ret;
    }

    public IntSupplier boundRnd(int bound) {
        final Random rnd = new Random(this.rnd.nextLong());
        return () -> rnd.nextInt(bound);
    }

    public Enumerable<Double> onEnumerable() {
        return Enumerable.on(elems());
    }

    public Enumerable<Double> ofIterableEnumerable() {
        return Enumerable.of(Arrays.asList(elems()));
    }

    public Enumerable<Double> ofIteratorEnumerable() {
        return Enumerable.of(onEnumerable().iterator());
    }

    public Enumerable<Double> ofEnumerationEnumerable() {
        return Enumerable.of(ofIterableEnumerable().asEnumeration());
    }

    public Enumerable<Double> ofStreamEnumerable() {
        return Enumerable.of(ofEnumerationEnumerable().asStream());
    }

    public Enumerable<Double> ofSpliteratorEnumerable() {
        return Enumerable.of(ofStreamEnumerable().asSpliterator());
    }

    public Enumerable<Double> ofSupplierEnumerable() {
        return Enumerable.of(ofSpliteratorEnumerable()
                .enumerator()
                .asSupplier());
    }

    public Enumerable<Double> ofLazyIteratorEnumerable() {
        return Enumerable.ofLazyIterator(
                () -> ofIteratorEnumerable().iterator());
    }

    public Enumerable<Double> ofLazyIterableEnumerable() {
        return Enumerable.ofLazyIterable(() -> ofIterableEnumerable());
    }

    public Enumerable<Double> ofLazyEnumerationEnumerable() {
        return Enumerable.ofLazyEnumeration(
                () -> ofEnumerationEnumerable().asEnumeration());
    }

    public Enumerable<Double> ofLazyStreamEnumerable() {
        return Enumerable.ofLazyStream(
                () -> ofStreamEnumerable().asStream());
    }

    public Enumerable<Double> ofLazySpliteratorEnumerable() {
        return Enumerable.ofLazySpliterator(
                () -> ofStreamEnumerable().asSpliterator());
    }

    public LateBindingEnumerable<Double> ofLateBindingEnumerable() {
        return Enumerable.ofLateBinding(Double.class);
    }

    public Enumerable<Double> enumerable() {
        final Enumerable[] subEnumerators = {
            onEnumerable(),
            ofIterableEnumerable(),
            ofIteratorEnumerable(),
            ofEnumerationEnumerable(),
            ofStreamEnumerable(),
            ofSpliteratorEnumerable(),
            ofSupplierEnumerable(),
            ofLazyIteratorEnumerable(),
            ofLazyIterableEnumerable(),
            ofLazyEnumerationEnumerable(),
            ofLazyStreamEnumerable(),
            ofLazySpliteratorEnumerable()
        };
        return Enumerable.choiceOf(
                boundRnd(subEnumerators.length),
                subEnumerators[0].as(Double.class),
                subEnumerators[1].as(Double.class),
                Enumerator.on(subEnumerators)
                          .skip(2)
                          .toArray(Enumerable.class));
    }
}
