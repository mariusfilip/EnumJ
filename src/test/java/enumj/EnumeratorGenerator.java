/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Optional;
import java.util.Random;
import java.util.function.IntSupplier;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
public class EnumeratorGenerator {

    public static final int SIZE = 100;

    private final long seed;
    private final Random rnd;

    public EnumeratorGenerator(long seed) {
        this.seed = seed;
        this.rnd = new Random(this.seed);
    }

    public static Enumerator<EnumeratorGenerator> generators() {
        final Random rnd = new Random(9691);
        return Enumerator.of(
                () -> Optional.of(new EnumeratorGenerator(rnd.nextLong())));
    }

    public static Enumerator<Pair<EnumeratorGenerator, EnumeratorGenerator>>
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

    public Enumerator<Double> onEnumerator() {
        return Enumerator.on(elems());
    }

    public Enumerator<Double> ofIteratorEnumerator() {
        return Enumerator.of(onEnumerator().asStream()
                                           .iterator());
    }

    public Enumerator<Double> ofIterableEnumerator() {
        return Enumerator.of(ofIteratorEnumerator().asIterable());
    }

    public Enumerator<Double> ofEnumerationEnumerator() {
        return Enumerator.of(ofIterableEnumerator().asEnumeration());
    }

    public Enumerator<Double> ofStreamEnumerator() {
        return Enumerator.of(ofEnumerationEnumerator().asStream());
    }

    public Enumerator<Double> ofSpliteratorEnumerator() {
        return Enumerator.of(ofStreamEnumerator().asSpliterator());
    }

    public Enumerator<Double> ofSupplierEnumerator() {
        return Enumerator.of(ofSpliteratorEnumerator().asSupplier());
    }

    public Enumerator<Double> ofLazyIteratorEnumerator() {
        return Enumerator.ofLazyIterator(
                () -> ofIteratorEnumerator().asStream().iterator());
    }

    public Enumerator<Double> ofLazyIterableEnumerator() {
        return Enumerator.ofLazyIterable(
                () -> ofIterableEnumerator().asIterable());
    }

    public Enumerator<Double> ofLazyEnumerationEnumerator() {
        return Enumerator.ofLazyEnumeration(
                () -> ofEnumerationEnumerator().asEnumeration());
    }

    public Enumerator<Double> ofLazyStreamEnumerator() {
        return Enumerator.ofLazyStream(
                () -> ofStreamEnumerator().asStream());
    }

    public Enumerator<Double> ofLazySpliteratorEnumerator() {
        return Enumerator.ofLazySpliterator(
                () -> ofStreamEnumerator().asSpliterator());
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
