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

import java.util.LinkedList;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;

final class MapPipeProcessor {
    
    private Runnable             mapper;
    private LinkedList<Runnable> mappers;
    
    private MapPipeProcessor() {}
    
    private void init(Runnable mapper) { this.mapper = mapper; }
    private boolean pushFrontMap(Runnable mapper) {
        ensureMappers();
        mappers.addFirst(mapper);
        return true;
    }
    private boolean enqueueMap(Runnable mapper) {
        ensureMappers();
        mappers.addLast(mapper);
        return true;
    }
    private void ensureMappers() {
        if (mappers == null) {
            mappers = new LinkedList<>();
            mappers.add(mapper);
            mapper = null;
        }
    }
    
    private void processInputValue(Runnable initializer) {
        initializer.run();
        if (mapper != null) {
            mapper.run();
        } else {
            for(Runnable r : mappers) {
                r.run();
            }
        }
    }

    // ---------------------------------------------------------------------- //

    public class OfInt extends AbstractPipeProcessor.OfInt {
        
        private final MutableInt value = new MutableInt();
        
        protected OfInt(IntUnaryOperator mapper) {
            super(false, false);
            MapPipeProcessor.this.init(runnable(mapper));
        }
        
        @Override public boolean pushFrontMap(IntUnaryOperator mapper) {
            return MapPipeProcessor.this.pushFrontMap(runnable(mapper));
        }
        @Override public boolean enqueueMap(IntUnaryOperator mapper) {
            return MapPipeProcessor.this.enqueueMap(runnable(mapper));
        }
        
        @Override public void processInputValue(int value) {
            MapPipeProcessor.this.processInputValue(() -> {
                this.value.setValue(value);
            });
        }
        @Override public boolean hasOutputValue() { return true; }
        @Override protected int retrieveIntOutputValue() {
            return value.intValue();
        }
        @Override protected void clearOutputValue() {}
        @Override public boolean isInactive() { return false; }
        
        private Runnable runnable(IntUnaryOperator mapper) {
            return () -> {
                value.setValue(mapper.applyAsInt(value.intValue()));
            };
        }
    }    
    public class OfLong extends AbstractPipeProcessor.OfLong {
        
        private final MutableLong value = new MutableLong();
        
        protected OfLong(LongUnaryOperator mapper) {
            super(false, false);
            MapPipeProcessor.this.init(runnable(mapper));
        }
        
        @Override public boolean pushFrontMap(LongUnaryOperator mapper) {
            return MapPipeProcessor.this.pushFrontMap(runnable(mapper));
        }
        @Override public boolean enqueueMap(LongUnaryOperator mapper) {
            return MapPipeProcessor.this.enqueueMap(runnable(mapper));
        }
        
        @Override public void processInputValue(long value) {
            MapPipeProcessor.this.processInputValue(() -> {
                this.value.setValue(value);
            });
        }
        @Override public boolean hasOutputValue() { return true; }
        @Override protected long retrieveLongOutputValue() {
            return value.longValue();
        }
        @Override protected void clearOutputValue() {}
        @Override public boolean isInactive() { return false; }
        
        private Runnable runnable(LongUnaryOperator mapper) {
            return () -> {
                value.setValue(mapper.applyAsLong(value.intValue()));
            };
        }
    }    
    public class OfDouble extends AbstractPipeProcessor.OfDouble {
        
        private final MutableDouble value = new MutableDouble();
        
        protected OfDouble(DoubleUnaryOperator mapper) {
            super(false, false);
            MapPipeProcessor.this.init(runnable(mapper));
        }
        
        @Override public boolean pushFrontMap(DoubleUnaryOperator mapper) {
            return MapPipeProcessor.this.pushFrontMap(runnable(mapper));
        }
        @Override public boolean enqueueMap(DoubleUnaryOperator mapper) {
            return MapPipeProcessor.this.enqueueMap(runnable(mapper));
        }
        
        @Override public void processInputValue(double value) {
            MapPipeProcessor.this.processInputValue(() -> {
                this.value.setValue(value);
            });
        }
        @Override public boolean hasOutputValue() { return true; }
        @Override protected double retrieveDoubleOutputValue() {
            return value.doubleValue();
        }
        @Override protected void clearOutputValue() {}
        @Override public boolean isInactive() { return false; }
        
        private Runnable runnable(DoubleUnaryOperator mapper) {
            return () -> {
                value.setValue(mapper.applyAsDouble(value.intValue()));
            };
        }
    }
    
    // ---------------------------------------------------------------------- //
    
    public static OfInt of(IntUnaryOperator mapper) {
        return (new MapPipeProcessor()).new OfInt(mapper);
    }
    public static OfLong of(LongUnaryOperator mapper) {
        return (new MapPipeProcessor()).new OfLong(mapper);
    }
    public static OfDouble of(DoubleUnaryOperator mapper) {
        return (new MapPipeProcessor()).new OfDouble(mapper);
    }
}
