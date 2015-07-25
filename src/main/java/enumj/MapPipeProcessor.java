/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.function.Function;

/**
 * Type of {@link AbstractPipeMultiProcessor} that transforms elements
 * by mapping them.
 * @param <In> input value to map.
 * @param <Out> mapped output value.
 */
final class MapPipeProcessor<In,Out> extends AbstractPipeProcessor<In,Out> {

    protected Function<In,Out> mapper;
    protected Out              value;

    /**
     * Constructs {@link MapPipeProcessor} instances.
     * @param functor {@link Function} instance mapping input elements to
     * output elements.
     */
    public MapPipeProcessor(Function<In,Out> functor) {
        super(false, false);
        this.mapper = functor;
    }

    @Override
    protected void processInputValue(In value) {
        this.value = mapper.apply(value);
    }
    @Override
    protected boolean hasOutputValue() {
        return true;
    }
    @Override
    protected Out retrieveOutputValue() {
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
