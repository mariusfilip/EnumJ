/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Marius Filip
 */
class ZipAnyEnumerator<U, V>
      extends ZipEnumerator<U, V, Pair<Optional<U>, Optional<V>>> {
    public ZipAnyEnumerator(Iterator<U> left, Iterator<V> right) {
        super(left, right, false, false, ZipAnyEnumerator::combine);
    }

    private static <U,V> Pair<Optional<U>, Optional<V>>
                         combine(U left, V right) {
        return Pair.of(Optional.ofNullable(left), Optional.ofNullable(right));
    }
}
