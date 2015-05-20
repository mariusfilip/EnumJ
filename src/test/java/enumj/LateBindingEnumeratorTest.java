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

import java.util.Iterator;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marius Filip
 */
public class LateBindingEnumeratorTest {
    
    public LateBindingEnumeratorTest() {
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

    /**
     * Test of internalHasNext method, of class LateBindingEnumerator.
     */
    @Test
    public void testInternalHasNext() {
        System.out.println("internalHasNext");
        final LateBindingEnumerator<Integer> lben = 
                Enumerator.ofLateBinding(Integer.class);
        lben.bind(Enumerator.on(1, 2, 3));
        assertTrue(lben.hasNext());
    }

    /**
     * Test of internalNext method, of class LateBindingEnumerator.
     */
    @Test
    public void testInternalNext() {
        System.out.println("internalNext");
        final LateBindingEnumerator<Integer> lben = 
                Enumerator.ofLateBinding(Integer.class);
        lben.bind(Enumerator.on(1, 2, 3));
        assertTrue(lben.elementsEqual(Enumerator.on(1, 2, 3)));
    }

    /**
     * Test of cleanup method, of class LateBindingEnumerator.
     */
    @Test
    public void testCleanup() {
        System.out.println("cleanup");
        final LateBindingEnumerator<Integer> lben = 
                Enumerator.ofLateBinding(Integer.class);
        lben.bind(Enumerator.on(1, 2, 3));
        assertEquals(3, lben.count());
    }

    /**
     * Test of bind method, of class LateBindingEnumerator.
     */
    @Test
    public void testBind() {
        System.out.println("bind");
        final LateBindingEnumerator<Integer> lben = 
                Enumerator.ofLateBinding(Integer.class);
        lben.bind(Enumerator.on(1, 2, 3));
        assertTrue(lben.binding().isPresent());
    }

    /**
     * Test of binding method, of class LateBindingEnumerator.
     */
    @Test
    public void testBinding() {
        System.out.println("binding");
        final LateBindingEnumerator<Integer> lben = 
                Enumerator.ofLateBinding(Integer.class);
        final Iterator<Integer> it = Enumerator.on(1, 2, 3);
        lben.bind(it);
        assertSame(it, lben.binding().get());
    }
}
