/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Enumeration;

/**
 *
 * @author Marius Filip
 */
final class EnumerationEnumerator<E> extends AbstractEnumerator<E> {
    private Enumeration<E> enumeration;

    public EnumerationEnumerator(Enumeration<E> enumeration) {
        Utils.ensureNotNull(enumeration, Messages.NullEnumeratorSource);
        this.enumeration = enumeration;
    }

    @Override
    protected boolean mayContinue() {
        return enumeration.hasMoreElements();
    }
    @Override
    protected E nextValue() {
        return enumeration.nextElement();
    }
    @Override
    protected void cleanup() {
        enumeration = null;
    }
}
