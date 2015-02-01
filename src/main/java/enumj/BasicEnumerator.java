/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;

/**
 *
 * @author Marius Filip
 */
class BasicEnumerator<E> extends AbstractEnumerator<E> {

    protected Iterator<E> source;
    private boolean done;

    public BasicEnumerator(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        if (!done) {
            done = !source.hasNext();
            if (done) {
                source = null;
            }
        }
        return !done;
    }

    @Override
    protected E nextValue() {
        return source.next();
    }
}
