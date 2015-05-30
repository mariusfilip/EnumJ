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

import java.util.Optional;
import java.util.Random;
import java.util.function.IntSupplier;
import org.apache.commons.lang3.tuple.Pair;

public class EnumeratorGenerator {

    public static final int SIZE = EnumerableGenerator.SIZE;

    private final Random rnd;
    private final EnumerableGenerator gen;

    public EnumeratorGenerator(long seed) {
        this.rnd = new Random(seed);
        this.gen = new EnumerableGenerator(seed);
    }
    private EnumeratorGenerator(EnumerableGenerator gen) {
        this.gen = gen;
        this.rnd = this.gen.rnd;
    }

    public static Enumerator<EnumeratorGenerator> generators() {
        return EnumerableGenerator.generators()
                                  .map(gen -> new EnumeratorGenerator(gen))
                                  .enumerator();
    }

    public static Enumerator<Pair<EnumeratorGenerator, EnumeratorGenerator>>
            generatorPairs() {
        return EnumerableGenerator
                .generatorPairs()
                .map(p -> Pair.of(new EnumeratorGenerator(p.getLeft()),
                                  new EnumeratorGenerator(p.getRight())))
                .enumerator();
    }

    public Double[] elems() {
        return gen.elems();
    }

    public IntSupplier boundRnd(int bound) {
        return gen.boundRnd(bound);
    }

    public Enumerator<Double> onEnumerator() {
        return gen.onEnumerable().enumerator();
    }

    public Enumerator<Double> ofIteratorEnumerator() {
        return gen.ofIteratorEnumerable()
                  .enumerator();
    }

    public Enumerator<Double> ofIterableEnumerator() {
        return gen.ofIterableEnumerable().enumerator();
    }

    public Enumerator<Double> ofEnumerationEnumerator() {
        return gen.ofEnumerationEnumerable().enumerator();
    }

    public Enumerator<Double> ofStreamEnumerator() {
        return gen.ofStreamEnumerable().enumerator();
    }

    public Enumerator<Double> ofSpliteratorEnumerator() {
        return gen.ofSpliteratorEnumerable().enumerator();
    }

    public Enumerator<Double> ofSupplierEnumerator() {
        return gen.ofSupplierEnumerable().enumerator();
    }

    public Enumerator<Double> ofLazyIteratorEnumerator() {
        return gen.ofLazyIteratorEnumerable().enumerator();
    }

    public Enumerator<Double> ofLazyIterableEnumerator() {
        return Enumerator.ofLazyIterable(gen::ofIterableEnumerable);
    }

    public Enumerator<Double> ofLazyEnumerationEnumerator() {
        return gen.ofLazyEnumerationEnumerable().enumerator();
    }

    public Enumerator<Double> ofLazyStreamEnumerator() {
        return gen.ofLazyStreamEnumerable().enumerator();
    }

    public Enumerator<Double> ofLazySpliteratorEnumerator() {
        return gen.ofLazySpliteratorEnumerable().enumerator();
    }

    public LateBindingEnumerator<Double> ofLateBindingEnumerator() {
        return Enumerator.ofLateBinding(Double.class);
    }

    public Enumerator<Double> enumerator() {
        final Enumerator[] subEnumerators = {
            onEnumerator(),
            ofIteratorEnumerator(),
            ofIterableEnumerator(),
            ofEnumerationEnumerator(),
            ofStreamEnumerator(),
            ofSpliteratorEnumerator(),
            ofSupplierEnumerator(),
            ofLazyIteratorEnumerator(),
            ofLazyIterableEnumerator(),
            ofLazyEnumerationEnumerator(),
            ofLazyStreamEnumerator(),
            ofLazySpliteratorEnumerator()
        };
        return Enumerator.choiceOf(
                boundRnd(subEnumerators.length),
                subEnumerators[0].as(Double.class),
                subEnumerators[1].as(Double.class),
                Enumerator.on(subEnumerators)
                          .skip(2)
                          .toArray(Enumerator.class));
    }
}
