/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.function.Function;

/**
 *
 * @author Marius Filip
 */
class MapPipeProcessor<In,Out> extends AbstractPipeProcessor<In,Out> {

    protected Function<In,Out> mapper;
    protected Out value;

    public MapPipeProcessor(Function<In,Out> functor) {
        this.mapper = functor;
    }

    @Override
    protected void process(In value) {
        this.value = mapper.apply(value);
    }
    @Override
    protected boolean hasValue() {
        return true;
    }
    @Override
    protected Out getValue() {
        return value;
    }
    @Override
    protected boolean nextOnNoValue() {
        return false;
    }
    @Override
    protected boolean hasNextNeedsValue() {
        return false;
    }
}
