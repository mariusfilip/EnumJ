/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

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

    /**
     * Test of state method, of class CachedEnumerable.
     */
    @Test
    public void testState() {
        System.out.println("state");
        assertNotNull(new CachedEnumerable(Enumerable.on(1, 2, 3)).state());
    }

    /**
     * Test of disable method, of class CachedEnumerable.
     */
    @Test
    public void testDisable() {
        System.out.println("disable");
        assertTrue(new CachedEnumerable(Enumerable.on(1, 2, 3))
                .disable()
                .isDisabled());
    }

    /**
     * Test of enable method, of class CachedEnumerable.
     */
    @Test
    public void testEnable() {
        System.out.println("enable");
        final CachedEnumerable<Integer> cen =
                new CachedEnumerable(Enumerable.on(1, 2, 3));
        cen.disable();
        assertFalse(cen.enable().isDisabled());
    }

    /**
     * Test of reset method, of class CachedEnumerable.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        assertNotNull(new CachedEnumerable(Enumerable.on(1, 2, 3))
                              .reset());
    }

    /**
     * Test of resize method, of class CachedEnumerable.
     */
    @Test
    public void testResize() {
        System.out.println("resize");
        final CachedEnumerable<Integer> cen = new CachedEnumerable(
                Enumerable.on(1, 2, 3, 4, 5),
                3,
                self -> {});
        assertEquals(5, cen.resize(5).limit);
    }

    /**
     * Test of internalEnumerator method, of class CachedEnumerable.
     */
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
