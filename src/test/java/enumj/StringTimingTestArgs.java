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
package enumj;

import java.util.Optional;

public class StringTimingTestArgs {

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("{")
                .append("concatCount:").append(concatCount).append(";")
                .append("deepMapCount:").append(deepMapCount).append(";")
                .append("wideMapCount:").append(wideMapCount).append(";")
                .append("squareMapCount:").append(squareMapCount).append("}");
        return builder.toString();
    }

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final int ALPHABET_LENGTH = ALPHABET.length();

    public final Optional<Long> concatCount;
    public final Optional<Long> deepMapCount;
    public final Optional<Long> wideMapCount;
    public final Optional<Long> squareMapCount;
    

    private StringTimingTestArgs(Optional<Long> concatCount,
                                 Optional<Long> deepMapCount,
                                 Optional<Long> wideMapCount,
                                 Optional<Long> squareMapCount) {
        this.concatCount = concatCount;
        this.deepMapCount = deepMapCount;
        this.wideMapCount = wideMapCount;
        this.squareMapCount = squareMapCount;
    }

    public static StringTimingTestArgs ofConcat(long concatCount) {
        return new StringTimingTestArgs(
                Optional.of(concatCount),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofDeepMap(long deepMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.of(deepMapCount),
                Optional.empty(),
                Optional.empty());
    }
    public static StringTimingTestArgs ofWideMap(long wideMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.of(wideMapCount),
                Optional.empty());
    }
    public static StringTimingTestArgs ofSquareMap(long squareMapCount) {
        return new StringTimingTestArgs(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(squareMapCount));
    }
}
