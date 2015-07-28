/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.LinkedList;
import java.util.function.Function;

/**
 * Type of {@link AbstractPipeMultiProcessor} that transforms elements
 * by mapping them.
 * @param <In> input value to map.
 * @param <Out> mapped output value.
 */
final class MapPipeProcessor<In,Out> extends AbstractPipeProcessor<In,Out> {

    protected LinkedList<Function<In,Out>> mappers;
    protected Out                          value;

    /**
     * Constructs {@link MapPipeProcessor} instances.
     * @param functor {@link Function} instance mapping input elements to
     * output elements.
     */
    public MapPipeProcessor(Function<In,Out> functor) {
        super(false, false);
        this.mappers = new LinkedList<>();
        enqueueMap((Function<Out,Out>)functor);
    }

    // ---------------------------------------------------------------------- //

    @Override
    public <R> boolean pushFrontMap(Function<Out,R> mapper) {
        mappers.addFirst((Function)mapper);
        return true;
    }
    @Override
    public <R> boolean enqueueMap(Function<Out,R> mapper) {
        mappers.add((Function)mapper);
        return true;
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected void processInputValue(In value) {
        for(Function fun : mappers) {
            value = (In)fun.apply(value);
        }
        this.value = (Out)value;
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
