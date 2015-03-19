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

import java.util.Iterator;
import java.util.Optional;

public class ZipAllEnumerator<E> extends AbstractEnumerator<Optional<E>[]> {

    private Iterator<E>[] sources;
    private int index;

    public ZipAllEnumerator(Iterator<E> first,
                            Iterator<? extends E> second,
                            Iterator<? extends E>... rest) {
        Utils.ensureNotNull(first, Messages.NULL_ENUMERATOR_SOURCE);
        Utils.ensureNotNull(second, Messages.NULL_ENUMERATOR_SOURCE);
        for(Iterator<? extends E> it: rest) {
            Utils.ensureNotNull(it, Messages.NULL_ENUMERATOR_SOURCE);
        }
        sources = new Iterator[2+rest.length];
        sources[0] = first;
        sources[1] = (Iterator<E>)second;
        for(int i=2; i<sources.length; ++i) {
            sources[i] = (Iterator<E>)rest[i-2];
        }
    }

    @Override
    protected boolean internalHasNext() {
        int count = sources.length;
        while (count > 0 && !sources[index].hasNext()) {
            index = (index+1)%sources.length;
            --count;
        }
        return count > 0;
    }

    @Override
    protected Optional<E>[] internalNext() {
        Optional<E>[] result = new Optional[sources.length];
        for(int i=0; i<sources.length; ++i) {
            Iterator<E> it = sources[i];
            result[i] = it.hasNext()
                        ? Optional.of(it.next())
                        : Optional.empty();
        }
        return result;
    }
    
    @Override
    protected void cleanup() {
        sources = null;
    }
}
