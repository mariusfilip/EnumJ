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
 * Pipe processor that stops enumeration once a limit is reached.
 *
 * @param <E> type of enumerated elements.
 * @see WhilePipeProcessor
 * @see SkipPipeProcessor
 */
final class LimitPipeProcessor<E> extends AbstractPipeProcessor<E,E> {

    private E    value;
    private long size;

    /**
     * Constructs a {@code LimitPipeProcessor} instance.
     * <p>
     * The new {@link LimitPipeProcessor} stores its limit internally.
     * </p>
     *
     * @param maxSize enumeration limit.
     */
    public LimitPipeProcessor(long maxSize) {
        super(false, true);
        Checks.ensureNonNegative(maxSize, Messages.NEGATIVE_ENUMERATOR_SIZE);
        this.size = maxSize;
    }

    @Override
    public void processInputValue(E value) {
        if (size > 0) {
            this.value = value;
        }
    }
    @Override
    public boolean hasOutputValue() {
        return size > 0;
    }
    @Override
    protected E retrieveOutputValue() {
        --size;
        return value;
    }
    @Override
    protected void clearOutputValue() {
        value = null;
    }
    @Override
    public boolean isInactive() {
        return size == 0;
    }
}
