/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
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
public class UtilsTest {
    
    public UtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        new Utils();
        new Messages();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of ensureNotNull method, of class Utils.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEnsureNotNull() {
        System.out.println("ensureNotNull");
        Utils.ensureNotNull("not null", "");
        Utils.ensureNotNull(null, "");
    }

    /**
     * Test of ensureNonNegative method, of class Utils.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEnsureNonNegative() {
        System.out.println("ensureNonNegative");
        Utils.ensureNonNegative(1, "");
        Utils.ensureNonNegative(-1, "");
    }

    /**
     * Test of ensureLessThan method, of class Utils.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEnsureLessThan() {
        System.out.println("ensureLessThan");
        Utils.ensureLessThan(1, 2, "");
        Utils.ensureLessThan(1, 1, "");
    }

    /**
     * Test of ensureNonEnumerating method, of class Utils.
     */
    @Test(expected = IllegalStateException.class)
    public void testEnsureNonEnumerating_Enumerator() {
        System.out.println("ensureNonEnumerating");
        final Enumerator<Integer> e = Enumerator.on(1, 2, 3);
        e.next();
        Utils.ensureNonEnumerating(e);
    }

    /**
     * Test of ensureNonEnumerating method, of class Utils.
     */
    @Test(expected = IllegalStateException.class)
    public void testEnsureNonEnumerating_Enumerable() {
        System.out.println("ensureNonEnumerating");
        final Enumerable<Integer> e = Enumerable.on(1, 2, 3);
        Utils.ensureNonEnumerating(e);
        final Iterator<Integer> it = e.iterator();
        Utils.ensureNonEnumerating(e);
    }
    
}
