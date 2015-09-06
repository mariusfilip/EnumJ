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

public class ShareableEnumeratorTest {

    public ShareableEnumeratorTest() {
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
    public void testAsShareable() {
        System.out.println("asShareable");
        final Enumerator<Integer> en = Enumerator.on(1, 2, 3).asShareable();
        final Enumerator<Integer> sen = en.asShareable();
        assertSame(en, en.asShareable());
    }

    @Test
    public void testShare_0args() {
        System.out.println("share 0 args");
        assertNotNull(Enumerator.on(1, 2, 3)
                                .asShareable()
                                .share());
    }

    @Test(expected=IllegalStateException.class)
    public void testShare_int() {
        System.out.println("share");
        final ShareableEnumerator<Integer> sen = Enumerator.on(1, 2, 3)
                                                           .asShareable();
        assertTrue(sen.hasNext());
        assertNotNull(sen.share(1));
    }

    @Test(expected=IllegalStateException.class)
    public void testInternalHasNext() {
        System.out.println("internalHasNext");
        final ShareableEnumerator<Integer> sen = Enumerator.on(1, 2, 3)
                                                           .asShareable();
        assertNotNull(sen.share(1));
        assertTrue(sen.hasNext());
    }

    @Test(expected=IllegalStateException.class)
    public void testInternalNext() {
        System.out.println("internalNext");
        final ShareableEnumerator<Integer> sen = Enumerator.on(1, 2, 3)
                                                           .asShareable();
        assertNotNull(sen.share(1));
        assertNotNull(sen.next());
    }

    @Test
    public void testCleanup() {
        System.out.println("cleanup");
        final Enumerator<Integer> sen = Enumerator.on(1, 2, 3)
                                                  .asShareable();
        sen.forEach(i -> System.out.println("Enumerating shareable: " + i));
        assertFalse(sen.hasNext());
    }

    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        final ShareableEnumerator<Integer> sen = Enumerator.on(1, 2, 3)
                                                           .asShareable();
        assertTrue(sen.share().hasNext());
    }
}
