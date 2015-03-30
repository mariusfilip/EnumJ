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
final class LazyEnumerator<E> extends AbstractEnumerator<E> {

    private Supplier<Iterator<E>> source;
    private Enumerator<E> iterator;

    private LazyEnumerator(Supplier<Iterator<E>> source) {
        this.source = source;
    }

    @Override
    protected boolean internalHasNext() {
        if (iterator == null) {
            iterator = Enumerator.of(source.get());
        }
        return iterator.hasNext();
    }
    @Override
    protected E internalNext() {
        return iterator.next();
    }
    @Override
    protected void cleanup() {
        source = null;
        iterator = null;
    }
    
    public static <E> LazyEnumerator<E> of(
            Supplier<? extends Iterator<E>> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return new LazyEnumerator(source);
    }
}
