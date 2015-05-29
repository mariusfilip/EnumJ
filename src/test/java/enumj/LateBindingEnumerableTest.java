/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marius
 */
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

    /**
     * Test of internalEnumerator method, of class LateBindingEnumerable.
     */
    @Test(expected = NoSuchElementException.class)
    public void testInternalEnumerator() {
        System.out.println("internalEnumerator");
        final LateBindingEnumerable<Integer> lben =
                new LateBindingEnumerable<>();
        assertNull(lben.enumerator());
    }

    /**
     * Test of bind method, of class LateBindingEnumerable.
     */
    @Test
    public void testBind() {
        System.out.println("bind");
        final LateBindingEnumerable<Integer> lben =
                new LateBindingEnumerable<>();
        lben.bind(Enumerable.on(1, 2, 3));
        assertTrue(lben.elementsEqual(Enumerable.on(1, 2, 3)));
    }

    /**
     * Test of binding method, of class LateBindingEnumerable.
     */
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
