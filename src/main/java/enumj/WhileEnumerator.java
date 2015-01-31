/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 *
 * @author Marius Filip
 */
class WhileEnumerator<E> extends RetrievingEnumerator<E> {
    public WhileEnumerator(Iterator<E> source, Predicate<? super E> predicate) {
        super(source, s -> new WhileValueRetriever(s, predicate));
        Utils.ensureNotNull(predicate, Messages.NullEnumeratorPredicate);
    }
}
