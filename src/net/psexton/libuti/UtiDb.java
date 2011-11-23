/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author psexton
 */
class UtiDb {
    private static final Logger logger = Logger.getLogger("net.psexton.libuti");
    private Map<String, String> suffixTable;
    
    public UtiDb() {
        suffixTable = new HashMap<String, String>();
    }
    
    /**
     * Adds UTI mappings to the DB from an XML source
     * @param in InputStream containing XML data
     * @throws IOException if there was a problem reading the InputStream
     * @throws JDOMException if there was a problem parsing the XML
     */
    public void importXmlData(InputStream in) throws IOException, JDOMException {
        // Parse the input stream and rip out a usable Element from the Document
        Document doc = new SAXBuilder().build(in);
        Element root = doc.detachRootElement();
        
        // root element is <uti-list>
        // Iterate over all <uti> children
        for(Object o : root.getChildren("uti")) {
            Element uti = (Element) o;
            // UTI's name is in a <name> child
            String name = uti.getChildText("name");
            // File suffixes are in <suffix> children
            // Iterate over them
            for(Object o2 : uti.getChildren("suffix")) {
                Element suffix = (Element) o2;
                suffixTable.put(suffix.getText(), name);
            }
        }
    }
    
    /**
     * 
     * @param suffix Three (or four or two) letter file extension
     * @return Name of matching UTI, or null
     */
    public String utiForSuffix(String suffix) {
        if(suffixTable.containsKey(suffix))
            return suffixTable.get(suffix);
        else
            return null;
    }
    
    /**
     * Checks if the specified UTI name is found in the DB
     * @param utiName UTI name to search for
     * @return True if found, false if not
     */
    public Boolean isUtiInDb(String utiName) {
        return suffixTable.containsValue(utiName);
    }
}
