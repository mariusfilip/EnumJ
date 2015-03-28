/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import org.apache.commons.lang3.mutable.MutableBoolean;

final class OnceEnumerable<E> implements Enumerable<E> {

    private Enumerator<E> source;
    private MutableBoolean enumerated;

    public OnceEnumerable(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        this.source = Enumerator.of(source);
        this.enumerated = new MutableBoolean();
    }

    @Override
    public Enumerator<E> enumerator() {
        Utils.ensureOnce(enumerated);
        return this.source;
    }
}
