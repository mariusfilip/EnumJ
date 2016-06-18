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

interface Value<T> {
    
    public enum Type {
        GENERIC,
        INT,
        LONG,
        DOUBLE,
        NONE
    }
    
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
    
    public Type getType();
    public void clear();
}

final class SingleValue<T> implements Value<T> {

    private T       value;
    private int     intValue;
    private long    longValue;
    private double  doubleValue;
    private Type    type;

    public SingleValue() { clear(); }
    public SingleValue(T value) { set(value); }
    public SingleValue(int value) { setInt(value); }
    public SingleValue(long value) { setLong(value); }
    public SingleValue(double value) { setDouble(value); }
    public SingleValue(SingleValue<? extends T> value) { setValue(value); }
    
    @Override public boolean isPresent() { return type != Type.NONE; }
    @Override public boolean intIsPresent() { return type == Type.INT; }
    @Override public boolean longIsPresent() { return type == Type.LONG; }
    @Override public boolean doubleIsPresent() { return type == Type.DOUBLE; }

    @Override public T get() throws NoSuchElementException {
        if (type == Type.NONE) {
            throw new NoSuchElementException();
        }
        return value = castIf();
    }
    @Override public int getInt()  throws NoSuchElementException {
        if (type != Type.INT) {
            throw new NoSuchElementException();
        }
        return intValue;
    }
    @Override public long getLong()  throws NoSuchElementException {
        if (type != Type.LONG) {
            throw new NoSuchElementException();
        }
        return longValue;
    }
    @Override public double getDouble()  throws NoSuchElementException {
        if (type != Type.DOUBLE) {
            throw new NoSuchElementException();
        }
        return doubleValue;
    }

    @Override public void set(T elem) {
        this.value = elem;
        this.type = Type.GENERIC;
    }
    @Override public void setInt(int elem) {
        this.intValue = elem;
        this.type = Type.INT;
        this.value = null;
    }
    @Override public void setLong(long elem) {
        this.longValue = elem;
        this.type = Type.LONG;
        this.value = null;
    }
    @Override public void setDouble(double elem) {
        this.doubleValue = elem;
        this.type = Type.DOUBLE;
        this.value = null;
    }
    @Override public void setValue(Value<? extends T> elem) {
        switch(elem.getType()) {
            case GENERIC: set(elem.get()); break;
            case INT: setInt(elem.getInt()); break;
            case LONG: setLong(elem.getLong()); break;
            case DOUBLE: setDouble(elem.getDouble()); break;
            case NONE: clear();
            default:
                throw new IllegalArgumentException(
                        "Illegal value type: " + elem.getType());
        }
    }

    @Override public Type getType() { return type; }
    @Override public void clear() {
        value = null;
        type = Type.NONE;
    }
    
    @Override public int hashCode() {
        return Objects.hash(isPresent(), isPresent() ? get(): null);
    }
    @Override public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof SingleValue)) { return false; }

        SingleValue other = (SingleValue)obj;
        if (this.isPresent() != other.isPresent()) { return false; }
        return !this.isPresent() && Objects.equals(this.get(), other.get());
    }

    private T castIf() {
        if (value != null) { return value; }
        switch(type) {
            case GENERIC: return value;
            case INT:
                final Integer i = intValue;
                return (T)(Object)i;
            case LONG:
                final Long l = longValue;
                return (T)(Object)l;
            case DOUBLE:
                final Double d = doubleValue;
                return (T)(Object)d;
            case NONE:
                throw new UnsupportedOperationException("Logic error");
            default:
                throw new UnsupportedOperationException(
                        "Unsupported value type: " + type);
        }
    }
}
