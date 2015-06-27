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

public class EnumeratorWideMapTimigTest
             extends EnumeratorStringTimingTestBase {

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
        return enumerator.map(mapper);
    }
    @Override
    protected Stream<String> transform(StringTimingTestArgs args,
                                       Stream<String> stream) {
        return stream.map(mapper);
    }
    @Override
    protected long sizeOf(StringTimingTestArgs args) {
        return args.wideMapCount.get();
    }
    private String[] getArray(StringTimingTestArgs args) {
        return Enumerator.repeat(
                StringTimingTestArgs.ALPHABET,
                args.wideMapCount.get()).toArray(String.class);
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double comparisonFactor(StringTimingTestArgs args,
                                      TimingTestKind kind) {
        return 1000;
    }
    @Override
    protected StringTimingTestArgs[] comparisonArgs(TimingTestKind kind) {
        if (kind == TimingTestKind.CONSTRUCTION)
        {
            return new StringTimingTestArgs[0];
        }
        return args(0, 10, 1)
                .concat(args(10, 100, 10))
                .concat(args(100, 1000, 100))
                .concat(args(1000, 10_000, 1000))
                .concat(args(10_000, 100_000, 10_000))
                .concat(args(100_000, 1000_000, 100_000))
                .toArray(StringTimingTestArgs.class);
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected double scalabilityFactor(StringTimingTestArgs args,
                                       TimingTestKind kind) {
        return 1;
    }
    @Override
    protected StringTimingTestArgs[] scalabilityArgs(TimingTestKind kind) {
        return new StringTimingTestArgs[0];
    }

    // ---------------------------------------------------------------------- //

    private static Enumerator<StringTimingTestArgs> args(long from,
                                                         long to,
                                                         long step) {
        return oneDimArgs(from, to, step, StringTimingTestArgs::ofWideMap);
    }
}
