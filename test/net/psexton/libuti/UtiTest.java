/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author PSexton
 */
public class UtiTest {

    /**
     * Test of toString method, of class Uti.
     */
    @Test
    public void testToString() {
        Uti instance = new Uti("foo");
        String expResult = "foo";
        String result = instance.toString();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Uti.
     */
    @Test
    public void testEquals() {
        // Equal objects should return true.
        // Nonequal objects should produce false.
        Uti instance = new Uti("foo");
        
        Uti equalUti = new Uti("foo");
        Uti nonequalUti = new Uti("bar");
        Object nullObj = null;
        String nonUti = "foo";
        
        assertTrue(instance.equals(instance)); // matches self
        assertTrue(instance.equals(equalUti)); // matches equal Uti
        assertFalse(instance.equals(nonequalUti)); // does not match nonequal Uti
        assertFalse(instance.equals(nullObj)); // gracefully handles null
        assertFalse(instance.equals(nonUti)); // gracefully handle other classes
    }

    /**
     * Test of hashCode method, of class Uti.
     */
    @Test
    public void testHashCode() {
        // Equal objects should produce equal hashes.
        // Nonequal objects should produce nonequal hashes.
        
        Uti instance1a = new Uti("foo");
        Uti instance1b = new Uti("foo");
        Uti instance2 = new Uti("bar");
        assertEquals(instance1a.hashCode(), instance1b.hashCode());
        assertFalse(instance1a.hashCode() == instance2.hashCode());
    }
    
    @Test
    public void testConformsToSelf() {
        Uti instance = new Uti("public.image");
        assertTrue(instance.conformsTo(instance));
    }
    
    @Test
    public void testConformsToParent() {
        Uti instance = new Uti("public.png");
        Uti parent = new Uti("public.image");
        assertTrue(instance.conformsTo(parent));
    }
    
    @Test
    public void testDoesNotConformToNonParent() {
        Uti instance = new Uti("public.png");
        Uti nonParent = new Uti("public.movie");
        assertFalse(instance.conformsTo(nonParent));
    }
}
