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
class SkipWhileValueRetriever<E> extends ValueRetriever<E> {

    private Predicate<? super E> predicate;
    private boolean testing;

    public SkipWhileValueRetriever(Iterator<E> source,
                                   Predicate<? super E> predicate) {
        super(source);
        assert predicate != null;
        this.predicate = predicate;
        this.testing = true;
    }

    @Override
    protected Optional<Mutable<E>> getValue(E first, Iterator<E> rest) {
        Mutable<E> value = new MutableObject(first);
        if (testing) {
            while (value != null && predicate.test(value.getValue())) {
                value = rest.hasNext()
                        ? new MutableObject(rest.next())
                        : null;
            }
            testing = false;
        }
        return Optional.ofNullable(value);
    }
}
