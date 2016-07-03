/*
 * The MIT License
 *
 * Copyright 2016 Marius Filip.
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
package enumj.primitive;

import java.util.PrimitiveIterator;

interface PipeSource {
    public PipeProcessor getFirstProcessor();
    public void setFirstProcessorIfNone(PipeProcessor processor);
    
    public static PrimitivePipeSource.OfInt of(
            PrimitiveIterator.OfInt source) {
        return PrimitivePipeSource.of(source);
    }
    public static PrimitivePipeSource.OfLong of(
            PrimitiveIterator.OfLong source) {
        return PrimitivePipeSource.of(source);
    }
    public static PrimitivePipeSource.OfDouble of(
            PrimitiveIterator.OfDouble source) {
        return PrimitivePipeSource.of(source);
    }
}

final class PrimitivePipeSource implements PipeSource {

    private PipeProcessor firstProcessor;
    
    private PrimitivePipeSource() {}
    
    @Override
    public final PipeProcessor getFirstProcessor() {
        return firstProcessor;
    }
    @Override
    public final void setFirstProcessorIfNone(PipeProcessor processor) {
        if (firstProcessor == null) {
            firstProcessor = processor;
        }
    }
    
    // ---------------------------------------------------------------------- //
    
    public class OfInt extends IteratorEnumerator.OfInt
                       implements PipeSource {
        protected OfInt(PrimitiveIterator.OfInt source) {
            super(source);
        }
        @Override
        public PipeProcessor getFirstProcessor() {
            return PrimitivePipeSource.this.getFirstProcessor();
        }
        @Override
        public void setFirstProcessorIfNone(PipeProcessor processor) {
            PrimitivePipeSource.this.setFirstProcessorIfNone(processor);
        }
    }
    public class OfLong extends IteratorEnumerator.OfLong
                        implements PipeSource {
        protected OfLong(PrimitiveIterator.OfLong source) {
            super(source);
        }
        @Override
        public PipeProcessor getFirstProcessor() {
            return PrimitivePipeSource.this.getFirstProcessor();
        }
        @Override
        public void setFirstProcessorIfNone(PipeProcessor processor) {
            PrimitivePipeSource.this.setFirstProcessorIfNone(processor);
        }
    }
    public class OfDouble extends IteratorEnumerator.OfDouble
                          implements PipeSource {
        protected OfDouble(PrimitiveIterator.OfDouble source) {
            super(source);
        }
        @Override
        public PipeProcessor getFirstProcessor() {
            return PrimitivePipeSource.this.getFirstProcessor();
        }
        @Override
        public void setFirstProcessorIfNone(PipeProcessor processor) {
            PrimitivePipeSource.this.setFirstProcessorIfNone(processor);
        }
    }

    // ---------------------------------------------------------------------- //

    public static OfInt of(PrimitiveIterator.OfInt source) {
        return (new PrimitivePipeSource()).new OfInt(source);
    }
    public static OfLong of(PrimitiveIterator.OfLong source) {
        return (new PrimitivePipeSource()).new OfLong(source);
    }
    public static OfDouble of(PrimitiveIterator.OfDouble source) {
        return (new PrimitivePipeSource()).new OfDouble(source);
    }
}
