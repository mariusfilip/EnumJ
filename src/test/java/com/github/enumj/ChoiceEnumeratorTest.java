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

import java.util.Iterator;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChoiceEnumeratorTest {

    public ChoiceEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        shortIterator = IntStream.range(0, shortLen).iterator();
        longIterator = IntStream.range(0, longLen).iterator();
        veryLongIterator = IntStream.range(0, veryLongLen).iterator();
        zipped = new ChoiceEnumerator(this::indexSupply,
                                      this::nextIndex,
                                      shortIterator,
                                      longIterator,
                                      Enumerator.on(veryLongIterator)
                                                .toList());
    }
    int shortLen = 3;
    int longLen = 5;
    int veryLongLen = 7;
    Iterator<Integer> shortIterator;
    Iterator<Integer> longIterator;
    Iterator<Integer> veryLongIterator;
    ChoiceEnumerator<Integer> zipped;

    int index = 0;
    int indexSupply() {
        int crt = index;
        index = (index+1)%3;
        return crt;
    }

    int nextIndex(int index) {
        return (index+1)%3;
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHasNext() {
        assertTrue(zipped.hasNext());
        assertTrue(zipped.hasNext());
    }

    @Test
    public void testNext() {
        for(int i=0; i<shortLen; ++i) {
            assertTrue(zipped.hasNext());
            Integer val = zipped.next();
            assertEquals(val, Integer.valueOf(i));
            assertTrue(zipped.hasNext());
            val = zipped.next();
            assertEquals(val, Integer.valueOf(i));
            assertTrue(zipped.hasNext());
            val = zipped.next();
            assertEquals(val, Integer.valueOf(i));
        }
        for(int i=shortLen; i<longLen; ++i) {
            assertTrue(zipped.hasNext());
            Integer val = zipped.next();
            assertTrue(val >= shortLen);
            assertTrue(zipped.hasNext());
            val = zipped.next();
            assertTrue(val >= shortLen);
        }
        for(int i=longLen; i<veryLongLen; ++i) {
            assertTrue(zipped.hasNext());
            Integer val = zipped.next();
            assertTrue(val >= longLen);
        }
        assertTrue(!zipped.hasNext());
        assertTrue(!zipped.hasNext());
    }    
}
