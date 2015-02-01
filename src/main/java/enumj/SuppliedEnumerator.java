/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Optional;
import java.util.function.Supplier;

/**
 *
 * @author Marius Filip
 */
class SuppliedEnumerator<E> extends AbstractEnumerator<E> {

    private Supplier<Optional<E>> source;
    private Optional<E> value;
    private boolean done;

    public SuppliedEnumerator(Supplier<Optional<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        if (!done) {
            done = !rawHasNext();
            if (done) {
                source = null;
                value = null;
            }
        }
        return !done;
    }
    
    private boolean rawHasNext() {
        if (value == null) {
            retrieveValue();
        }
        return value.isPresent();
    }

    @Override
    protected E nextValue() {
        Optional<E> oldValue = value;
        retrieveValue();
        return oldValue.get();
    }

    private void retrieveValue() {
        value = source.get();
    }
}
