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

public class PipeEnumerableTest {

    public PipeEnumerableTest() {
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
    public void testInternalOnceOnly() {
        System.out.println("internalOnceOnly");
        final Enumerable<Integer> once =
                Enumerator.on(1, 2, 3)
                          .asEnumerable()
                          .map(x -> x);
        assertTrue(once.onceOnly());
        final Enumerable<Integer> more =
                once.map(x -> x);
        more.onceOnly();
        assertTrue(more.onceOnly());
    }

    @Test
    public void testInternalEnumerator() {
        System.out.println("internalEnumerator");
        assertTrue(Enumerable.on(1, 2, 3)
                             .enumerator()
                             .elementsEqual(Enumerator.on(1, 2, 3)));
    }

    @Test
    public void testOf() {
        System.out.println("of");
        assertTrue(Enumerable.of(Enumerator.on(1, 2, 3))
                             .elementsEqual(Enumerable.on(1, 2, 3)));
    }

    @Test
    public void testConcat() {
        System.out.println("concat");
        assertTrue(Enumerable.on(1, 2, 3)
                             .concat(Enumerable.on(4, 5))
                             .elementsEqual(Enumerable.on(1, 2, 3, 4, 5)));
    }

    @Test
    public void testDistinct() {
        System.out.println("distinct");
        assertTrue(Enumerable.on(1, 1, 2, 2, 3, 3)
                             .distinct()
                             .elementsEqual(Enumerable.on(1, 2, 3)));
    }

    @Test
    public void testFilter() {
        System.out.println("filter");
        assertEquals(1, Enumerable.on(1, 2, 3)
                                  .filter(x -> x>2)
                                  .enumerator()
                                  .count());
    }

    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
        assertEquals(6, Enumerable.on(1, 2, 3)
                                  .flatMap(x -> Enumerable.on(x, x+1))
                                  .enumerator()
                                  .count());
    }

    @Test
    public void testLimit() {
        System.out.println("limit");
        assertEquals(2, Enumerable.on(1, 2, 3)
                                  .limit(2)
                                  .enumerator()
                                  .count());
    }

    @Test
    public void testLimitWhile() {
        System.out.println("limitWhile");
        assertEquals(2, Enumerable.on(1, 2, 3)
                                  .limitWhile(x -> x < 3)
                                  .enumerator()
                                  .count());
    }

    @Test
    public void testMap_Enumerable_Function() {
        System.out.println("map");
        assertTrue(Enumerable.on(1, 2, 3)
                             .map(x -> x+1)
                             .elementsEqual(Enumerable.on(2, 3, 4)));
    }

    @Test
    public void testMap_Enumerable_BiFunction() {
        System.out.println("map");
        assertTrue(Enumerable.on(1, 2, 3)
                             .map((x,i) -> x+i.intValue())
                             .elementsEqual(Enumerable.on(1, 3, 5)));
    }

    @Test
    public void testPeek() {
        System.out.println("peek");
        assertTrue(Enumerable.on(1, 2, 3)
                             .peek(x -> {})
                             .elementsEqual(Enumerable.on(1, 2, 3)));
    }

    @Test
    public void testSkip() {
        System.out.println("skip");
        assertEquals(1, Enumerable.on(1, 2, 3)
                                  .skip(2)
                                  .enumerator()
                                  .count());
    }

    @Test
    public void testSkipWhile() {
        System.out.println("skipWhile");
        assertEquals(1, Enumerable.on(1, 2, 3)
                                  .skipWhile(x -> x < 3)
                                  .enumerator()
                                  .count());
    }

    @Test
    public void testTakeWhile() {
        System.out.println("takeWhile");
        assertEquals(2, Enumerable.on(1, 2, 3)
                                  .takeWhile(x -> x < 3)
                                  .enumerator()
                                  .count());
    }

    @Test
    public void testZipAll() {
        System.out.println("zipAll");
        assertTrue(Enumerable.on(1, 2, 3)
                             .zipBoth(Enumerable.on(2, 3, 4))
                             .map(p -> p.getLeft()+p.getRight())
                             .elementsEqual(Enumerable.on(3, 5, 7)));
    }
}
