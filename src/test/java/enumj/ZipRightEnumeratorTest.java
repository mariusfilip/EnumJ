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
public class ZipRightEnumeratorTest {
    
    public ZipRightEnumeratorTest() {
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
        leftZipped = new ZipRightEnumerator(shortIterator, longIterator);
        rightZipped = new ZipRightEnumerator(longIterator, shortIterator);
    }
    int shortLen = 3;
    int longLen = 5;
    Iterator<Integer> shortIterator;
    Iterator<Integer> longIterator;
    ZipRightEnumerator<Integer, Integer> leftZipped;
    ZipRightEnumerator<Integer, Integer> rightZipped;

    @After
    public void tearDown() {
    }

    @Test
    public void testLeft() {
        for(int i=0; i<shortLen; ++i) {
            assertTrue(leftZipped.hasNext());
            Pair<Optional<Integer>, Integer> val = leftZipped.next();
            assertTrue(val.getLeft().isPresent());
            assertEquals(val.getLeft().get(), Integer.valueOf(i));
            assertEquals(val.getRight(), Integer.valueOf(i));
        }
        for(int i=shortLen; i<longLen; ++i) {
            assertTrue(leftZipped.hasNext());
            Pair<Optional<Integer>, Integer> val = leftZipped.next();
            assertTrue(!val.getLeft().isPresent());
            assertEquals(val.getRight(), Integer.valueOf(i));
        }
        assertTrue(!leftZipped.hasNext());
    }    

    @Test
    public void testRight() {
        for(int i=0; i<shortLen; ++i) {
            assertTrue(rightZipped.hasNext());
            Pair<Optional<Integer>, Integer> val = rightZipped.next();
            assertTrue(val.getLeft().isPresent());
            assertEquals(val.getLeft().get(), Integer.valueOf(i));
            assertEquals(val.getRight(), Integer.valueOf(i));
        }
        assertTrue(!rightZipped.hasNext());
    }    
}
