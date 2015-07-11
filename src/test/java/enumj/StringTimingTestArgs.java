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

public class StringTimingTestArgs {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final int ALPHABET_LENGTH = ALPHABET.length();

    public final Optional<Long> concatCount;
    public final Optional<Long> deepMapCount;
    public final Optional<Long> wideMapCount;
    public final Optional<Long> deepFlatMapCount;
    public final Optional<Long> wideFlatMapCount;
    public final Optional<Long> innerFlatMapCount;
    public final Optional<Long> deepFilterCount;
    public final Optional<Long> wideFilterCount;
    public final Optional<Long> deepLimitCount;
    public final Optional<Long> wideLimitCount;
    public final Optional<Long> deepPeekCount;
    public final Optional<Long> widePeekCount;
    public final Optional<Long> deepSkipCount;
    public final Optional<Long> wideSkipCount;

    private StringTimingTestArgs(Optional<Long> concatCount,
                                 Optional<Long> deepMapCount,
                                 Optional<Long> wideMapCount,
                                 Optional<Long> deepFlatMapCount,
                                 Optional<Long> wideFlatMapCount,
                                 Optional<Long> innerFlatMapCount,
                                 Optional<Long> deepFilterCount,
                                 Optional<Long> wideFilterCount,
                                 Optional<Long> deepLimitCount,
                                 Optional<Long> wideLimitCount,
                                 Optional<Long> deepPeekCount,
                                 Optional<Long> widePeekCount,
                                 Optional<Long> deepSkipCount,
                                 Optional<Long> wideSkipCount) {
        this.concatCount = concatCount;
        this.deepMapCount = deepMapCount;
        this.wideMapCount = wideMapCount;
        this.deepFlatMapCount = deepFlatMapCount;
        this.wideFlatMapCount = wideFlatMapCount;
        this.innerFlatMapCount = innerFlatMapCount;
        this.deepFilterCount = deepFilterCount;
        this.wideFilterCount = wideFilterCount;
        this.deepLimitCount = deepLimitCount;
        this.wideLimitCount = wideLimitCount;
        this.deepPeekCount = deepPeekCount;
        this.widePeekCount = widePeekCount;
        this.deepSkipCount = deepSkipCount;
        this.wideSkipCount = wideSkipCount;
    }

    public static StringTimingTestArgs ofConcat(long concatCount) {
        return new StringTimingTestArgs(
                Optional.of(concatCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofDeepMap(long deepMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.of(deepMapCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofWideMap(long wideMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.of(wideMapCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofSquareMap(long squareMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.of(squareMapCount),
                Optional.of(squareMapCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofDeepFlatMap(long deepFlatMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(deepFlatMapCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofWideFlatMap(long wideFlatMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(wideFlatMapCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofInnerFlatMap(long deepFlatMapCount,
                                                      long innerFlatMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(deepFlatMapCount),
                Optional.empty(),
                Optional.of(innerFlatMapCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofCubeFlatMap(long deepFlatMapCount,
                                                     long wideFlatMapCount,
                                                     long innerFlatMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(deepFlatMapCount),
                Optional.of(wideFlatMapCount),
                Optional.of(innerFlatMapCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofDeepFilter(long deepFilterCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(deepFilterCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofWideFilter(long wideFilterCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(wideFilterCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofSquareFilter(long squareFilterCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(squareFilterCount),
                Optional.of(squareFilterCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofDeepLimit(long deepLimitCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(deepLimitCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofWideLimit(long wideLimitCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(wideLimitCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofSquareLimit(long squareLimitCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(squareLimitCount),
                Optional.of(squareLimitCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofDeepPeek(long deepPeekCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(deepPeekCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofWidePeek(long widePeekCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(widePeekCount),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofSquarePeek(long squarePeekCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(squarePeekCount),
                Optional.of(squarePeekCount),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofDeepSkip(long deepSkipCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(deepSkipCount),
                Optional.empty());
    }
    public static StringTimingTestArgs ofWideSkip(long wideSkipCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(wideSkipCount));
    }
    public static StringTimingTestArgs ofSquareSkip(long squareSkipCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(squareSkipCount),
                Optional.of(squareSkipCount));
    }
}
