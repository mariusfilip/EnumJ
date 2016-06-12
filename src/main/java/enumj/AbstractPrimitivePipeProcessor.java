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

import java.lang.ref.WeakReference;
import java.util.Optional;

abstract class AbstractPrimitivePipeProcessor<T, R>
         extends AbstractPipeProcessor<T, R> {

    protected AbstractPrimitivePipeProcessor(
            boolean  nextOnSameSourceOnNoValue,
            boolean  hasNextNeedsValue) {
        super(nextOnSameSourceOnNoValue, hasNextNeedsValue);
    }
    
    private WeakReference<AbstractPrimitivePipeProcessor<?,? extends T>>
                    prev;
    private boolean prevSet;
    private boolean primitiveNextIsSet;
    private boolean nextIsPrimitive;
    
    public final Optional<AbstractPrimitivePipeProcessor<?,? extends T>>
                     getPrev() {
        return Optional.ofNullable(prev.get());
    }
    public final void setPrev(AbstractPipeProcessor<?,? extends T> prev) {
        if (!prevSet) {
            if (prev instanceof AbstractPrimitivePipeProcessor) {
                final AbstractPrimitivePipeProcessor p =
                        (AbstractPrimitivePipeProcessor)prev;
                this.prev = new WeakReference<>(p);
            }
            prevSet = true;
        } else {
            throw new UnsupportedOperationException();
        }
    }
    private boolean nextIsPrimitive() {
        if (primitiveNextIsSet) { return nextIsPrimitive; }
        if (super.getNext() != null) {
            nextIsPrimitive =
                    super.getNext() instanceof AbstractPrimitivePipeProcessor;
            primitiveNextIsSet = true;
        }
        return false;
    }

    // ---------------------------------------------------------------------- //

    @Override
    public    final void processInputValue(Value<T> value) {
        final AbstractPrimitivePipeProcessor p =
                prev != null ? prev.get() : null;
        if (p != null) {
            processPrimitiveInputValue(value);
        } else {
            processGenericInputValue(value);
        }
    }
    @Override
    protected final R    retrieveOutputValue() {
        return nextIsPrimitive() ? null : retrieveGenericOutputValue();
    }
    @Override
    protected final void clearOutputValue() {}
    @Override
    protected final void detachPrevious() {
        if (prev != null) {
            prev.clear();
        }
    }

    protected abstract void processPrimitiveInputValue(Value<T> value);
    protected abstract void processGenericInputValue(Value<T> value);
    protected abstract R retrieveGenericOutputValue();
}
