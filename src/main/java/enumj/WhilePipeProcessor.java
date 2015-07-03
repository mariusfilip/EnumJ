/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.function.Predicate;

class WhilePipeProcessor<E> extends AbstractPipeProcessor<E,E> {
    protected E value;
    protected Predicate<E> filter;

    public WhilePipeProcessor(Predicate<E> filter) {
        Utils.ensureNotNull(filter, Messages.NULL_ENUMERATOR_PREDICATE);
        this.filter = filter;
    }

    @Override
    protected void process(E value) {
        if (this.filter != null) {
            this.value = value;
        }
    }
    @Override
    protected boolean hasValue() {
        if (filter == null) {
            return false;
        }

        if (!filter.test(value)) {
            value = null;
            filter = null;
        }
        return filter != null;
    }
    @Override
    protected E getValue() {
        return value;
    }
    @Override
    protected boolean nextElementOnNoValue() {
        return false;
    }
    @Override
    protected boolean hasNextNeedsValue() {
        return true;
    }
}
