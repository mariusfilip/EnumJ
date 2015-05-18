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
class SkipPipeProcessor<E> extends AbstractPipeProcessor<E,E> {
    protected E value;
    protected Predicate<E> filter;
    protected boolean skip;

    public SkipPipeProcessor(Predicate<E> filter) {
        Utils.ensureNotNull(filter, Messages.NULL_ENUMERATOR_PREDICATE);
        this.filter = filter;
        this.skip = true;
    }

    @Override
    protected void process(E value) {
        this.value = value;
    }
    @Override
    protected boolean hasValue() {
        if (!skip) {
            return true;
        }
        skip = filter.test(value);
        return !skip;
    }
    @Override
    protected E getValue() {
        return value;
    }
    @Override
    protected boolean nextOnNoValue() {
        return true;
    }
    @Override
    protected boolean hasNextNeedsValue() {
        return true;
    }
}
