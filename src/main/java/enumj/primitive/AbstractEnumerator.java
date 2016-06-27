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
package enumj.primitive;

import java.util.NoSuchElementException;

abstract class AbstractEnumerator implements ExtendedIterator {

    private boolean started;
    private boolean hasNextHasBeenCalled;
    private boolean hasNextHasThrown;
    private boolean done;

    protected AbstractEnumerator() {
        // do nothing
    }

    public final boolean enumerating() {
        return started;
    }
    @Override
    public final boolean hasNext() {
        if (done) {
            return false;
        }

        started = true;
        done = !safeHasNext();
        if (done) {
            safeCleanup();
        }

        hasNextHasThrown = false;
        hasNextHasBeenCalled = true;
        return !done;
    }
    @Override
    public final int nextInt() {
        safeNext();
        return internalNextInt();
    }
    @Override
    public final long nextLong() {
        safeNext();
        return internalNextLong();
    }
    @Override
    public final double nextDouble() {
        safeNext();
        return internalNextDouble();
    }

    private boolean safeHasNext() {
        try {
            return internalHasNext();
        } catch(Throwable err) {
            hasNextHasThrown = true;
            hasNextHasBeenCalled = true;
            throw err;
        }
    }
    private void safeNext() {
        if (!hasNextHasBeenCalled) {
            hasNext();
        }
        if (done || hasNextHasThrown) {
            throw new NoSuchElementException();
        }
        try {
            moveToNext();
        } finally {
            hasNextHasBeenCalled = false;
        }
    }
    private void safeCleanup() {
        try {
            cleanup();
        } catch(Throwable err) {
            hasNextHasThrown = true;
            hasNextHasBeenCalled = true;
            throw err;
        }
    }

    protected abstract boolean internalHasNext();
    protected abstract void moveToNext();
    protected abstract int internalNextInt();
    protected abstract long internalNextLong();
    protected abstract double internalNextDouble();
    protected void cleanup() {}
}

abstract class AbstractIntEnumerator extends AbstractEnumerator
                                     implements PrimitiveEnumerator.OfInt {
    @Override
    protected final long internalNextLong() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected final double internalNextDouble() {
        throw new UnsupportedOperationException();
    }
}
abstract class AbstractLongEnumerator extends AbstractEnumerator
                                      implements PrimitiveEnumerator.OfLong {
    @Override
    protected final int internalNextInt() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected final double internalNextDouble() {
        throw new UnsupportedOperationException();
    }
}
abstract class AbstractDoubleEnumerator
               extends AbstractEnumerator
               implements PrimitiveEnumerator.OfDouble {
    @Override
    protected final int internalNextInt() {
        throw new UnsupportedOperationException();
    }
    @Override
    protected final long internalNextLong() {
        throw new UnsupportedOperationException();
    }
}
