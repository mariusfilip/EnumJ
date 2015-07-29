/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.LinkedList;
import java.util.function.Function;

/**
 * Pipe processor that transforms inputs elements by mapping them to output
 * elements.
 *
 * @param <In> input value to map.
 * @param <Out> mapped output value.
 * @see FlatMapPipeProcessor
 * @see ZipPipeProcessor
 */
final class MapPipeProcessor<In,Out> extends AbstractPipeProcessor<In,Out> {

    protected LinkedList<Function<In,Out>> mappers;
    protected Out                          value;

    /**
     * Constructs a {@code MapPipeProcessor} instance.
     * <p>
     * The new {@link MapPipeProcessor} instance stores its {@code functor}
     * internally.
     * </p>
     *
     * @param functor {@link Function} mapping input elements to
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
    public void processInputValue(In value) {
        for(Function fun : mappers) {
            value = (In)fun.apply(value);
        }
        this.value = (Out)value;
    }
    @Override
    public boolean hasOutputValue() {
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
    public boolean isInactive() {
        return false;
    }
}
