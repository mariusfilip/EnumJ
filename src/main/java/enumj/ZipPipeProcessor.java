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

public class ZipPipeProcessor<E> extends PipeProcessor<Optional<E>,
                                                       Optional<E>[]> {
    
    protected Iterator<E>[] iterators;
    protected Optional<E>[] value;
    protected boolean hasAny;
    
    public ZipPipeProcessor(Iterator<E> first,
                            Iterator<E>... rest) {
        iterators = new Iterator[1+rest.length];
        value = new Optional[1+iterators.length];
        iterators[0] = first;
        for(int i=1; i<iterators.length; ++i) {
            iterators[i] = rest[i-1];
        }
    }

    @Override
    protected void process(Optional<E> value) {
        this.value[0] = value;
        hasAny = value.isPresent();
        for(int i=0; i<this.iterators.length; ++i) {
            if (this.iterators[i].hasNext()) {
                hasAny = true;
                this.value[i+1] = Optional.of(this.iterators[i].next());
            }
            else {
                this.value[i+1] = Optional.empty();
            }
        }
    }
    @Override
    protected boolean hasValue() {
        return hasAny;
    }
    @Override
    protected Optional<E>[] getValue() {
        return value;
    }
    @Override
    protected boolean nextOnNoValue() {
        return false;
    }
}
