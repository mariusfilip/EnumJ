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

import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LateBindingEnumerableTest {

    public LateBindingEnumerableTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = NoSuchElementException.class)
    public void testInternalEnumerator() {
        System.out.println("internalEnumerator");
        final LateBindingEnumerable<Integer> lben =
                new LateBindingEnumerable<>();
        assertNull(lben.enumerator());
    }

    @Test
    public void testBind() {
        System.out.println("bind");
        final LateBindingEnumerable<Integer> lben =
                new LateBindingEnumerable<>();
        lben.bind(Enumerable.on(1, 2, 3));
        assertTrue(lben.elementsEqual(Enumerable.on(1, 2, 3)));
    }

    @Test
    public void testBinding() {
        System.out.println("binding");
        final LateBindingEnumerable<Integer> lben =
                new LateBindingEnumerable<>();
        assertFalse(lben.binding().isPresent());
        lben.bind(Enumerable.on(1, 2, 3));
        assertTrue(lben.binding().isPresent());
    }
}
