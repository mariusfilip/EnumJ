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

abstract class AbstractPipeProcessor<T,R> {
    private AbstractPipeProcessor<? extends R,?> next;
    private PipeReference reference;

    public AbstractPipeProcessor<? extends R,?> getNext() {
        return next;
    }
    public void setNext(AbstractPipeProcessor<? extends R,?> next) {
        assert next != null;
        assert this.next == null;
        this.next = next;
    }

    public PipeReference getReference() {
        return reference;
    }
    public void setReference(PipeReference reference) {
        assert reference != null;
        assert this.reference == null;
        this.reference = reference;
    }

    protected AbstractPipeProcessor() {}
    abstract void process(T value);
    abstract boolean hasValue();
    abstract R getValue();
    abstract boolean nextOnNoValue();
    abstract boolean hasNextNeedsValue();
}
