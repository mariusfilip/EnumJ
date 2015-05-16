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
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;

class CartesianProductPipeProcessor<In,T> extends
                                          PipeMultiProcessor<In,Pair<In,T>> {

    protected final Supplier<Iterator<T>> source;
    protected final Nullable<Pair<In,T>> value;
    
    protected In in;
    protected Iterator<T> iterator;
    
    CartesianProductPipeProcessor(Supplier<Iterator<T>> source) {
        this.in = null;
        this.source = source;
        this.iterator = null;
        this.value = Nullable.empty();
    }

    @Override
    protected boolean needsValue() {
        return !value.isPresent();
    }
    @Override
    protected void process(In value) {
        in = value;
        iterator = source.get();
        if (iterator.hasNext()) {
            this.value.set(Pair.of(in, iterator.next()));
        } else {
            this.value.clear();
        }
    }
    @Override
    protected boolean hasValue() {
        return value.isPresent();
    }
    @Override
    protected Pair<In,T> getValue() {
        Pair<In,T> result = value.get();
        value.clear();
        if (iterator.hasNext()) {
            value.set(Pair.of(in, iterator.next()));
        }
        return result;
    }
    @Override
    protected boolean nextOnNoValue() {
        return true;
    }
}
