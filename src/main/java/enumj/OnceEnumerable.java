/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

final class OnceEnumerable<E> extends AbstractEnumerable<E> {

    private Enumerator<E> source;
    private AtomicBoolean consumed = new AtomicBoolean();

    public OnceEnumerable(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        this.source = Enumerator.of(source);
    }

    @Override
    protected Enumerator<E> internalEnumerator() {
        if (consumed.compareAndSet(false, true)) {
            return source;
        }
        throw new NoSuchElementException();
    }
    @Override
    protected boolean internalConsumed() {
        return consumed.get();
    }
    @Override
    protected void cleanup() {
        source = null;
    }
}
