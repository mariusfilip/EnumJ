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

/**
 * Type of {@link IteratorEnumerator} that acts as a source of enumerated
 * elements for {@link AbstractPipeProcessor} instances within
 * {@link PipeEnumerator#pipeline}.
 * @param <E> type of enumerated elements.
 */
class PipeSource<E> extends IteratorEnumerator<E> {

    private AbstractPipeProcessor<?,?> firstProcessor;

    /**
     * Constructs a new {@link PipeSource} instances based on the given
     * {@code source}.
     * @param source {@link Iterator} supplying enumerated elements.
     */
    protected PipeSource(Iterator<E> source) {
        super(source);
    }

    /**
     * Gets the first {@link AbstractPipeProcessor} instance that processes the
     * elements of this {@link PipeSource}
     * @return {@link AbstractPipeProcessor} instance.
     */
    public AbstractPipeProcessor<?,?> getFirstProcessor() {
        return this.firstProcessor;
    }
    /**
     * Sets {@link #firstProcessor} to the given {@link AbstractPipeProcessor}
     * if there is none.
     * @param processor value for {@link #firstProcessor} is none.
     */
    public void setFirstProcessorIfNone(AbstractPipeProcessor<?,?> processor) {
        if (this.firstProcessor == null) {
            this.firstProcessor = processor;
        }
    }

    /**
     * Creates a new instance of {@link PipeSource} with the given
     * {@code source}.
     * @param <T> type of enumerated elements.
     * @param source {@link Iterator} instance acting as a source of elements.
     * @return {@link PipeSource} instance.
     */
    static <T> PipeSource<T> of(Iterator<T> source) {
        return new PipeSource(source);
    }
}
