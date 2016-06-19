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
package enumj;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

interface Value<T> {

    public static final int NONE    = -1;    
    public static final int GENERIC = 0;
    public static final int INT     = 1;
    public static final int LONG    = 2;
    public static final int DOUBLE  = 3;
    
    public boolean isPresent();
    public boolean intIsPresent();
    public boolean longIsPresent();
    public boolean doubleIsPresent();

    public T get() throws NoSuchElementException;
    public int getInt() throws NoSuchElementException;
    public long getLong() throws NoSuchElementException;
    public double getDouble() throws NoSuchElementException;
    
    public void set(T value);
    public void setInt(int value);
    public void setLong(long value);
    public void setDouble(double value);
    public void setValue(Value<? extends T> value);
    
    public int  getType();
    public void clear();
}

final class SingleValue<T> implements Value<T> {

    private T       value;
    private int     intValue;
    private long    longValue;
    private double  doubleValue;
    private int     type;
    
    static final Consumer<SingleValue> C        = SingleValue::convert;
    static final Consumer<SingleValue> C_INT    = SingleValue::convertInt;
    static final Consumer<SingleValue> C_LONG   = SingleValue::convertLong;
    static final Consumer<SingleValue> C_DOUBLE = SingleValue::convertDouble;
    static final Consumer[] Cs = {
        C,
        C_INT,
        C_LONG,
        C_DOUBLE
    };    
    static final BiConsumer<Value,Value> T
            = SingleValue::transfer;
    static final BiConsumer<Value,Value> T_INT
            = SingleValue::transferInt;
    static final BiConsumer<Value,Value> T_LONG
            = SingleValue::transferLong;
    static final BiConsumer<Value,Value> T_DOUBLE
            = SingleValue::transferDouble;
    static final BiConsumer[] Ts = {
        T,
        T_INT,
        T_LONG,
        T_DOUBLE
    };

    public SingleValue() { clear(); }
    public SingleValue(T value) { set(value); }
    public SingleValue(int value) { setInt(value); }
    public SingleValue(long value) { setLong(value); }
    public SingleValue(double value) { setDouble(value); }
    public SingleValue(Value<? extends T> value) { setValue(value); }
    
    @Override public boolean isPresent() { return type != NONE; }
    @Override public boolean intIsPresent() { return type == INT; }
    @Override public boolean longIsPresent() { return type == LONG; }
    @Override public boolean doubleIsPresent() { return type == DOUBLE; }

    @Override public T get() throws NoSuchElementException {
        if (type == NONE) {
            throw new NoSuchElementException();
        }
        if (value != null) {
            return value;
        }
        Cs[type].accept(this);
        return value;
    }
    @Override public int getInt()  throws NoSuchElementException {
        if (type != INT) {
            throw new NoSuchElementException();
        }
        return intValue;
    }
    @Override public long getLong()  throws NoSuchElementException {
        if (type != LONG) {
            throw new NoSuchElementException();
        }
        return longValue;
    }
    @Override public double getDouble()  throws NoSuchElementException {
        if (type != DOUBLE) {
            throw new NoSuchElementException();
        }
        return doubleValue;
    }

    @Override public void set(T elem) {
        this.value = elem;
        this.type = GENERIC;
    }
    @Override public void setInt(int elem) {
        this.intValue = elem;
        this.type = INT;
        this.value = null;
    }
    @Override public void setLong(long elem) {
        this.longValue = elem;
        this.type = LONG;
        this.value = null;
    }
    @Override public void setDouble(double elem) {
        this.doubleValue = elem;
        this.type = DOUBLE;
        this.value = null;
    }
    @Override public void setValue(Value<? extends T> elem) {
        Ts[elem.getType()].accept(elem, this);
    }

    @Override public int  getType() { return type; }
    @Override public void clear() {
        value = null;
        type = NONE;
    }
    
    @Override public int     hashCode() {
        return Objects.hash(isPresent(), isPresent() ? get(): null);
    }
    @Override public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof SingleValue)) { return false; }

        SingleValue other = (SingleValue)obj;
        if (this.isPresent() != other.isPresent()) { return false; }
        return !this.isPresent() && Objects.equals(this.get(), other.get());
    }
    
    static void convert(SingleValue value) {}
    static void convertInt(SingleValue value) {
        value.value = value.intValue;
    }
    static void convertLong(SingleValue value) {
        value.value = value.longValue;
    }
    static void convertDouble(SingleValue value) {
        value.value = value.doubleValue;
    }
    static void transfer(Value from, Value to) {
        to.set(from.get());
    }
    static void transferInt(Value from, Value to) {
        to.setInt(from.getInt());
    }
    static void transferLong(Value from, Value to) {
        to.setInt(from.getInt());
    }
    static void transferDouble(Value from, Value to) {
        to.setDouble(from.getDouble());
    }    
}
