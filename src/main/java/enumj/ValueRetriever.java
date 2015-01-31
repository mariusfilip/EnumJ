/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.lang3.mutable.Mutable;

/**
 *
 * @author Marius Filip
 */
abstract class ValueRetriever<E> {

    private final Iterator<E> source;

    public ValueRetriever(Iterator<E> source) {
        assert source != null;
        this.source = source;
    }

    public final Optional<Mutable<E>> get() {
        if (!source.hasNext()) {
            return Optional.empty();
        }
        E first = source.next();
        return getValue(first, source);
    }

    protected abstract Optional<Mutable<E>> getValue(E first, Iterator<E> rest);
}
