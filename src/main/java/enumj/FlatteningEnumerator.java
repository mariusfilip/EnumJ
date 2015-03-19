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
class FlatteningEnumerator<E> extends AbstractEnumerator<E> {

    private Enumerator<Iterator<E>> source;
    private Facultative<Enumerator<E>> iterator;

    public FlatteningEnumerator(Iterator<Iterator<E>> source) {
        this.source = Enumerator.of(source);
        this.iterator = Facultative.empty();
    }

    @Override
    protected boolean internalHasNext() {
        if (iterator.isPresent() && iterator.get().hasNext()) {
            return true;
        }
        while(source.hasNext()) {
            iterator.set(Enumerator.of(source.next()));
            if (iterator.get().hasNext()) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected E internalNext() {
        return iterator.get().next();
    }
    @Override
    protected void cleanup() {
        source = null;
        iterator.clear();
        iterator = null;
    }
}
