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
 * Pipe processor that skips over a number of elements.
 *
 * @param <E> type of enumerated elements.
 * @see SkipWhilePipeProcessor
 * @see WhilePipeProcessor
 */
final class SkipPipeProcessor<E> extends AbstractPipeProcessor<E,E> {

    private final InOut<E> value;
    private       long     n;

    /**
     * Constructs a {@code SkipPipeProcessor} that skips over {@code n}
     * elements.
     * <p>
     * The new {@link SkipPipeProcessor} stores its limit internally.
     * </p>
     *
     * @param n number of elements to skip.
     */
    public SkipPipeProcessor(long n) {
        super(true, true);
        Checks.ensureNonNegative(n, Messages.NEGATIVE_ENUMERATOR_SIZE);
        this.value = new InOut<>();
        this.n = n;
    }

    @Override
    public void processInputValue(In<E> value) {
        if (n == 0) {
            this.value.setValue(value);
        }
    }
    @Override
    public boolean hasOutputValue() {
        if (n == 0) {
            return true;
        }
        --n;
        return false;
    }
    @Override
    public void getOutputValue(Out<E> value) {
        value.setValue(this.value);
        this.value.clear();
    }
    @Override
    public boolean isInactive() {
        return false;
    }
}
