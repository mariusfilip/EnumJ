/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

/**
 *
 * @author Marius Filip
 */
class FlatteningEnumerator<E> extends AbstractEnumerator<E> {

    private final Iterator<Iterator<E>> source;
    private Iterator<E> iterator;
    private Optional<E> value;

    public FlatteningEnumerator(Iterator<Iterator<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        while ((iterator == null || !iterator.hasNext())
               && source.hasNext()) {
            iterator = source.next();
        }
        return iterator != null && iterator.hasNext();
    }

    @Override
    protected E nextValue() {
        return iterator.next();
    }
}
