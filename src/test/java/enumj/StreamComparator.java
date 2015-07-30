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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;

public abstract class StreamComparator<T,E> {

    private final Random rnd;
    private final int    depth;
    private final int    length;

    public  Stream<T> lhs;
    public  E         rhs;
    private boolean   built;

    protected StreamComparator(Random rnd,
                               int    maxDepth,
                               int    maxLength) {
        assert rnd != null;
        assert maxDepth >= 0;
        assert maxLength > 0;

        this.rnd = rnd;
        this.depth = maxDepth;
        this.length = maxLength;

        this.lhs = lhs;
        this.rhs = rhs;
    }

    protected void rndSpin() {
        rndSpin(depth + length);
    }
    protected void rndSpin(int times) {
        assert times > 0;
        times = rnd.nextInt(times);
        while(times > 0) {
            rnd.nextInt();
            --times;
        }
    }

    // ---------------------------------------------------------------------- //

    public StreamComparatorStatistics statistics;

    public void build() {

        if (built) {
            return;
        }

        for(int i=0; i<length; ++i) {
            final Pair<Function<Stream<T>,Stream<T>>,
                       Function<E,E>> transf = getFuns();

            lhs = transf.getLeft().apply(lhs);
            rhs = transf.getRight().apply(rhs);
        }

        built = true;
    }

    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getFuns() {
        final int limit = 7 + (depth > 0 ? 2 : 0);
        switch(rnd.nextInt(limit)) {
            case 0: return getDistinctFuns();
            case 1: return getFilterFuns();
            case 2: return getLimitFuns();
            case 3: return getMapFuns();
            case 4: return getPeekFuns();
            case 5: return getSkipFuns();
            case 6: return getSortedFuns();
            /* deep */
            case 7: return getConcatFuns();
            case 8: return getFlatMapFuns();
            default: assert false; return null;
        }
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getDistinctFuns() {
        final Function<Stream<T>,Stream<T>> lhs = s -> s.distinct();
        final Function<E,E> rhs = e -> distinct(e);
        if (statistics != null) {
            statistics.distinct();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getFilterFuns() {
        final int seed = rnd.nextInt();
        final Predicate<T> filter = predicateOfSeed.apply(seed);
        final Function<Stream<T>,Stream<T>> lhs = s -> s.filter(filter);
        final Function<E,E> rhs = e -> filter(e, filter);
        if (statistics != null) {
            statistics.filter();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getLimitFuns() {
        final int seed = rnd.nextInt();
        final long limit = limitOfSeed.apply(seed);
        final Function<Stream<T>,Stream<T>> lhs = s -> s.limit(limit);
        final Function<E,E> rhs = e -> limit(e, limit);
        if (statistics != null) {
            statistics.limit();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getMapFuns() {
        final int seed = rnd.nextInt();
        final Function<T,T> mapper = mapperOfSeed.apply(seed);
        final Function<Stream<T>,Stream<T>> lhs = s -> s.map(mapper);
        final Function<E,E> rhs = e -> map(e, mapper);
        if (statistics != null) {
            statistics.map();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getPeekFuns() {
        final int seed = rnd.nextInt();
        final Consumer<T> peeker = peekConsumerOfSeed.apply(seed);
        final Function<Stream<T>,Stream<T>> lhs = s -> s.peek(peeker);
        final Function<E,E> rhs = e -> peek(e, peeker);
        if (statistics != null) {
            statistics.peek();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getSkipFuns() {
        final int seed = rnd.nextInt();
        final long n = this.skipLimitOfSeed.apply(seed);
        final Function<Stream<T>,Stream<T>> lhs = s -> s.skip(n);
        final Function<E,E> rhs = e -> skip(e, n);
        if (statistics != null) {
            statistics.skip();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getSortedFuns() {
        final Function<Stream<T>,Stream<T>> lhs = s -> s.sorted();
        final Function<E,E> rhs = e -> sorted(e);
        if (statistics != null) {
            statistics.sorted();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getConcatFuns() {
        rndSpin();
        final StreamComparator<T,E> sub =
                subComparator(new Random(rnd.nextInt()), depth-1, length);
        sub.statistics = statistics;
        sub.build();
        rndSpin();

        final Function<Stream<T>,Stream<T>> lhs = s ->
                Stream.concat(s.limit(length/2), sub.lhs.skip(length/2));
        final Function<E,E> rhs = e ->
                concat(limit(e, length/2), skip(sub.rhs, length/2));
        if (statistics != null) {
            statistics.concat();
        }
        return Pair.of(lhs, rhs);
    }
    private Pair<Function<Stream<T>,Stream<T>>,
                 Function<E,E>> getFlatMapFuns() {
        rndSpin();
        final int seed = rnd.nextInt();
        final Function<Stream<T>,Stream<T>> lhs = s -> s.flatMap(
                x -> {
                    final StreamComparator<T,E> sub =
                            subComparator(new Random(seed),
                                          depth-1,
                                          length);
                    sub.statistics = statistics;
                    sub.build();
                    return sub.lhs.limit(2);
                });
        final Function<E,E> rhs = e -> flatMap(e,
                x -> {
                    final StreamComparator<T,E> sub =
                            subComparator(new Random(seed),
                                          depth-1,
                                          length);
                    sub.build();
                    return limit(sub.rhs, 2);
                });
        rndSpin();
        if (statistics != null) {
            statistics.flatMap();
        }
        return Pair.of(lhs, rhs);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  //

    public void test(BiConsumer<T,Optional<T>> elemAssert,
                     Runnable                  lengthMismatchAsserter) {
        if (!built) {
            build();
        }

        lhs.forEach(x ->
            elemAssert.accept(x, Optional.ofNullable(rhsHasNext()
                                                     ? rhsNext()
                                                     : null))
        );
        if (rhsHasNext()) {
            lengthMismatchAsserter.run();
        }
    }

    protected abstract boolean rhsHasNext();
    protected abstract T       rhsNext();

    // ---------------------------------------------------------------------- //

    public IntFunction<Predicate<T>>  predicateOfSeed;
    public IntFunction<Long>          limitOfSeed;
    public IntFunction<Function<T,T>> mapperOfSeed;
    public IntFunction<Consumer<T>>   peekConsumerOfSeed;
    public IntFunction<Long>          skipLimitOfSeed;

    protected abstract StreamComparator<T,E> subComparator(
            Random rnd,
            int maxDepth,
            int maxLength);

    protected abstract E concat(E first, E second);
    protected abstract E distinct(E en);
    protected abstract E filter(E en, Predicate<T> filter);
    protected abstract E flatMap(E en, Function<T,E> flatMap);
    protected abstract E limit(E en, long limit);
    protected abstract E map(E en, Function<T,T> mapper);
    protected abstract E peek(E en, Consumer<T> peekConsumer);
    protected abstract E skip(E en, long n);
    protected abstract E sorted(E en);
}
