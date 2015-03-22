/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.function.Predicate;

/**
 *
 * @author Marius Filip
 */
class WhilePipeProcessor<E> extends PipeProcessor<E,E> {
    protected E value;
    protected Predicate<E> filter;
    protected boolean get;

    public WhilePipeProcessor(Predicate<E> filter) {
        Utils.ensureNotNull(filter, Messages.NULL_ENUMERATOR_PREDICATE);
        this.filter = filter;
        this.get = true;
    }

    @Override
    public void process(E value) {
        this.value = value;
    }
    @Override
    public boolean hasValue() {
        if (!get) {
            return false;
        }
        return get = filter.test(value);
    }
    @Override
    public E getValue() {
        return value;
    }
    @Override
    public boolean nextOnNoValue() {
        return false;
    }
}
