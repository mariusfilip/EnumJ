/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Marius Filip
 */
class FlatteningEnumerator<E> extends AbstractEnumerator<E> {

    private Enumerator<Iterator<E>> source;
    private Nullable<Enumerator<E>> iterator;
    private LinkedList<Iterator<E>> rest;

    public FlatteningEnumerator(Iterator<Iterator<E>> source) {
        this.source = Enumerator.of(source);
        this.iterator = Nullable.empty();
    }

    @Override
    protected boolean internalHasNext() {
        if (iterator.isPresent() && iterator.get().hasNext()) {
            return true;
        }
        if (source != null) {
            while(source.hasNext()) {
                iterator.set(Enumerator.of(source.next()));
                if (iterator.get().hasNext()) {
                    return true;
                }
            }
            source = null;
        }
        if (rest != null) {
            while (!rest.isEmpty()) {
                iterator.set(Enumerator.of(rest.remove()));
                if (iterator.get().hasNext()) {
                    return true;
                }
            }
            rest = null;
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

    // ---------------------------------------------------------------------- //

    @Override
    public Enumerator<E> concat(Iterator<? extends E> elements) {
        Utils.ensureNonEnumerating(this);
        if (rest == null) {
            rest = new LinkedList<>();
        }
        rest.addLast((Iterator<E>)elements);
        return this;
    }
}
