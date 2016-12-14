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

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author PSexton
 */
public class UtiBuilderTest {
    
     @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    
    /**
     * Test of fromFile method, of class UtiBuilder.
     * @throws java.io.IOException
     */
    @Test
    public void testFromFileMatches() throws IOException {
        File file = temp.newFile("foo.txt");
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti("public.plain-text");
        Uti result = instance.fromFile(file);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFromFileDoesNotMatch() throws IOException {
        File file = temp.newFile("foo.notevenclosetoarealextension");
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti("dyn.notevenclosetoarealextension");
        Uti result = instance.fromFile(file);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFromFileWithDir() throws IOException {
        File dir = temp.newFolder("subsubsub");
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti("public.folder");
        Uti result = instance.fromFile(dir);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFromFileNoExtension() throws IOException {
        File file = temp.newFile("foo");
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti("public.data");
        Uti result = instance.fromFile(file);
        assertEquals(expResult, result);
    }

    /**
     * Test of fromSuffix method, of class UtiBuilder.
     */
    @Test
    public void testFromSuffixMatches() {
        String fileSuffix = "docx";
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti("com.microsoft.word.docx");
        Uti result = instance.fromSuffix(fileSuffix);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFromSuffixDoesNotMatch() {
        String fileSuffix = "dog";
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti("dyn." + fileSuffix);
        Uti result = instance.fromSuffix(fileSuffix);
        assertEquals(expResult, result);
    }

    /**
     * Test of fromString method, of class UtiBuilder.
     */
    @Test
    public void testFromStringMatches() {
        String utiName = "com.microsoft.word.docx";
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti(utiName);
        Uti result = instance.fromString(utiName);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFromStringDoesNotMatch() {
        String utiName = "this.is.not.a.uti";
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = null;
        Uti result = instance.fromString(utiName);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFromSuffixUppercase() {
        String fileSuffix = "DOCX";
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = new Uti("com.microsoft.word.docx");
        Uti result = instance.fromSuffix(fileSuffix);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynStringRoundTrip() {
        // If we start out with an unknown file suffix, and get a "dyn.*" UTI,
        // calling Uti#toString followed by UtiBuilder#fromString should still 
        // work.
        
        String unknownSuffix = "foobar";
        UtiBuilder instance = new UtiBuilder();
        Uti orig = instance.fromSuffix(unknownSuffix);
        Uti copy = instance.fromString(orig.toString());
        assertEquals(orig, copy);
    }
    
}
