/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.NoSuchElementException;

/**
 *
 * @author Marius Filip
 */
abstract class AbstractEnumerator<E> implements Enumerator<E> {

    @Override
    public final E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return nextValue();
    }

    protected abstract E nextValue();
}
