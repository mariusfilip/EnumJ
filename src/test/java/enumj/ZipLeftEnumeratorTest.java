/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.IntStream;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.tuple.Pair;
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
public class ZipLeftEnumeratorTest {
    
    public ZipLeftEnumeratorTest() {
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
        leftZipped = new ZipLeftEnumerator(shortIterator, longIterator);
        rightZipped = new ZipLeftEnumerator(longIterator, shortIterator);
    }
    int shortLen = 3;
    int longLen = 5;
    Iterator<Integer> shortIterator;
    Iterator<Integer> longIterator;
    ZipLeftEnumerator<Integer, Integer> leftZipped;
    ZipLeftEnumerator<Integer, Integer> rightZipped;

    @After
    public void tearDown() {
    }

    @Test
    public void testLeft() {
        for(int i=0; i<shortLen; ++i) {
            assertTrue(leftZipped.hasNext());
            Pair<Integer, Optional<Mutable<Integer>>> val = leftZipped.next();
            assertTrue(val.getRight().isPresent());
            assertEquals(val.getLeft(), Integer.valueOf(i));
            assertEquals(val.getRight().get().getValue(), Integer.valueOf(i));
        }
        assertTrue(!leftZipped.hasNext());
    }    

    @Test
    public void testRight() {
        for(int i=0; i<shortLen; ++i) {
            assertTrue(rightZipped.hasNext());
            Pair<Integer, Optional<Mutable<Integer>>> val = rightZipped.next();
            assertTrue(val.getRight().isPresent());
            assertEquals(val.getRight().get().getValue(), Integer.valueOf(i));
        }
        for(int i=shortLen; i<longLen; ++i) {
            assertTrue(rightZipped.hasNext());
            Pair<Integer, Optional<Mutable<Integer>>> val = rightZipped.next();
            assertTrue(!val.getRight().isPresent());
            assertEquals(val.getLeft(), Integer.valueOf(i));
        }
        assertTrue(!rightZipped.hasNext());
    }    
}
