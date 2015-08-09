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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class EnumeratorStringTimingTestBase
                extends EnumeratorTimingTestBase<StringTimingTestArgs,String> {

    @Override
    protected void consumeEnumerator(String element) {}
    @Override
    protected void consumeStream(String element) {}

    protected static Enumerator<StringTimingTestArgs> oneDimArgs(
            long from, long to, long step,
            Function<Long,StringTimingTestArgs> builder) {
        return Enumerator
                .rangeClosed(from, to, x -> x+step, Long::compare)
                .filter(x -> x != from)
                .map(builder);
    }

    // ---------------------------------------------------------------------- //

    protected final Map<Long, String[]> buffers = new HashMap<Long, String[]>();

    protected void buildBuffers(long[] sizes) {
        buffers.clear();
        for(long size: sizes) {
            final String[] buffer = new String[(int)size];
            for(int i=0; i<size; ++i) {
                buffer[i] = StringTimingTestArgs.ALPHABET;
            }
            buffers.put(size, buffer);
        }
    }
}
