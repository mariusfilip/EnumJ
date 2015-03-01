/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.NoSuchElementException;

/**
 *
 * @author Marius Filip
 */
abstract class AbstractEnumerator<E> implements Enumerator<E> {

    private boolean hasNextCalled;
    private boolean done;
    
    @Override
    public final boolean hasNext() {
        try {
            if (done) {
                return false;
            }
            done = !mayContinue();
            if (done) {
                cleanup();
            }
            return !done;
        } finally {
            hasNextCalled = true;
        }
    }

    @Override
    public final E next() {
        if (!hasNextCalled) {
            hasNext();
        }
        if (done) {
            throw new NoSuchElementException();
        }
        E value;
        try {
            value = nextValue();
        } finally {
            hasNextCalled = false;
        }
        return value;
    }

    protected abstract boolean mayContinue();
    protected abstract E nextValue();
    protected void cleanup() {}
}
