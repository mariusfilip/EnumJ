/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.function.Predicate;

class FilterPipeProcessor<E> extends AbstractPipeProcessor<E,E> {
    protected E            value;
    protected Predicate<E> filter;
    
    public FilterPipeProcessor(Predicate<E> filter) {
        super(true, true);
        Utils.ensureNotNull(filter, Messages.NULL_ENUMERATOR_PREDICATE);
        this.filter = filter;
    }

    @Override
    protected void processInputValue(E value) {
        this.value = value;
    }
    @Override
    protected boolean hasOutputValue() {
        if (filter.test(value)) {
            return true;
        }
        value = null;
        return false;
    }
    @Override
    protected E retrieveOutputValue() {
        return value;
    }
    @Override
    protected void clearOutputValue() {
        value = null;
    }
    @Override
    protected boolean isInactive() {
        return false;
    }
}
