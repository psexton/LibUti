/*
 * This file is part of LibUti.
 * 
 * LibUti is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LibUti is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with LibUti. If not, see <http://www.gnu.org/licenses/>.
 */
package net.psexton.libuti;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author PSexton
 */
public class UtiTest {
    
    @Rule public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testNullName() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("name cannot be null");
        Uti instance = new Uti(null);
    }
    
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
    
    @Test
    public void testToSuffix() {
        Uti instance = new Uti("public.png");
        String expResult = "png";
        String result = instance.toSuffix();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testToSuffixNull() {
        Uti instance = new Uti("public.data");
        String expResult = null;
        String result = instance.toSuffix();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testToSuffixDyn() {
        Uti instance = new Uti("dyn.foo");
        String expResult = "foo";
        String result = instance.toSuffix();
        
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
    
    @Test
    public void testDoesNotConformToNull() {
        Uti instance = new Uti("public.png");
        assertFalse(instance.conformsTo(null));
    }
    
    @Test
    public void testCompareToLess() {
        Uti instance = new Uti("public.audio");
        Uti other = new Uti("public.image");
        int result = instance.compareTo(other);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompareToMore() {
        Uti instance = new Uti("public.foo.bar");
        Uti other = new Uti("public.foo");
        int result = instance.compareTo(other);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompareToEquals() {
        Uti instance = new Uti("com.microsoft.waveform-audio");
        int result = instance.compareTo(instance);
        assertTrue(result == 0);
    }
    
    @Test
    public void testCompareToNull() {
        Uti instance = new Uti("public.data");
        Uti other = null;
        thrown.expect(NullPointerException.class);
        int result = instance.compareTo(other);
    }
    
    @Test
    public void conformancesForRoot() {
        Uti instance = new Uti("public.item");
        Set<Uti> expResult = new HashSet<>();
        expResult.add(instance);
        Set<Uti> result = instance.getConformances();
        assertEquals(expResult, result);
    }
    
    @Test
    public void conformancesForLeaf() {
        Uti instance = new Uti("public.png");
        Set<Uti> expResult = new HashSet<>();
        // self
        expResult.add(instance);
        // physical hierarchy
        expResult.add(new Uti("public.image"));
        expResult.add(new Uti("public.data"));
        expResult.add(new Uti("public.item"));
        // document hierarchy
        expResult.add(new Uti("public.content"));
        Set<Uti> result = instance.getConformances();
        assertEquals(expResult, result);
    }
    
    @Test
    public void conformersForMid() {
        Uti instance = new Uti("public.image");
        Set<Uti> expResult = new HashSet<>();
        // self
        expResult.add(instance);
        // physical hierarchy
        expResult.add(new Uti("public.png"));
        expResult.add(new Uti("public.jpeg"));
        expResult.add(new Uti("public.tiff"));
        expResult.add(new Uti("com.compuserve.gif"));
        expResult.add(new Uti("com.microsoft.bmp"));
        Set<Uti> result = instance.getConformers();
        assertEquals(expResult, result);
    }
    
    @Test
    public void conformersForLeaf() {
        Uti instance = new Uti("public.png");
        Set<Uti> expResult = new HashSet<>();
        // self
        expResult.add(instance);
        Set<Uti> result = instance.getConformers();
        assertEquals(expResult, result);
    }
}
