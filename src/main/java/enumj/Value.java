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

public class Value<T> {

    private T       value;
    private boolean isPresent;    
    private int     intValue;    private boolean intIsPresent;
    private long    longValue;   private boolean longIsPresent;
    private double  doubleValue; private boolean doubleIsPresent;

    protected Value() {}
    protected Value(T value) {
        this.value = value;
        this.isPresent = true;
    }
    protected Value(int value) {
        this.intValue = value;
        this.isPresent = this.intIsPresent = true;
    }
    protected Value(long value) {
        this.longValue = value;
        this.isPresent = this.longIsPresent = true;
    }
    protected Value(double value) {
        this.doubleValue = value;
        this.isPresent = this.doubleIsPresent = true;
    }
    protected Value(Value<? extends T> value) {
        if (value.intIsPresent()) {
            setInt(value.getInt());
        } else if (value.longIsPresent()) {
            setLong(value.getLong());
        } else if (value.doubleIsPresent()) {
            setDouble(value.getDouble());
        } else if (value.isPresent()) {
            set(value.get());
        }
    }
    
    public boolean isPresent() { return isPresent; }
    public boolean intIsPresent() { return intIsPresent; }
    public boolean longIsPresent() { return longIsPresent; }
    public boolean doubleIsPresent() { return doubleIsPresent; }

    public final T get() {
        if (!isPresent) {
            throw new NoSuchElementException();
        }
        return value = castIf();
    }
    private T castIf() {
        if (value != null) { return value; }
        if (intIsPresent && value == null) {
            final Integer i = intValue;
            return (T)(Object)i;
        }
        if (longIsPresent && value == null) {
            final Long l = longValue;
            return (T)(Object)l;
        }
        if (doubleIsPresent && value == null) {
            final Double d = doubleValue;
            return (T)(Object)d;
        }
        return value;
    }
    public final int getInt() {
        if (!intIsPresent) {
            throw new NoSuchElementException();
        }
        return intValue;
    }
    public final long getLong() {
        if (!longIsPresent) {
            throw new NoSuchElementException();
        }
        return longValue;
    }
    public final double getDouble() {
        if (!doubleIsPresent) {
            throw new NoSuchElementException();
        }
        return doubleValue;
    }

    public final void set(T elem) {
        this.value = elem;
        this.isPresent = true;
        this.intIsPresent = false;
        this.longIsPresent = false;
        this.doubleIsPresent = false;
    }
    public final void setInt(int elem) {
        this.intValue = elem;
        this.isPresent = this.intIsPresent = true;
        this.value = null;
        this.longIsPresent = false;
        this.doubleIsPresent = false;
    }
    public final void setLong(long elem) {
        this.longValue = elem;
        this.isPresent = this.longIsPresent = true;
        this.value = null;
        this.intIsPresent = false;
        this.doubleIsPresent = false;
    }
    public final void setDouble(double elem) {
        this.doubleValue = elem;
        this.isPresent = this.doubleIsPresent = true;
        this.value = null;
        this.intIsPresent = false;
        this.longIsPresent = false;
    }

    public final void clear() {
        value = null;
        isPresent = false;
        intIsPresent = false;
        longIsPresent = false;
        doubleIsPresent = false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isPresent(), isPresent() ? get(): null);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof Value)) { return false; }

        Value other = (Value)obj;
        if (this.isPresent() != other.isPresent()) { return false; }
        if (this.isPresent() &&
            !Objects.equals(this.get(), other.get())) { return false; }
        return this.intIsPresent == other.intIsPresent
               && this.longIsPresent == other.longIsPresent
               && this.doubleIsPresent == other.doubleIsPresent;
    }
}
