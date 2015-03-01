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
public class SkipPipeProcessor<E> extends PipeProcessor<E,E> {
    protected E value;
    protected Predicate<E> filter;
    protected boolean skip;

    public SkipPipeProcessor(Predicate<E> filter) {
        Utils.ensureNotNull(filter, Messages.NullEnumeratorPredicate);
        this.filter = filter;
        this.skip = true;
    }

    @Override
    public void process(E value) {
        this.value = value;
    }
    @Override
    public boolean hasValue() {
        if (!skip) {
            return true;
        }
        skip = filter.test(value);
        return !skip;
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
