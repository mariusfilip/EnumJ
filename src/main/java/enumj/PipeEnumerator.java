/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package enumj;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

class PipeEnumerator<E> extends AbstractEnumerator<E> {

    protected LinkedList<AbstractPipeProcessor> pipeline;
    protected LinkedList<PipeMultiProcessor> multiPipeline;
    protected LinkedList<PipeReference> sources;
    protected Nullable<E> value;
    protected long needValueForHasNext;

    PipeEnumerator(Iterator<E> source) {
        this();
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        PipeReference<?> ref = PipeReference.of(source);
        this.sources.add(ref);
    }
    private PipeEnumerator() {
        this.pipeline = new LinkedList<>();
        this.multiPipeline = new LinkedList<>();
        this.sources = new LinkedList<>();
        this.value = Nullable.empty();
    }

    static <T> PipeEnumerator<T> of(Iterator<T> source) {
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        return (source instanceof PipeEnumerator)
               ? (PipeEnumerator<T>)source
               : new PipeEnumerator(source);
    }

    // ---------------------------------------------------------------------- //

    protected <X> Enumerator<X> enqueue(
            AbstractPipeProcessor<? super E, ? extends X> processor) {
        final AbstractPipeProcessor last = pipeline.peekLast();
        pipelineAddLast(processor);
        if (processor.hasNextNeedsValue()) {
            ++needValueForHasNext;
        }
        if (last != null) {
            last.next = processor;
        }
        if (!sources.isEmpty()) {
            sources.peekLast().setRefIfNull(processor);
        }
        return (Enumerator<X>)this;
    }
    protected <X> Enumerator<X> enqueue(
            PipeMultiProcessor<? super E, ? extends X> processor) {
        final AbstractPipeProcessor last = pipeline.peekLast();
        boolean added = false;
        try {
            pipelineAddLast(processor);
            added = true;
            multiPipelineAddLast(processor);
        } catch(Exception ex) {
            if (added) {
                pipeline.removeLast();
            }
            throw ex;
        }
        if (processor.hasNextNeedsValue()) {
            ++needValueForHasNext;
        }
        if (last != null) {
            last.next = processor;
        }
        if (!sources.isEmpty()) {
            sources.peekLast().setRefIfNull(processor);
        }
        return (Enumerator<X>)this;
    }
    protected void dequeue() {
        assert !sources.isEmpty();
        sources.remove();
        value.clear();
        
        if (!sources.isEmpty()) {
            while(!pipeline.isEmpty()
                  && pipeline.peekFirst() != sources.peekFirst().getRef()) {
                AbstractPipeProcessor head = pipeline.remove();
                if (!multiPipeline.isEmpty()
                    && head == multiPipeline.peekFirst()) {
                    multiPipeline.remove();
                }
                if (head.hasNextNeedsValue()) {
                    --needValueForHasNext;
                }
            }
        } else {
            pipeline.clear();
            multiPipeline.clear();
        }
    }
    protected boolean tryPipelineIn(
            Nullable<Object>                in,
            Nullable<AbstractPipeProcessor> processor) {
        in.clear();
        processor.clear();

        if (!multiPipeline.isEmpty()) {
            Iterator<PipeMultiProcessor> multiProcessorIterator =
                    multiPipeline.descendingIterator();
            PipeMultiProcessor multiProcessor;
            while(multiProcessorIterator.hasNext()) {
                multiProcessor = multiProcessorIterator.next();
                if (!multiProcessor.needsValue()) {
                    in.set(multiProcessor.getValue());
                    processor.set(multiProcessor.next);
                    return true;
                }
            }
        }
        while(!sources.isEmpty() && !sources.peekFirst().hasNext()) {
            dequeue();
        }
        if (sources.isEmpty()) {
            return false;
        }

        in.set(sources.peekFirst().next());
        processor.set(pipeline.peekFirst());
        return true;
    }
    protected boolean tryPipelineOut(Object                    in,
                                     AbstractPipeProcessor     processor,
                                     Nullable<E>               out,
                                     Nullable<Boolean>         nextOnNoValue) {
        out.clear();
        nextOnNoValue.clear();

        while(processor != null) {
            processor.process(in);
            if (processor.hasValue()) {
                in = processor.getValue();
            }
            else {
                nextOnNoValue.set(processor.nextOnNoValue());
                return false;
            }
            processor = processor.next;
        }

        out.set((E)in);
        return true;
    }

