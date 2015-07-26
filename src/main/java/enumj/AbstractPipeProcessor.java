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
 * Processing unit in the highly composable pipeline of {@link PipeEnumerator}.
 *
 * @param <T> Type of element being processed.
 * @param <R> Type of processed result.
 */
abstract class AbstractPipeProcessor<T,R> {

    /**
     * Represents whether the pipeline should proceed to the next element
     * of the same source when {@link #hasOutputValue()} returns {@code false}.
     */
    public final boolean nextOnSameSourceOnNoValue;
    /**
     * Represents whether the processor needs a value fed in when participating
     * to the outcome of {@link PipeEnumerator#hasNext()}.
     */
    public final boolean hasNextNeedsValue;

    /**
     * Constructs a new instance of {@link AbstractPipeProcessor}.
     *
     * @param nextOnSameSourceOnNoValue value for
     * {@link #nextOnSameSourceOnNoValue}.
     * @param hasNextNeedsValue value for {@link #hasNextNeedsValue}.
     */
    protected AbstractPipeProcessor(boolean nextOnSameSourceOnNoValue,
                                    boolean hasNextNeedsValue) {
        this.nextOnSameSourceOnNoValue =
                nextOnSameSourceOnNoValue;
        this.hasNextNeedsValue = hasNextNeedsValue;
    }

    // ---------------------------------------------------------------------- //

    /**
     * Link to the next processor in the pipeline.
     */
    private AbstractPipeProcessor<? extends R,?> next;
    /**
     * {@link PipeSource} instance which represents the source of data that is
     * being processed starting with the current processor in the pipeline.
     */
    private PipeSource source;

    /**
     * Returns the value of {@link #next}.
     * @return Value of {@link #next}.
     */
    public AbstractPipeProcessor<? extends R,?> getNext() {
        return next;
    }
    /**
     * Sets the value of {@link #next}.
     * <p>
     * Once {@link next} is set to a non-null value, it cannot change.
     * </p>
     * @param next Value for {@link #next}.
     */
    public void setNext(AbstractPipeProcessor<? extends R,?> next) {
        if (this.next == null) {
            this.next = next;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Gets the value of {@link #source}.
     * @return Value of {@link #source}.
     */
    public PipeSource getSource() {
        return source;
    }
    /**
     * Sets the value of {@link #source}.
     * <p>
     * Once {@link #source} is set to a non-null value, it cannot change.
     * </p>
     * @param reference Value for {@link #source}.
     */
    public void setSource(PipeSource reference) {
        if (this.source == null) {
            this.source = reference;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Processes an input value and returns the result, if any.
     * <p>
     * The result of processing must be stored internally.
     * </p>
     * @param value Value to process.
     */
    abstract void    processInputValue(T value);
    /**
     * Gets whether processing was successful and the processor has a value
     * to yield.
     * <p>
     * If {@link #hasOutputValue()} returns {@code false} than no internal
     * storage of input or output should take place.
     * </p>
     * @return {@code true} if there is an output value to yield, {@code false}
     * otherwise.
     * @see #processInputValue(java.lang.Object)
     * @see #getOutputValue()
     */
    abstract boolean hasOutputValue();
    /**
     * Gets the processed result produced by
     * {@link #processInputValue(java.lang.Object)} and clears the internal
     * storage of it.
     * @return Processed result.
     * @see #processInputValue(Object)
     * @see #hasOutputValue()
     * @see #retrieveOutputValue()
     * @see #clearOutputValue()
     */
    final R getOutputValue() {
        final R result = retrieveOutputValue();
        clearOutputValue();
        return result;
    }
    /**
     * Retrieves the processed result produced by
     * {@link #processInputValue(Object)}.
     * @return Processed result.
     * @see #getOutputValue()
     * @see #clearOutputValue()
     */
    protected abstract R retrieveOutputValue();
    /**
     * Clears the processed result after being returned by
     * {@link #retrieveOutputValue()}.
     */
    protected abstract void clearOutputValue();
    /**
     * Gets whether the processor does not process any more and the pipeline
     * up to it should be discarded.
     * <p>
     * {@link #isInactive()} returning {@code true} is equivalent to the
     * situation when no matter with what value
     * {@link #processInputValue(Object)} will be called,
     * {@link #hasOutputValue()} will always return {@code false}.
     * </p>
     * <p>
     * Most processors have this function return {@code false} with the
     * exception of {@code while} and {@code limit} compositions. These will
     * turn completely inactive once a certain condition is met.
     * </p>
     * @return {@code true} if processing must stop, {@code false} otherwise.
     */
    abstract boolean isInactive();
}
