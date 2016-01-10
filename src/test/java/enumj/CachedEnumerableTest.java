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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CachedEnumerableTest {

    public CachedEnumerableTest() {
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
    public void testState() {
        System.out.println("state");
        assertNotNull(new CachedEnumerable(Enumerable.on(1, 2, 3)).state());
    }

    @Test
    public void testDisable() {
        System.out.println("disable");
        assertTrue(new CachedEnumerable(Enumerable.on(1, 2, 3))
                .disable()
                .isDisabled());
    }

    @Test
    public void testEnable() {
        System.out.println("enable");
        final CachedEnumerable<Integer> cen =
                new CachedEnumerable(Enumerable.on(1, 2, 3));
        cen.disable();
        assertFalse(cen.enable().isDisabled());
    }

    @Test
    public void testReset() {
        System.out.println("reset");
        assertNotNull(new CachedEnumerable(Enumerable.on(1, 2, 3))
                              .reset());
    }

    @Test
    public void testResize() {
        System.out.println("resize");
        final CachedEnumerable<Integer> cen = new CachedEnumerable(
                Enumerable.on(1, 2, 3, 4, 5),
                3,
                self -> {});
        assertEquals(5, cen.resize(5).limit);
    }

    @Test
    public void testInternalEnumerator() {
        System.out.println("internalEnumerator");
        final CachedEnumerable<Integer> cen =
                new CachedEnumerable(Enumerable.on(1, 2, 3, 4, 5),
                                     3,
                                     self -> {});
        assertTrue(cen.elementsEqual(Enumerable.on(1, 2, 3, 4, 5)));
    }    
}
