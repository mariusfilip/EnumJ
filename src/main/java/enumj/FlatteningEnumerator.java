/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;

/**
 *
 * @author Marius Filip
 */
class FlatteningEnumerator<E> extends AbstractEnumerator<E> {

    private Iterator<Iterator<E>> source;
    private Iterator<E> iterator;
    private Optional<E> value;
    private boolean done;

    public FlatteningEnumerator(Iterator<Iterator<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        if (!done) {
            while ((iterator == null || !iterator.hasNext())
                   && source.hasNext()) {
                iterator = source.next();
            }
            done = !(iterator != null && iterator.hasNext());
            if (done) {
                source = null;
                iterator = null;
                value = null;
            }
        }
        return !done;
    }

    @Override
    protected E nextValue() {
        return iterator.next();
    }
}
