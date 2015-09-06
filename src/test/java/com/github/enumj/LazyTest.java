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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LazyTest {

    public LazyTest() {
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
    public void testGet() {
        System.out.println("get");
        assertEquals("something", new Lazy(() -> "something").get());
    }

    @Test(expected=ArithmeticException.class)
    public void testGet_ThrowRuntimeException() {
        System.out.println("get throw runtime exception");
        assertEquals("something",
                     new Lazy(LazyTest::throwArithmeticException).get());
    }

    @Test(expected=Error.class)
    public void testGet_ThrowError() {
        System.out.println("get throw error");
        assertEquals("something",
                     new Lazy(LazyTest::throwError).get());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testGet_ThrowLazyException() {
        System.out.println("get throw lazy exception");
        assertEquals("something",
                     new Lazy(LazyTest::throwLazyException).get());
    }

    private static String throwArithmeticException() {
        throw new ArithmeticException();
    }

    private static String throwError() {
        throw new Error();
    }

    private static String throwLazyException() {
        throw new LazyException("", new IOException());
    }

    @Test
    public void testInitialize() {
        System.out.println("initialize");
        final AtomicInteger count = new AtomicInteger(0);
        final Lazy<String> lazy = new Lazy(() -> {
            count.incrementAndGet();
            return "something";
        });
        for(int i=0; i<10; ++i) {
            assertEquals("something", lazy.get());
        }
        assertEquals(1, count.get());
    }    
}
