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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumerableEnumerationTest {

    public EnumerableEnumerationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        enum1 = Enumerator.rangeInt(1, 10);
        enum2 = Enumerator.rangeInt(1, 10);
        en = new EnumerableEnumeration<>(enum2);
    }
    private Enumerator<Integer> enum1;
    private Enumerator<Integer> enum2;
    private EnumerableEnumeration<Integer> en;

    @After
    public void tearDown() {
    }

    @Test
    public void testHasMoreElements() {
        System.out.println("hasMoreElements");
        while(enum1.hasNext()) {
            assertTrue(en.hasMoreElements());
            enum1.next();
            en.nextElement();
        }
        assertTrue(!en.hasMoreElements());
    }

    @Test
    public void testNextElement() {
        System.out.println("nextElement");
        while(enum1.hasNext()) {
            assertTrue(en.hasMoreElements());
            assertEquals(enum1.next(), en.nextElement());
        }
    }
}
