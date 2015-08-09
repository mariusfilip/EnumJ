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

import static enumj.EnumeratorStringTimingTestBase.oneDimArgs;
import java.util.function.Function;
import java.util.stream.Stream;

public class EnumeratorDeepFlatMapTimingTest
             extends EnumeratorStringTimingTestBase {

    public static final long STREAM_DEEP_FLATMAP_COUNT = 1000;
    public static final long STREAM_DEEP_FLATMAP_STEP = 100;
    public static final long ENUM_DEEP_FLATMAP_COUNT = 1000_000;
    public static final long ENUM_DEEP_FLATMAP_STEP = 100_000;
    public static final long LARGE_DEEP_FLATMAP_COUNT = 10_000_000;

    private final Function<String,Enumerator<String>> enumeratorMapper =
            s -> Enumerator.on(s);
    private final Function<String,Stream<String>> streamMapper =
            s -> Stream.of(s);

    @Override
    protected Enumerator<String> enumerator(StringTimingTestArgs args) {
        return Enumerator.on(StringTimingTestArgs.ALPHABET);
    }
    @Override
    protected Stream<String> stream(StringTimingTestArgs args) {
        return Stream.of(StringTimingTestArgs.ALPHABET);
    }
    @Override
    protected Enumerator<String> transform(StringTimingTestArgs args,
                                           Enumerator<String> enumerator) {
        final long count = args.deepFlatMapCount.get();
        for(long i=0; i<count; ++i) {
            enumerator = enumerator.flatMap(enumeratorMapper);
        }
        return enumerator;
    }
    @Override
    protected Stream<String> transform(StringTimingTestArgs args,
                                       Stream<String> stream) {
        final long count = args.deepFlatMapCount.get();
        for(long i=0; i<count; ++i) {
            stream = stream.flatMap(streamMapper);
        }
        return stream;
    }
    @Override
    protected long sizeOf(StringTimingTestArgs args) {
        return args.deepFlatMapCount.get();
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
        return args(0, 10, 1)
                .concat(args(10, 100, 10))
                .concat(args(100, 1000, 100))
                .concat(args(1000,
                             STREAM_DEEP_FLATMAP_COUNT,
                             STREAM_DEEP_FLATMAP_STEP))
                .toArray(StringTimingTestArgs.class);
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double scalabilityFactor(StringTimingTestArgs args,
                                       TimingTestKind kind) {
        switch(kind) {
            case CONSTRUCTION:
                return 100;
            case CONSUMPTION:
                return 1.6*16;
            case BOTH:
                return scalabilityFactor(args, TimingTestKind.CONSTRUCTION)
                       +
                       scalabilityFactor(args, TimingTestKind.CONSUMPTION);
            default:
                throw new IllegalArgumentException();
        }
    }
    @Override
    protected StringTimingTestArgs[] scalabilityArgs(TimingTestKind kind) {
        return args(0, ENUM_DEEP_FLATMAP_COUNT, ENUM_DEEP_FLATMAP_STEP)
                .append(StringTimingTestArgs
                        .ofDeepFlatMap(LARGE_DEEP_FLATMAP_COUNT/2))
                .append(StringTimingTestArgs
                        .ofDeepFlatMap(LARGE_DEEP_FLATMAP_COUNT))
                .toArray(StringTimingTestArgs.class);
    }

    // ---------------------------------------------------------------------- //

    private static Enumerator<StringTimingTestArgs> args(long from,
                                                         long to,
                                                         long step) {
        return oneDimArgs(from, to, step, StringTimingTestArgs::ofDeepFlatMap);
    }
}
