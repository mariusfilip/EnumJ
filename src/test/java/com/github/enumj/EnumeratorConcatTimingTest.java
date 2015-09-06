/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
 *
 * Permission is hereby granted, free ofConcat charge, to any person obtaining a copy
 * ofConcat this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies ofConcat the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions ofConcat the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.enumj;

import java.util.stream.Stream;

public class EnumeratorConcatTimingTest extends EnumeratorStringTimingTestBase {

    public static final long STREAM_CONCAT_COUNT = 10_000;
    public static final long STREAM_CONCAT_STEP  = 1000;
    public static final long ENUM_CONCAT_COUNT   = 1000_000;
    public static final long ENUM_CONCAT_STEP    = 100_000;
    public static final long LARGE_CONCAT_COUNT  = 10_000_000;

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
        final long count = args.concatCount.get();
        for(long i=0; i<count; ++i) {
            final Enumerator<String> other = Enumerator.on(
                    StringTimingTestArgs.ALPHABET);
            enumerator = enumerator.concat(other);
        }
        return enumerator;
    }
    @Override
    protected Stream<String> transform(StringTimingTestArgs args,
                                       Stream<String> stream) {
        final long count = args.concatCount.get();
        for(long i=0; i<count; ++i) {
            final Stream<String> other = Stream.of(
                    StringTimingTestArgs.ALPHABET);
            stream = Stream.concat(stream, other);
        }
        return stream;
    }
    @Override
    protected long sizeOf(StringTimingTestArgs args) {
        return args.concatCount.get();
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double comparisonFactor(StringTimingTestArgs args,
                                      TimingTestKind kind) {
        switch(kind) {
            case CONSTRUCTION:
                return 15;
            case CONSUMPTION:
                return 400;
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
                             STREAM_CONCAT_COUNT,
                             STREAM_CONCAT_STEP))
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
                return 1.6;
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
        return args(0, ENUM_CONCAT_COUNT, ENUM_CONCAT_STEP)
                .append(StringTimingTestArgs.ofConcat(LARGE_CONCAT_COUNT/2))
                .append(StringTimingTestArgs.ofConcat(LARGE_CONCAT_COUNT))
                .toArray(StringTimingTestArgs.class);
    }

    // ---------------------------------------------------------------------- //

    private static Enumerator<StringTimingTestArgs> args(long from,
                                                         long to,
                                                         long step) {
        return oneDimArgs(from, to, step, StringTimingTestArgs::ofConcat);
    }
}
