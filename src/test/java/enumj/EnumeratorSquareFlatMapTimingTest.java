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

/**
 *
 * @author Marius Filip
 */
public class EnumeratorSquareFlatMapTimingTest
             extends EnumeratorStringTimingTestBase {

    public final static long[] powers = {
        1,
        3,
        5,
        7,
        11,
        13
    };
    public final static long[] sizes = new long[powers.length];

    static {
        for(int i=0; i<sizes.length; ++i) {
            sizes[i] = (long)Math.floor(Math.pow(2, powers[i]));
        }
    }

    {
        buildBuffers(sizes);
    }

    private final Function<String,Enumerator<String>> enumeratorMapper =
            s -> Enumerator.on(s, s);
    private final Function<String,Stream<String>> streamMapper =
            s -> Stream.of(s, s);

    @Override
    protected Enumerator<String> enumerator(StringTimingTestArgs args) {
        return Enumerator.of(buffers.get(args.wideFlatMapCount.get()));
    }
    @Override
    protected Stream<String> stream(StringTimingTestArgs args) {
        return Arrays.stream(buffers.get(args.wideFlatMapCount.get()));
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
        assert args.innerFlatMapCount.get() == 2;
        final long wide = args.wideFlatMapCount.get();
        final long inner = 
                (long)Math.floor(Math.pow(args.innerFlatMapCount.get(),
                                          args.deepFlatMapCount.get()));
        return wide * inner;
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double comparisonFactor(StringTimingTestArgs args,
                                      TimingTestKind kind) {
        return Integer.MAX_VALUE;
    }
    @Override
    protected StringTimingTestArgs[] comparisonArgs(TimingTestKind kind) {
        final StringTimingTestArgs[] args =
                new StringTimingTestArgs[sizes.length];
        for(int i=0; i<args.length; ++i) {
            args[i] = StringTimingTestArgs.ofCubeFlatMap(powers[i],
                                                         sizes[i],
                                                         2);
        }
        return args;
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
}