    protected <X> void pipelineAddLast(
            AbstractPipeProcessor<? super E, ? extends X> processor) {
        pipeline.addLast(processor);
    }

    protected <X> void multiPipelineAddLast(
            PipeMultiProcessor<? super E, ? extends X> processor) {
        multiPipeline.addLast(processor);
    }

    // ---------------------------------------------------------------------- //

    @Override
    protected boolean internalHasNext() {
        if (value.isPresent()) {
            return true;
        }
        if (needValueForHasNext > 0) {
            return tryGetNext();
        }

        assert needValueForHasNext == 0;
        return straightHasNext();
    }
    @Override
    protected E internalNext() {
        if (value.isPresent()) {
            final E result = value.get();
            value.clear();
            return result;
        }

        assert needValueForHasNext == 0;
        final boolean hasNext = tryGetNext();
        assert hasNext;
        assert value.isPresent();

        final E result = value.get();
        value.clear();
        return result;
    }
    @Override
    protected void cleanup() {
        pipeline.clear();
        pipeline = null;
        multiPipeline.clear();
        multiPipeline = null;
        sources.clear();
        sources = null;
        value.clear();
        value = null;
    }

    protected final boolean straightHasNext() {
        while(!sources.isEmpty() && !sources.peekFirst().hasNext()) {
            dequeue();
        }
        return !sources.isEmpty();
    }
    protected final boolean tryGetNext() {
        final Nullable<Object> in = Nullable.empty();
        final Nullable<AbstractPipeProcessor> processor = Nullable.empty();
        final Nullable<Boolean> nextOnNoValue = Nullable.empty();
        while(true) {
            if (!tryPipelineIn(in, processor)) {
                return false;
            }
            if (tryPipelineOut(in.get(),
                               processor.get(),
                               value,
                               nextOnNoValue)) {
                return true;
            }
            if (!nextOnNoValue.get()) {
                dequeue();
            }
        }
    }

    // ---------------------------------------------------------------------- //

    @Override
    public Enumerator<E> concat(Iterator<? extends E> elements) {
        sources.addLast(PipeReference.of(elements));
        return this;
    }
    @Override
    public Enumerator<E> filter(Predicate<? super E> predicate) {
        return enqueue(new FilterPipeProcessor(predicate));
    }
    @Override
    public <R> Enumerator<R> flatMap(
        Function<? super E, ? extends Iterator<? extends R>> mapper) {
        return enqueue(new FlatMapPipeProcessor(mapper));
    }
    @Override
    public <X> Enumerator<X> map(
            Function<? super E, ? extends X> mapper) {
        return enqueue(new MapPipeProcessor(mapper));
    }
    @Override
    public Enumerator<E> takeWhile(Predicate<? super E> predicate) {
        return enqueue(new WhilePipeProcessor(predicate));
    }
    @Override
    public Enumerator<Optional<E>[]> zipAll(Iterator<? extends E> first,
                                            Iterator<? extends E>... rest) {
        return zipAll((Iterator<E>)first,
                       (List<Iterator<E>>)(List<?>)Arrays.asList(rest));
    }

    // ---------------------------------------------------------------------- //

    Enumerator<Optional<E>[]> zipAll(Iterator<E> first,
                                     List<Iterator<E>> rest) {
        PipeEnumerator<Optional<E>> optionalPipe =
                (PipeEnumerator<Optional<E>>)map(e -> Optional.of(e))
                .concat(Enumerator.of(() -> Optional.of(Optional.empty())));
        return optionalPipe.enqueue(new ZipPipeProcessor(first, rest));
    }    
}
