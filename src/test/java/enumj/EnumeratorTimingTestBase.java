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

import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class EnumeratorTimingTestBase<TArgs,E> {

    public static int percentage(Pair<Long,Long> result) {
        return percentage(result.getLeft(), result.getRight());
    }
    public static int percentage(long a, long b) {
        final double ratio = ((double)a) / b;
        return (int)(ratio * 100);
    }

    // ---------------------------------------------------------------------- //

    protected abstract Enumerator<E> enumerator(TArgs args);
    protected abstract Stream<E> stream(TArgs args);
    protected abstract Enumerator<E> transform(TArgs args,
                                               Enumerator<E> enumerator);
    protected abstract Stream<E> transform(TArgs args, Stream<E> stream);
    protected abstract long sizeOf(TArgs args);
    protected abstract void consumeEnumerator(E element);
    protected abstract void consumeStream(E element);

    private Enumerator<E> testEnumeratorConstruction(TArgs args) {
        return transform(args, enumerator(args));
    }
    private Stream<E> testStreamConstruction(TArgs args) {
        return transform(args, stream(args));
    }
    private Enumerator<E> testEnumeratorConsumption(Enumerator<E> enumerator) {
        enumerator.forEach(this::consumeEnumerator);
        return enumerator;
    }
    private Stream<E> testStreamConsumption(Stream<E> stream) {
        stream.forEach(this::consumeStream);
        return stream;
    }
    private Enumerator<E> testEnumeratorBoth(TArgs args) {
        return testEnumeratorConsumption(testEnumeratorConstruction(args));
    }
    private Stream<E> testStreamBoth(TArgs args) {
        return testStreamConsumption(testStreamConstruction(args));
    }

    // ---------------------------------------------------------------------- //

    protected abstract TArgs[] comparisonArgs();
    protected abstract double comparisonConstructionFactor(TArgs args);
    protected abstract double comparisonConsumptionFactor(TArgs args);

    private <U,V> void streamComparisonProc(
            String testName,
            Function<TArgs[],Pair<Long,Long>[]> test,
            Function<TArgs,Double> factor) {
        System.out.println(testName);

        final TArgs[] args = comparisonArgs();
        final Pair<Long,Long>[] results = test.apply(args);
        assertEquals(args.length, results.length);

        System.out.println("Instances," +
                           "Enumerator(uS),Stream(uS)," +
                           "Enumerator(ns/inst),Stream(ns/inst)," +
                           "Enumerator/Stream(%)");
        for(int i=0; i<args.length; ++i) {
            final long size = sizeOf(args[i]);
            System.out.println(size + "," +
                               (results[i].getLeft()/1000) + "," +
                               (results[i].getRight()/1000) + "," +
                               (results[i].getLeft()/size) + "," +
                               (results[i].getRight()/size) + "," +
                               percentage(results[i]));
            assertTrue(results[i].getLeft() <=
                       factor.apply(args[i]) * results[i].getRight());
        }
    }

    @Test
    public void streamComparisonConstructionTest() {
        streamComparisonProc(
                "streamComparisonConstructionTest",
                args -> NanoTimer.nanos(
                        this::testEnumeratorConstruction,
                        this::testStreamConstruction,
                        args),
                this::comparisonConstructionFactor);
    }
    @Test
    public void streamComparisonConsumptionTest() {
        streamComparisonProc(
                "streamComparisonConsumptionTest",
                args -> NanoTimer.buildNanos(
                        this::testEnumeratorConstruction,
                        this::testEnumeratorConsumption,
                        this::testStreamConstruction,
                        this::testStreamConsumption,
                        args),
                this::comparisonConsumptionFactor);
    }
    @Test
    public void streamComparisonBothTest() {
        streamComparisonProc(
                "streamComparisonBothTest",
                args -> NanoTimer.nanos(
                        this::testEnumeratorBoth,
                        this::testStreamBoth,
                        args),
                args -> Math.max(comparisonConstructionFactor(args),
                                 comparisonConsumptionFactor(args)));
    }

    // ---------------------------------------------------------------------- //

    protected abstract TArgs[] scalabilityArgs();
    protected abstract double scalabilityConstructionFactor(TArgs args);
    protected abstract double scalabilityConsumptionFactor(TArgs args);

    private void scalabilityProc(
            String testName,
            Function<TArgs[],long[]> test,
            Function<TArgs,Double> factor) {
        System.out.println(testName);
        
        final TArgs[] args = scalabilityArgs();
        final long[] results = test.apply(args);
        assertEquals(args.length, results.length);

        System.out.println("Instances," +
                           "Enumerator(uS)," +
                           "Enumerator(ns/inst)");
        long sum = 0;
        long count = 0;
        for(int i=0; i<args.length; ++i) {
            final long size = sizeOf(args[i]);
            final long perInst = results[i]/size;
            System.out.println(size + "," +
                               (results[i]/1000) + "," +
                               perInst);
            if (i > Math.max(5, args.length/2)) {
                final double avg = sum / count;
                assertTrue(perInst <= factor.apply(args[i]) * avg);
            }
            sum += results[i];
            count += size;
        }
    }

    @Test
    public void scalabilityConstructionTest() {
        scalabilityProc(
                "scalabilityConstructionTest",
                args -> NanoTimer.nanos(this::testEnumeratorConstruction,
                                        args),
                this::scalabilityConstructionFactor);
    }
    @Test
    public void scalabilityConsumptionTest() {
        scalabilityProc(
                "scalabilityConsumptionTest",
                args -> NanoTimer.buildNanos(this::testEnumeratorConstruction,
                                             this::testEnumeratorConsumption,
                                             args),
                this::scalabilityConsumptionFactor);
    }
    @Test
    public void scalabilityBothTest() {
        scalabilityProc(
                "scalabilityBothTest",
                args -> NanoTimer.nanos(this::testEnumeratorBoth, args),
                args -> Math.max(scalabilityConstructionFactor(args),
                                 scalabilityConsumptionFactor(args)));
    }
}
