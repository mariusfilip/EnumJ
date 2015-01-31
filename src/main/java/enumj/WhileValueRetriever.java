/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

/**
 *
 * @author Marius Filip
 */
class WhileValueRetriever<E> extends ValueRetriever<E> {

    private final Predicate<? super E> predicate;

    public WhileValueRetriever(Iterator<E> source,
                               Predicate<? super E> predicate) {
        super(source);
        assert predicate != null;
        this.predicate = predicate;
    }

    @Override
    protected Optional<Mutable<E>> getValue(E first, Iterator<E> rest) {
        E value = first;
        return predicate.test(value)
               ? Optional.of(new MutableObject(value))
               : Optional.empty();
    }
}
