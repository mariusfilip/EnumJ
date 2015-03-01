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
    private boolean isValue;

    public SuppliedEnumerator(Supplier<Optional<E>> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    protected boolean mayContinue() {
        if (!isValue) {
            value = source.get();
            isValue = true;
        }
        return value.isPresent();
    }
    @Override
    protected E nextValue() {
        isValue = false;
        return value.get();
    }
    @Override
    protected void cleanup() {
        source = null;
        value = null;
    }
}
