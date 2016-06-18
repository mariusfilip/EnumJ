/*
 * The MIT License
 *
 * Copyright 2016 Marius Filip.
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

abstract class PipeSource<E> extends IteratorEnumerator<E> {

    private AbstractPipeProcessor<?,?> firstProcessor;

    protected PipeSource(Iterator<E> source) { super(source); }

    public final AbstractPipeProcessor<?,?> getFirstProcessor() {
        return firstProcessor;
    }
    public final void setFirstProcessorIfNone(
            AbstractPipeProcessor<?,?> processor) {
        if (firstProcessor == null) { firstProcessor = processor; }
    }
    
    public final void yieldNext(Out<? super E> value) {
        internalYieldNext(value);
    }
    protected abstract void internalYieldNext(Out<? super E> value);
}
