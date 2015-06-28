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
import java.util.function.Function;
import java.util.stream.Stream;

public class EnumeratorSquareMapTimingTest
             extends EnumeratorStringTimingTestBase {

    public static final long ENUM_SQUARE_MAP_COUNT = 10_000_000;
    public static final long ENUM_SQUARE_MAP_STEP = 2000_000;
    public static final long LARGE_SQUARE_MAP_COUNT = 100_000_000;

    private final Function<String,String> mapper = Function.identity();

    @Override
    protected Enumerator<String> enumerator(StringTimingTestArgs args) {
        return Enumerator.of(getArray(args));
    }
    @Override
    protected Stream<String> stream(StringTimingTestArgs args) {
        return Arrays.stream(getArray(args));
    }
    @Override
    protected Enumerator<String> transform(StringTimingTestArgs args,
                                           Enumerator<String> enumerator) {
        final long count = args.squareMapCount.get();
        for(long i=0; i<count; ++i) {
            enumerator = enumerator.map(mapper);
        }
        return enumerator;
    }
    @Override
    protected Stream<String> transform(StringTimingTestArgs args,
                                       Stream<String> stream) {
        final long count = args.squareMapCount.get();
        for(long i=0; i<count; ++i) {
            stream = stream.map(mapper);
        }
        return stream;
    }
    @Override
    protected long sizeOf(StringTimingTestArgs args) {
        final long count = args.squareMapCount.get();
        return count * count;
    }
    private String[] getArray(StringTimingTestArgs args) {
        return Enumerator.repeat(
                StringTimingTestArgs.ALPHABET,
                args.squareMapCount.get()).toArray(String.class);
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double comparisonFactor(StringTimingTestArgs args,
                                      TimingTestKind kind) {
        switch(kind) {
            case CONSTRUCTION:
                return 200;
            case CONSUMPTION:
                return 200;
            case BOTH:
                return comparisonFactor(args, TimingTestKind.CONSTRUCTION)
                       +
                       comparisonFactor(args, TimingTestKind.CONSUMPTION);
            default:
                throw new IllegalArgumentException();
        }
    }
    @Override
    protected StringTimingTestArgs[] comparisonArgs(TimingTestKind kind) {
        return args(0, ENUM_SQUARE_MAP_COUNT, ENUM_SQUARE_MAP_STEP)
                .concat(args(0,
                             LARGE_SQUARE_MAP_COUNT,
                             2*ENUM_SQUARE_MAP_COUNT))
                .toArray(StringTimingTestArgs.class);
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double scalabilityFactor(StringTimingTestArgs args,
                                       TimingTestKind kind) {
        return 0;
    }
    @Override
    protected StringTimingTestArgs[] scalabilityArgs(TimingTestKind kind) {
        return new StringTimingTestArgs[0];
    }

    // ---------------------------------------------------------------------- //

    private static Enumerator<StringTimingTestArgs> args(long from,
                                                         long to,
                                                         long step) {
        final long fromRoot = Math.round(Math.sqrt(from));
        final long toRoot = Math.round(Math.sqrt(to));
        final long count = (to-from)/step;
        final long rootStep = (toRoot - fromRoot)/count;
        return oneDimArgs(fromRoot,
                          toRoot,
                          rootStep,
                          StringTimingTestArgs::ofSquareMap);
    }
}
