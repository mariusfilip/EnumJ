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
package com.github.enumj;

import java.util.Enumeration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumerationEnumeratorTest {

    public EnumerationEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        enum1 = Enumerator.rangeInt(0, 20);
        enum2 = Enumerator.rangeInt(0, 20);
        en = enum2.asEnumeration();
        enum3 = Enumerator.of(en);
    }
    private Enumerator<Integer> enum1;
    private Enumerator<Integer> enum2;
    private Enumeration<Integer> en;
    private Enumerator<Integer> enum3;

    @After
    public void tearDown() {
        enum1 = enum2 = enum3 = null;
        en = null;
    }

    @Test
    public void testMayContinue() {
        System.out.println("mayContinue");
        while(enum3.hasNext()) {
            assertTrue(enum1.hasNext());
            enum3.next();
            enum1.next();
        }
        assertTrue(!enum1.hasNext());
    }

    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        while(enum3.hasNext()) {
            assertTrue(enum1.hasNext());
            assertEquals(enum3.next(), enum1.next());
        }
        assertTrue(!enum1.hasNext());
    }
}
