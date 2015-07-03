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

    protected LinkedList<AbstractPipeProcessor>      pipeline;
    protected LinkedList<AbstractPipeMultiProcessor> multiPipeline;
    protected LinkedList<PipeReference>              sources;
    protected Nullable<E>                            value;
    protected long                                   needValueForHasNext;

    PipeEnumerator(Iterator<E> source) {
        this();
        Utils.ensureNotNull(source, Messages.NULL_ENUMERATOR_SOURCE);
        PipeReference<?> ref = PipeReference.of(source);
        this.sources.add(ref);
    }
    PipeEnumerator() {
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

    protected <X> Enumerator<X> enqueueProcessor(
            AbstractPipeProcessor<? super E, ? extends X> processor) {
        final AbstractPipeProcessor<?,? extends E> last = pipeline.peekLast();
        safePipelineAddLast(processor);
        if (processor.hasNextNeedsValue()) {
            ++needValueForHasNext;
        }
        if (last != null) {
            last.setNext((AbstractPipeProcessor)processor);
        }
        if (!sources.isEmpty()) {
            sources.getLast().setReferenceIfNull(processor);
        }
        return (Enumerator<X>)this;
    }
    protected <X> PipeEnumerator<E> pushFrontProcessor(
            AbstractPipeProcessor<? super X, ?> processor) {
        final AbstractPipeProcessor<?,?> first = pipeline.peekFirst();
        safePipelineAddFirst(processor);
        if (processor.hasNextNeedsValue()) {
            ++needValueForHasNext;
        }
        if (first != null) {
            processor.setNext((AbstractPipeProcessor)first);
        }
        return this;
    }
    protected <X> Enumerator<X> enqueueProcessor(
            AbstractPipeMultiProcessor<? super E, ? extends X> processor) {
        final AbstractPipeProcessor<?,? extends E> last = pipeline.peekLast();
        safePipelineAddLast(processor);
        safeMultiPipelineAddLast(processor);
        if (processor.hasNextNeedsValue()) {
            ++needValueForHasNext;
        }
        if (last != null) {
            last.setNext((AbstractPipeProcessor)processor);
        }
        if (!sources.isEmpty()) {
            sources.peekLast().setReferenceIfNull(processor);
        }
        return (Enumerator<X>)this;
    }
    protected <X> PipeEnumerator<E> pushFrontProcessor(
            AbstractPipeMultiProcessor<? super X, ?> processor) {
        final AbstractPipeProcessor<?,?> first = pipeline.peekFirst();
        safePipelineAddFirst(processor);
        safeMultiPipelineAddFirst(processor);
        if (processor.hasNextNeedsValue()) {
            ++needValueForHasNext;
        }
        if (first != null) {
            processor.setNext((AbstractPipeProcessor)first);
        }
        return this;
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  //

    protected <X> void safePipelineAddLast(
            AbstractPipeProcessor<? super E, ? extends X> processor) {
        pipeline.addLast(processor);
    }
    protected <X> void safeMultiPipelineAddLast(
            AbstractPipeMultiProcessor<? super E, ? extends X> processor) {
        try {
            multiPipeline.addLast(processor);
        } catch(Throwable err) {
            pipeline.removeLast();
            throw err;
        }
    }
    protected <X> void safePipelineAddFirst(
            AbstractPipeProcessor<? super X, ?> processor) {
        pipeline.addFirst(processor);
    }
    protected <X> void safeMultiPipelineAddFirst(
            AbstractPipeMultiProcessor<? super X, ?> processor) {
        try {
            multiPipeline.addFirst(processor);
        } catch(Throwable err) {
            pipeline.removeFirst();
            throw err;
        }
    }

    // ---------------------------------------------------------------------- //

    protected void dequeueSourceWithProcessors() {
        dequeueSourceProcessors(sources.remove());
        value.clear();
    }
    protected void dequeueSourceProcessors(PipeReference removed) {
        if (sources.isEmpty()) {
            pipeline.clear();
            multiPipeline.clear();
            return;
        }

        AbstractPipeProcessor firstInPipeline = pipeline.peekFirst();
        final PipeReference firstReference = sources.getFirst();

        if (firstReference.getReference() == null) {
            if (removed.getReference() != null) {
                while(firstInPipeline != null) {
                    firstInPipeline = dequeueProcessor();
                }
            }
            return;
        }

        while(firstInPipeline != null
              && firstInPipeline != firstReference.getReference()) {
            firstInPipeline = dequeueProcessor();
        }
    }
    protected AbstractPipeProcessor dequeueProcessor() {
        final AbstractPipeProcessor head = pipeline.remove();
        if (!multiPipeline.isEmpty()
            && head == multiPipeline.getFirst()) {
            multiPipeline.remove();
        }
        if (head.hasNextNeedsValue()) {
            --needValueForHasNext;
        }
        return pipeline.peekFirst();
    }
    protected boolean tryPipelineIn(
            Out<Object>                in,
            Out<AbstractPipeProcessor> processor) {
        in.clear();
        processor.clear();

        if (!multiPipeline.isEmpty()) {
            final Iterator<AbstractPipeMultiProcessor> multiProcessorIterator =
                    multiPipeline.descendingIterator();
            AbstractPipeMultiProcessor multiProcessor;
            while(multiProcessorIterator.hasNext()) {
                multiProcessor = multiProcessorIterator.next();
                if (!multiProcessor.needsValue()) {
                    in.set(multiProcessor.getValue());
                    processor.set(multiProcessor.getNext());
                    return true;
                }
            }
        }
        while(!sources.isEmpty() && !sources.peekFirst().hasNext()) {
            dequeueSourceWithProcessors();
        }
        if (sources.isEmpty()) {
            return false;
        }

        in.set(sources.peekFirst().next());
        processor.set(pipeline.peekFirst());
        return true;
    }
    protected boolean tryPipelineOut(
            Object                in,
            AbstractPipeProcessor processor,
            Nullable<E>           out,
            Out<Boolean>          nextElementOnNoValue) {
        out.clear();
        nextElementOnNoValue.clear();

        while(processor != null) {
            processor.process(in);
            if (processor.hasValue()) {
                in = processor.getValue();
            }
            else {
                nextElementOnNoValue.set(
                        processor.nextElementOnNoValue());
                return false;
            }
            processor = processor.getNext();
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
        return needValueForHasNext > 0
                ? tryGetNext()
                : straightHasNext();
    }
    @Override
    protected E internalNext() {
        if (value.isPresent()) {
            final E result = value.get();
            value.clear();
            return result;
        }
        tryGetNext();

        final E result = value.get();
        value.clear();
        return result;
    }
    @Override
    protected void cleanup() {
        pipeline = null;
        multiPipeline = null;
        sources = null;
        value = null;
    }

    protected final boolean straightHasNext() {
        while(!sources.isEmpty() && !sources.peekFirst().hasNext()) {
            dequeueSourceWithProcessors();
        }
        return !sources.isEmpty();
    }
    protected final boolean tryGetNext() {
        final Out<Object> in = Out.empty();
        final Out<AbstractPipeProcessor> processor = Out.empty();
        final Out<Boolean> nextElementOnNoValue = Out.empty();
        while(true) {
            if (!tryPipelineIn(in, processor)) {
                return false;
            }
            if (tryPipelineOut(in.get(),
                               processor.get(),
                               value,
                               nextElementOnNoValue)) {
                return true;
            }

            if (nextElementOnNoValue.get()) {
                // continue to next element
            } else {
                // continue to next source
                dequeueSourceWithProcessors();
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
        return enqueueProcessor(new FilterPipeProcessor(predicate));
    }
    @Override
    public <R> Enumerator<R> flatMap(
        Function<? super E, ? extends Iterator<? extends R>> mapper) {
        return enqueueProcessor(new FlatMapPipeProcessor(mapper));
    }
    @Override
    public <X> Enumerator<X> map(
            Function<? super E, ? extends X> mapper) {
        return enqueueProcessor(new MapPipeProcessor(mapper));
    }
    @Override
    public Enumerator<E> limit(long maxSize) {
        return enqueueProcessor(new LimitPipeProcessor(maxSize));
    }
    @Override
    public Enumerator<E> takeWhile(Predicate<? super E> predicate) {
        return enqueueProcessor(new WhilePipeProcessor(predicate));
    }
    @Override
    public Enumerator<Optional<E>[]> zipAll(Iterator<? extends E> first,
                                            Iterator<? extends E>... rest) {
        return zipAll((Iterator<E>)first,
                      (List<Iterator<E>>)(List<?>)Arrays.asList(rest));
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -//

    Enumerator<Optional<E>[]> zipAll(Iterator<E> first,
                                     List<Iterator<E>> rest) {
        PipeEnumerator<Optional<E>> optionalPipe =
                (PipeEnumerator<Optional<E>>)map(e -> Optional.of(e))
                .concat(Enumerator.of(() -> Optional.of(Optional.empty())));
        return optionalPipe.enqueueProcessor(new ZipPipeProcessor(first, rest));
    }

    // ---------------------------------------------------------------------- //

    public PipeEnumerator<E> setSource(Iterator<?> elements) {
        sources.addFirst(PipeReference.of(elements));
        if (!pipeline.isEmpty()
            && pipeline.getFirst().getReference() == null) {
            sources.getFirst().setReferenceIfNull(pipeline.getFirst());
        }
        return this;
    }
    public PipeEnumerator<E> reversedConcat(Iterator<?> elements) {
        return setSource(elements);
    }
    public PipeEnumerator<E> reversedFilter(Predicate<?> predicate) {
        return pushFrontProcessor(new FilterPipeProcessor(predicate));
    }
    public PipeEnumerator<E> reversedFlatMap(
        Function<?, ? extends Iterator<?>> mapper) {
        return pushFrontProcessor(new FlatMapPipeProcessor(mapper));
    }
    public <X> PipeEnumerator<E> reversedMap(
            Function<? super X, ?> mapper) {
        return pushFrontProcessor(new MapPipeProcessor(mapper));
    }
    public Enumerator<E> reversedLimit(long maxSize) {
        return pushFrontProcessor(new LimitPipeProcessor(maxSize));
    }
    public PipeEnumerator<E> reversedTakeWhile(Predicate<?> predicate) {
        return pushFrontProcessor(new WhilePipeProcessor(predicate));
    }
    public PipeEnumerator<E> reversedZipAll(Iterator<?> first,
                                            List<Iterator<?>> rest) {
        pushFrontProcessor(new ZipPipeProcessor(first, rest));
        reversedConcat(Enumerator.of(() -> Optional.of(Optional.empty())));
        return reversedMap(e -> Optional.of(e));
    }
}
