/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;

public final class OnceEnumerable<E> extends AbstractEnumerable<E> {

    private Enumerator<E> source;

    public OnceEnumerable(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        this.source = Enumerator.of(source);
    }

    @Override
    public Enumerator<E> internalEnumerator() {
        Utils.ensureNonEnumerating(this);
        Enumerator<E> result = source;
        source = null;
        return result;
    }
}
