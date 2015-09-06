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

public class CachedEnumerableStateTest {

    public CachedEnumerableStateTest() {
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

    @Test
    public void testIsDisabled() {
        System.out.println("isDisabled");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          null,
                                          1000,
                                          self -> {});
        assertFalse(ces.isDisabled());
    }

    @Test
    public void testEnable() {
        System.out.println("enable");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          null,
                                          1000,
                                          self -> {});
        assertFalse(ces.enable().isDisabled());
    }

    @Test
    public void testDisable() {
        System.out.println("disable");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          null,
                                          1000,
                                          self -> {});
        assertTrue(ces.disable().isDisabled());
    }

    @Test
    public void testReset() {
        System.out.println("reset");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          null,
                                          1000,
                                          self -> {});
        assertEquals(ces.limit, ces.reset().limit);
    }

    @Test
    public void testResize() {
        System.out.println("resize");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          null,
                                          1000,
                                          self -> {});
        assertEquals(ces.limit+1000, ces.resize(2000).limit);
    }

    @Test
    public void testEnumerator() {
        System.out.println("enumerator");
        for(int count=1; count<21; ++count) {
            for(int limit=1; limit<21; ++limit) {
                final CachedEnumerableState<Integer> ces =
                        new CachedEnumerableState(Enumerable.rangeInt(0, count),
                                                  null,
                                                  limit,
                                                  self -> {});
                assertTrue(Enumerator.rangeInt(0, count)
                                     .elementsEqual(ces.enumerator()));
                final CachedEnumerableState<Integer> ces0 =
                        new CachedEnumerableState(Enumerable.empty(),
                                                  null,
                                                  limit,
                                                  self -> {});
                assertTrue(Enumerator.empty()
                                     .elementsEqual(ces0.enumerator()));
            }
        }
        final CachedEnumerableState<Integer> ces_ =
                new CachedEnumerableState(
                        Enumerable.on(1, 2, 3, 4, 5),
                        null,
                        3,
                        self -> { throw new IllegalArgumentException(); });
        assertTrue(Enumerator.on(1, 2, 3, 4, 5)
                             .elementsEqual(ces_.enumerator()));
    }    
}
