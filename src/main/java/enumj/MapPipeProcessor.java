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
class MapPipeProcessor<In,Out> extends PipeProcessor<In,Out> {
    protected Function<In,Out> functor;
    protected Out value;

    public MapPipeProcessor(Function<In,Out> functor) {
        Utils.ensureNotNull(functor, Messages.NullEnumeratorMapper);
        this.functor = functor;
    }

    @Override
    protected void process(In value) {
        this.value = functor.apply((In)value);
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
    protected boolean continueOnNoValue() {
        return false;
    }
}
