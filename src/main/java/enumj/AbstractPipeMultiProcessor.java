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
 * Pipe processor that may produce multiple output elements for an input
 * element.
 *
 * @param <T> Type of input element.
 * @param <R> Type of resulted elements.
 * @see AbstractPipeProcessor
 * @see PipeEnumerator
 */
abstract class AbstractPipeMultiProcessor<T,R>
               extends AbstractPipeProcessor<T,R> {
    /**
     * Constructs a new {@code AbstractPipeMultiProcessor} instance.
     *
     * @param nextOnSameSourceOnNoValue value for
     * {@link #nextOnSameSourceOnNoValue}.
     * @param hasNextNeedsValue value for {@link #hasNextNeedsValue}.
     * @see AbstractPipeProcessor
     */
    protected AbstractPipeMultiProcessor(boolean nextOnSameSourceOnNoValue,
                                         boolean hasNextNeedsValue) {
        super(nextOnSameSourceOnNoValue, hasNextNeedsValue);
    }

    /**
     * Gets whether the processor needs a fresh input value to produce output
     * values.
     * <p>
     * Unlike {@link AbstractPipeProcessor}, this type of processor may yield
     * more than one output for a single input. This means feeding in input
     * values must be put on hold as long as the current processor still has
     * output values to provide. This state ends when {@code needsValue} returns
     * true.
     * </p>
     *
     * @return true if the processor needs a new input to produce outputs, false
     * otherwise.
     * @see AbstractPipeProcessor
     */
    public abstract boolean needsValue();
}
