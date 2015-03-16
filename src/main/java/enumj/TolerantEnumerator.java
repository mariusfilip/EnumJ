/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 *
 * @author Marius Filip
 */
final class TolerantEnumerator<E> implements Enumerator<E> {

    private final Consumer<? super Exception> handler;
    private Iterator<E> source;
    private E recent;
    private boolean hasRecent;
    private boolean hasNextCalled;
    private boolean done;

    public TolerantEnumerator(Iterator<E> source,
                              Consumer<? super Exception> handler) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        Utils.ensureNotNull(handler, Messages.NullEnumeratorHandler);
        this.source = source;
        this.handler = handler;
    }

    @Override
    public boolean enumerating() {
        return hasNextCalled;
    }

    @Override
    public boolean hasNext() {
        try {
            if (done) {
                return false;
            }
            if (hasRecent) {
                return true;
            }

            try {
                while (sourceHasNext()) {
                    try {
                        recent = source.next();
                        hasRecent = true;
                        break;
                    } catch(Exception ex) {
                        handler.accept(ex);
                    }
                }
            } catch(Exception ex3) { }

            if (hasRecent) {
                return true;
            }
            source = null;
            done = true;
            return false;
        }
        finally {
            hasNextCalled = true;
        }
    }

    private boolean sourceHasNext() {
        try {
            return source.hasNext();
        } catch(Exception ex) {
            handler.accept(ex);
            return false;
        }
    }

    @Override
    public E next() {
        if (!hasRecent) {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
        }
        assert hasRecent;

        final E result = recent;
        hasRecent = false;
        recent = null;
        return result;
    }
}
