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

import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class OutTest {
    
    public OutTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        out = Out.empty();
    }
    private Out<String> out;

    @After
    public void tearDown() {
        out = null;
    }

    @Test
    public void testGet() {
        System.out.println("get");
        out.set("titi");
        assertEquals("titi", out.get());
    }

    @Test(expected=NoSuchElementException.class)
    public void testGetNoValue() {
        System.out.println("getNoValue");
        assertNotNull(out.get());
    }

    @Test
    public void testSet() {
        System.out.println("set");
        out.set("titi");
        assertEquals("titi", out.get());
        out.set("fifi");
        assertEquals("fifi", out.get());
    }

    @Test
    public void testHasValue() {
        System.out.println("hasValue");
        assertFalse(out.hasValue());
        out.set("titi");
        assertTrue(out.hasValue());
    }

    @Test
    public void testClear() {
        System.out.println("clear");
        out.set("titi");
        assertTrue(out.hasValue());
        out.clear();
        assertFalse(out.hasValue());
    }

    @Test
    public void testEmpty() {
        System.out.println("empty");
        assertEquals(out, Out.empty());
    }

    @Test
    public void testEquals() {
        System.out.println("equals");
        assertEquals(out, Out.empty());
        out.set("titi");
        final Out<String> other = Out.empty();
        other.set("titi");
        assertEquals(out, other);
        other.set("fifi");
        assertFalse(out.equals(other));
        assertFalse(out.equals("titi"));
    }

    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        final int code0 = out.hashCode();
        out.set("titi");
        final int code1 = out.hashCode();
        out.clear();
        final int code2 = out.hashCode();
        assertEquals(code0, code2);
        assertFalse(code0 == code1);
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        assertEquals("<none>", out.toString());
    }
}
