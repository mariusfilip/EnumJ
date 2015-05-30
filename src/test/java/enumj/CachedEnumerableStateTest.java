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

    /**
     * Test of isDisabled method, of class CachedEnumerableState.
     */
    @Test
    public void testIsDisabled() {
        System.out.println("isDisabled");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          1000,
                                          () -> {});
        assertFalse(ces.isDisabled());
    }

    /**
     * Test of enable method, of class CachedEnumerableState.
     */
    @Test
    public void testEnable() {
        System.out.println("enable");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          1000,
                                          () -> {});
        assertFalse(ces.enable().isDisabled());
    }

    /**
     * Test of disable method, of class CachedEnumerableState.
     */
    @Test
    public void testDisable() {
        System.out.println("disable");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          1000,
                                          () -> {});
        assertTrue(ces.disable().isDisabled());
    }

    /**
     * Test of reset method, of class CachedEnumerableState.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          1000,
                                          () -> {});
        assertEquals(ces.limit, ces.reset().limit);
    }

    /**
     * Test of resize method, of class CachedEnumerableState.
     */
    @Test
    public void testResize() {
        System.out.println("resize");
        final CachedEnumerableState<Integer> ces =
                new CachedEnumerableState(Enumerable.on(1, 2, 3),
                                          1000,
                                          () -> {});
        assertEquals(ces.limit+1000, ces.resize(2000).limit);
    }

    /**
     * Test of enumerator method, of class CachedEnumerableState.
     */
    @Test
    public void testEnumerator() {
        System.out.println("enumerator");
        for(int count=1; count<21; ++count) {
            for(int limit=1; limit<21; ++limit) {
                final CachedEnumerableState<Integer> ces =
                        new CachedEnumerableState(Enumerable.rangeInt(0, count),
                                                  limit,
                                                  () -> {});
                assertTrue(Enumerator.rangeInt(0, count)
                                     .elementsEqual(ces.enumerator()));
                final CachedEnumerableState<Integer> ces0 =
                        new CachedEnumerableState(Enumerable.empty(),
                                                  limit,
                                                  () -> {});
                assertTrue(Enumerator.empty()
                                     .elementsEqual(ces0.enumerator()));
            }
        }
        final CachedEnumerableState<Integer> ces_ =
                new CachedEnumerableState(
                        Enumerable.on(1, 2, 3, 4, 5),
                        3,
                        () -> { throw new IllegalArgumentException(); });
        assertTrue(Enumerator.on(1, 2, 3, 4, 5)
                             .elementsEqual(ces_.enumerator()));
    }
    
}
