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
import java.util.stream.Stream;

public class EnumeratorConcatTimingTest extends EnumeratorStringTimingTestBase {

    public static final long STREAM_CONCAT_COUNT = 10000;
    public static final long STREAM_CONCAT_STEP = 1000;
    public static final long ENUM_CONCAT_COUNT = 1000000;
    public static final long ENUM_CONCAT_STEP = 100000;
    public static final long LARGE_CONCAT_COUNT = 10000000;

    @Override
    protected Enumerator<String> enumerator(StringTimingTestArgs args) {
        assert args.streamLen == 1;
        return Enumerator.on(StringTimingTestArgs.ALPHABET);
    }
    @Override
    protected Stream<String> stream(StringTimingTestArgs args) {
        assert args.streamLen == 1;
        return Stream.of(StringTimingTestArgs.ALPHABET);
    }
    @Override
    protected Enumerator<String> transform(StringTimingTestArgs args,
                                           Enumerator<String> enumerator) {
        for(long i=0; i<args.concatCount; ++i) {
            final Enumerator<String> other = Enumerator.on(
                    StringTimingTestArgs.ALPHABET);
            enumerator = enumerator.concat(other);
        }
        return enumerator;
    }
    @Override
    protected Stream<String> transform(StringTimingTestArgs args,
                                       Stream<String> stream) {
        for(long i=0; i<args.concatCount; ++i) {
            final Stream<String> other = Stream.of(
                    StringTimingTestArgs.ALPHABET);
            stream = Stream.concat(stream, other);
        }
        return stream;
    }
    @Override
    protected long sizeOf(StringTimingTestArgs args) {
        return args.concatCount;
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double comparisonConstructionFactor(StringTimingTestArgs args) {
        return 4;
    }
    @Override
    protected double comparisonConsumptionFactor(StringTimingTestArgs args) {
        return 400;
    }
    @Override
    protected StringTimingTestArgs[] comparisonArgs() {
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
    protected double scalabilityConstructionFactor(StringTimingTestArgs args) {
        return 100;
    }
    @Override
    protected double scalabilityConsumptionFactor(StringTimingTestArgs args) {
        return 1.6;
    }

    @Override
    protected StringTimingTestArgs[] scalabilityArgs() {
        return args(0, ENUM_CONCAT_COUNT, ENUM_CONCAT_STEP)
                .append(StringTimingTestArgs.of(1,
                                                LARGE_CONCAT_COUNT/2))
                .append(StringTimingTestArgs.of(1,
                                                LARGE_CONCAT_COUNT))
                .toArray(StringTimingTestArgs.class);
    }

    private static Enumerator<StringTimingTestArgs> args(long from,
                                                         long to,
                                                         long step) {
        return Enumerator
                .rangeClosed(from, to, x -> x+step, Long::compare)
                .filter(x -> x != from)
                .map(c -> StringTimingTestArgs.of(1, c));
    }
}
