/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Marius Filip
 */
public class PipeEnumerator<E,R> extends AbstractEnumerator<R> {
    protected Iterator<E> source;
    protected LinkedList<PipeProcessor> pipeline;
    protected R value;
    protected boolean isValue;

    public PipeEnumerator(Iterator<E> source) {
        Utils.ensureNotNull(source, Messages.NullEnumeratorSource);
        this.source = source;
        this.pipeline = new LinkedList<>();
    }
    protected PipeEnumerator() {
        this.source = null;
        this.pipeline = new LinkedList<>();
    }

    protected<X> Enumerator<X> pipe(
            PipeProcessor<? super R, ? extends X> processor) {
        pipeline.add(processor);
        return (Enumerator<X>)this;
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected boolean mayContinue() {
        if (isValue) {
            return true;
        }
        while (source.hasNext()) {
            Object elem = source.next();
            boolean completed = true;
            for(PipeProcessor processor : pipeline) {
                processor.process(elem);
                if (processor.hasValue()) {
                    elem = processor.getValue();
                    continue;
                }
                if (processor.continueOnNoValue()) {
                    completed = false;
                    break;
                }
                return false;
            }
            if (completed) {
                value = (R)elem;
                isValue = true;
                return true;
            }
        }
        return false;
    }
    @Override
    protected R nextValue() {
        isValue = false;
        return value;
    }
    @Override
    protected void cleanup() {
        source = null;
        pipeline = null;
        value = null;
    }

    // ---------------------------------------------------------------------- //

    @Override
    public Enumerator<R> filter(Predicate<? super R> predicate) {
        return pipe(new FilterPipeProcessor(predicate));
    }
    @Override
    public <X> Enumerator<X> map(
            Function<? super R, ? extends X> mapper) {
        return pipe(new MapPipeProcessor(mapper));
    }
    @Override
    public Enumerator<R> skipWhile(Predicate<? super R> predicate) {
        return pipe(new SkipPipeProcessor(predicate));
    }
    @Override
    public Enumerator<R> takeWhile(Predicate<? super R> predicate) {
        return pipe(new WhilePipeProcessor(predicate));
    }
}
