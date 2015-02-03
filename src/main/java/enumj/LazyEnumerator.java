/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 *
 * @author Marius Filip
 */
public final class LazyEnumerator<E> extends AbstractEnumerator<E> {

    private Supplier<? extends Iterator<E>> source;
    private Iterator<E> iterator;
    private boolean done;

    public LazyEnumerator(Supplier<? extends Iterator<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
        this.iterator = null;
        this.done = false;
    }

    @Override
    public boolean hasNext() {
        if (!done) {
            done = !getIterator().hasNext();
            if (done) {
                source = null;
                iterator = null;
            }
        }
        return !done;
    }

    private Iterator<E> getIterator() {
        if (iterator == null) {
            iterator = source.get();
            Utils.ensureNotNull(iterator, Messages.NullEnumeratorSource);
        }
        return iterator;
    }

    @Override
    protected E nextValue() {
        return getIterator().next();
    }
}
