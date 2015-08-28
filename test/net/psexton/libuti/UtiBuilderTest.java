/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
}
