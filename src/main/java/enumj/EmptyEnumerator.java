/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

/**
 *
 * @author Marius Filip
 */
class EmptyEnumerator<E> extends AbstractEnumerator<E> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    protected E nextValue() {
        return null;
    }
}
