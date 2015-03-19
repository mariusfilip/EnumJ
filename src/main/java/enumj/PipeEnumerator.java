/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Marius Filip
 */
class PipeEnumerator<E> extends AbstractEnumerator<E> {

    protected LinkedList<PipeProcessor> pipeline;
    protected LinkedList<PipeReference> sources;
    protected Facultative<E> value;

    PipeEnumerator(Iterator<E> source) {
        this();
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        PipeReference<?> ref = PipeReference.of(source);
        this.sources.add(ref);
    }
    PipeEnumerator() {
        this.pipeline = new LinkedList<>();
        this.sources = new LinkedList<>();
        this.value = Facultative.empty();
    }

    protected<X> Enumerator<X> enqueue(
            PipeProcessor<? super E, ? extends X> processor) {
        pipeline.addLast(processor);
        if (!sources.isEmpty()) {
            sources.peekLast().setRefIfNull(processor);
        }
        return (Enumerator<X>)this;
    }
    protected void dequeue() {
        if (!sources.isEmpty()) {
            sources.remove();
            if (sources.isEmpty()) {
                pipeline.clear();
            } else {
                while(!pipeline.isEmpty()
                      && pipeline.peekFirst() != sources.peekFirst().getRef()) {
                    pipeline.remove();
                }
            }
        }
    }
    protected boolean pipe(Object in, Facultative<E> out) {
        out.clear();
        for(PipeProcessor processor: pipeline) {
            processor.process(in);
            if (processor.hasValue()) {
                in = processor.getValue();
            }
            else if (!processor.continueOnNoValue()) {
                return false;
            }
        }

        out.set((E)in);
        return true;
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected boolean internalHasNext() {
        if (value.isPresent()) {
            return true;
        }
        while(true) {
            while(!sources.isEmpty() && !sources.peekFirst().hasNext()) {
                dequeue();
            }
            if (sources.isEmpty()) {
                return false;
            }
            do {
                Object in = sources.peekFirst().next();
                if (pipe(in, value)) {
                    return true;
                }
            }
            while(sources.peekFirst().hasNext());
        }
    }
    @Override
    protected E internalNext() {
        E result = value.get();
        value.clear();
        return result;
    }
    @Override
    protected void cleanup() {
        pipeline.clear();
        pipeline = null;
        sources.clear();
        sources = null;
        value.clear();
        value = null;
    }

    // ---------------------------------------------------------------------- //

    @Override
    public Enumerator<E> filter(Predicate<? super E> predicate) {
        return enqueue(new FilterPipeProcessor(predicate));
    }
    @Override
    public <X> Enumerator<X> map(
            Function<? super E, ? extends X> mapper) {
        return enqueue(new MapPipeProcessor(mapper));
    }
    @Override
    public Enumerator<E> skipWhile(Predicate<? super E> predicate) {
        return enqueue(new SkipPipeProcessor(predicate));
    }
    @Override
    public Enumerator<E> takeWhile(Predicate<? super E> predicate) {
        return enqueue(new WhilePipeProcessor(predicate));
    }
}
