/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.NoSuchElementException;

abstract class AbstractEnumerator<E> implements Enumerator<E> {

    private boolean enumerating;
    private boolean yielding;
    private boolean done;

    @Override
    public final boolean enumerating() {
        return enumerating;
    }

    @Override
    public final boolean hasNext() {
        try {
            if (done) {
                return false;
            }
            done = !internalHasNext();
            if (done) {
                cleanup();
            }
            return !done;
        } finally {
            enumerating = true;
            yielding = true;
        }
    }

    @Override
    public final E next() {
        if (!yielding) {
            hasNext();
        }
        if (done) {
            throw new NoSuchElementException();
        }
        try {
            return internalNext();
        } finally {
            yielding = false;
        }
    }

    protected abstract boolean internalHasNext();
    protected abstract E internalNext();
    protected void cleanup() {}
}
