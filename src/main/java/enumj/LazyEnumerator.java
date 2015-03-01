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
final class LazyEnumerator<E> extends PipeEnumerator<E,E> {

    private Supplier<? extends Iterator<E>> source;

    public LazyEnumerator(Supplier<? extends Iterator<E>> source) {
        super();
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }
    
    @Override
    protected boolean mayContinue() {
        if (super.source == null) {
            super.source = this.source.get();
        }
        return super.mayContinue();
    }
    @Override
    protected void cleanup() {
        super.cleanup();
        this.source = null;
    }
}
