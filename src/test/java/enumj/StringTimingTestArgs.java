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

public class StringTimingTestArgs {

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("{")
                .append("streamLen:").append(streamLen).append(";")
                .append("concatCount:").append(concatCount).append("}");
        return builder.toString();
    }

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final int ALPHABET_LENGTH = ALPHABET.length();

    public final long streamLen;
    public final long concatCount;

    private StringTimingTestArgs(long streamLen,
                                 long concatCount) {
        assert streamLen >= 0;
        assert concatCount >= 0;

        this.streamLen = streamLen;
        this.concatCount = concatCount;
    }

    public static StringTimingTestArgs of(long streamLen,
                                          long concatCount) {
        return new StringTimingTestArgs(streamLen, concatCount);
    }

    public static String stringOfLen(long len) {
        final StringBuilder builder = new StringBuilder();
        while(len > ALPHABET_LENGTH) {
            builder.append(ALPHABET);
            len -= ALPHABET_LENGTH;
        }
        if (len > 0) {
            builder.append(ALPHABET.substring(0, (int)len));
        }
        return builder.toString();
    }
}
