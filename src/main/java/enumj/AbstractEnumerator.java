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

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Abstract implementation of the {@code Enumerator} interface.
 * <p>
 * This class forms the basis for all the concrete implementations of
 * {@link Enumerator} within the {@code enumj} package.
 * </p>
 *
 * @param <E> Type of elements being enumerated.
 * @see AbstractEnumerable
 * @see Enumerable
 * @see Enumerator
 */
abstract class AbstractEnumerator<E> implements Enumerator<E>, Recoverable {

    private boolean   started;
    private boolean   hasNextHasBeenCalled;
    private Throwable error;
    private boolean   done;
    
    private final InOut<E> value;

    /**
     * Constructs a new {@code AbstractEnumerator} instance.
     * <p>
     * The new {@link AbstractEnumerable} instance gets initialised as
     * non-enumerating.
     * </p>
     *
     * @see #enumerating
     */
    protected AbstractEnumerator() {
        this.value = new InOut<>();
    }

    @Override public final boolean enumerating() {
        return started;
    }
    @Override public final boolean hasNext() {
        if (error != null) {
            throw new UnsupportedOperationException(
                    "Enumerator in error state", error);
        }
        if (done) {
            return false;
        }

        started = true;
        done = !safeHasNext();
        if (done) {
            safeCleanup();
        }
        return !done;
    }
    @Override public final E next() {
        if (!hasNextHasBeenCalled) {
            hasNext();
        }
        if (done) {
            throw new NoSuchElementException();
        }
        if (error != null) {
            throw new UnsupportedOperationException(
                    "Enumerator in error state", error);
        }
        try {
            internalNext(value);
            return value.get();
        } catch(Throwable exception) {
            error = exception;
            throw exception;
        } finally {
            hasNextHasBeenCalled = false;
            value.clear();
        }
    }
    @Override public final Optional<Throwable> getLastError() {
        return Optional.ofNullable(error);
    }
    @Override public final void recover() {
        if (error != null) {
            try {
                internalRecovery(error);
            } finally {
                error = null;
            }
        }
    }

    private boolean safeHasNext() {
        try {
            return internalHasNext();
        } catch(Throwable exception) {
            error = exception;
            throw exception;
        } finally {
            hasNextHasBeenCalled = true;
        }
    }
    private void safeCleanup() {
        try {
            value.clear();
            error = null;
            cleanup();
        } catch(Throwable exception) {
            error = exception;
            throw exception;
        }
    }

    /**
     * Returns whether the enumerator has more elements.
     * <p>
     * This method is the internal counterpart of {@link #hasNext()}.
     * </p>
     *
     * @return true if the enumerator has more elements, false
     * otherwise.
     * @see #next()
     */
    protected abstract boolean internalHasNext();
    /**
     * Returns the next enumerated element.
     * <p>
     * This method is the internal counterpart of {@link #hasNext()}
     * </p>
     * @param value instance of {@link Out} storing the next value.
     */
    protected abstract void internalNext(Out<E> value);
    protected abstract void internalRecovery(Throwable error);
    /**
     * Cleans up the internals of the current enumerator when enumeration
     * ends.
     * <p>
     * By default this method does nothing.
     * </p>
     * 
     * @see #hasNext()
     * @see #next()
     * @see #internalHasNext()
     * @see #internalNext()
     */
    protected void cleanup() {}
}
