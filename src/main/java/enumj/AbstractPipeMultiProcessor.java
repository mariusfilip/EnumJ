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

/**
 * Specialised type of {@link AbstractPipeProcessor} that may produce multiple
 * elements for a single input element.
 * @param <T> Type of input element.
 * @param <R> Type of resulted elements.
 */
abstract class AbstractPipeMultiProcessor<T,R>
               extends AbstractPipeProcessor<T,R> {
    /**
     * Constructs a new {@link AbstractPipeMultiProcessor} instance.
     * @param sameSourceNextOnNoValue {@code true} if the processor demands
     * continuing to the next element of the same piped source when
     * {@link #hasOutputValue()} value for {@link #sameSourceNextOnNoValue}.
     * @param hasNextNeedsValue value for {@link #hasNextNeedsValue}.
     */
    protected AbstractPipeMultiProcessor(boolean sameSourceNextOnNoValue,
                                         boolean hasNextNeedsValue) {
        super(sameSourceNextOnNoValue, hasNextNeedsValue);
    }

    /**
     * Gets whether the processor needs a fresh input value to produce output
     * values.
     * @return {@code true} if the processor needs a new input to produce
     * output, {@code false} otherwise.
     */
    abstract boolean needsValue();
}
