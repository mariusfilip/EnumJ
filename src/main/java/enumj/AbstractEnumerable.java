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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Marius Filip
 */
abstract class AbstractEnumerable<E> implements Enumerable<E> {

    private AtomicBoolean enumerating = new AtomicBoolean(false);

    @Override
    public final boolean enumerating() {
        return enumerating.get();
    }

    @Override
    public final Enumerator<E> enumerator() {
        boolean previous = !enumerating.compareAndSet(false, true);
        try {
            return internalEnumerator();
        } catch(Exception ex) {
            enumerating.set(previous);
            throw ex;
        }
    }

    @Override
    public final Enumerable<E> clone() {
        Utils.ensureNonEnumerating(this);
        Utils.ensureCloneable(this);
        AbstractEnumerable<E> result = internalNewClone();
        result.internalCopyClone(this);
        return result;
    }
    @Override
    public final boolean cloneable() {
        return internalCloneable();
    }

    protected abstract Enumerator<E> internalEnumerator();
    protected abstract AbstractEnumerable<E> internalNewClone();
    protected abstract void internalCopyClone(AbstractEnumerable<E> source);
    protected abstract boolean internalCloneable();
}
