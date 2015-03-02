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

    protected LinkedList<Iterator<E>> sources;

    public FlatteningEnumerator(Iterator<Iterator<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.sources = new LinkedList<>();
        while(source.hasNext()) {
            this.sources.addLast(source.next());
        }
    }

    @Override
    protected boolean mayContinue() {
        while (sources.size() > 0) {
            if (sources.element().hasNext()) {
                return true;
            }
            sources.remove();
        }
        return false;
    }
    @Override
    protected E nextValue() {
        return sources.element().next();
    }
    @Override
    protected void cleanup() {
        sources = null;
    }

    ////////////////////////////////////////////////////////////////////////////

    public Enumerator<E> prepend(Iterator<? extends E> elements) {
        sources.addFirst((Iterator<E>)elements);
        return this;
    }

    public Enumerator<E> concat(Iterator<? extends E> elements) {
        sources.addLast((Iterator<E>)elements);
        return this;
    }
}
