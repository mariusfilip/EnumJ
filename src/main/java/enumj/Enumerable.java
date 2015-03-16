/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;

final class Enumerable<E> implements Iterable<E> {

    private Iterator<E> source;

    public Enumerable(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
    }

    @Override
    public Iterator<E> iterator() {
        return this.source;
    }
}
