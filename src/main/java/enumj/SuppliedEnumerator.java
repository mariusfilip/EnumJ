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
final class SuppliedEnumerator<E> extends AbstractEnumerator<E> {

    private Supplier<Optional<E>> source;
    private Optional<E> value;

    public SuppliedEnumerator(Supplier<Optional<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    protected boolean internalHasNext() {
        if (value == null) {
            value = source.get();
        }
        return value.isPresent();
    }
    @Override
    protected E internalNext() {
        E val = value.get();
        value = null;
        return val;
    }
    @Override
    protected void cleanup() {
        source = null;
        value = null;
    }
}
