/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.function.Supplier;
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
public class LazyEnumerableTest {
    
    public LazyEnumerableTest() {
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
     * Test of internalEnumerator method, of class LazyEnumerable.
     */
    @Test
    public void testInternalEnumerator() {
        System.out.println("internalEnumerator");
        final LazyEnumerable<Integer> lazy = LazyEnumerable.of(() ->
                Enumerable.on(1, 2, 3));
        assertTrue(lazy.elementsEqual(Enumerable.on(1, 2, 3)));
    }

    /**
     * Test of of method, of class LazyEnumerable.
     */
    @Test
    public void testOf() {
        System.out.println("of");
        assertNotNull(LazyEnumerable.of(() -> Enumerable.on(1, 2, 3)));
    }
    
}
