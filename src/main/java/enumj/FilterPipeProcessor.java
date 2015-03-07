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
class FilterPipeProcessor<E> extends PipeProcessor<E,E> {
    protected E value;
    protected Predicate<E> filter;
    
    public FilterPipeProcessor(Predicate<E> filter) {
        Utils.ensureNotNull(filter, Messages.NullEnumeratorPredicate);
        this.filter = filter;
    }

    @Override
    public void process(E value) {
        this.value = value;
    }
    @Override
    public boolean hasValue() {
        return filter.test(value);
    }
    @Override
    public E getValue() {
        return value;
    }
    @Override
    public boolean continueOnNoValue() {
        return true;
    }
}
