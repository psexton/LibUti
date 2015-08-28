/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
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
    // Singleton stuff
    private static class SingletonHolder { public static final UtiDb instance = new UtiDb(true); }
    public static UtiDb getInstance() { return SingletonHolder.instance; }
    public static UtiDb getCleanInstance() { return new UtiDb(false); }
    // End Singleton stuff
    
    private final Map<String, String> suffixTable;
    private final Map<String, String> reverseSuffixTable;
    private final DirectedSparseGraph<String, String> conformances;
    
    private UtiDb(boolean loadStandardData) {
        suffixTable = new HashMap<String, String>();
        reverseSuffixTable = new HashMap<String, String>();
        conformances = new DirectedSparseGraph<String, String>();
        if(loadStandardData) {
            String[] dataFiles = {"Archive", "Audio", "Image", "Matlab", "MicrosoftOffice", "Other", "RootsAndBases", "Text", "Video"};
            for (String dataFile : dataFiles) {
                try {
                    importXmlData(this.getClass().getResourceAsStream("/net/psexton/libuti/data/" + dataFile + ".xml"));
                } 
                catch (IOException ex) {
                    Logger.getLogger(UtiDb.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (JDOMException ex) {
                    Logger.getLogger(UtiDb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Adds UTI mappings to the DB from an XML source
     * @param in InputStream containing XML data
     * @throws IOException if there was a problem reading the InputStream
     * @throws JDOMException if there was a problem parsing the XML
     */
    public final void importXmlData(InputStream in) throws IOException, JDOMException {
        // Parse the input stream and rip out a usable Element from the Document
        Document doc = new SAXBuilder().build(in);
        Element root = doc.detachRootElement();
        
        // root element is <uti-list>
        // Iterate over all <uti> children
        for(Object o : root.getChildren("uti")) {
            Element uti = (Element) o;
            // UTI's name is in a <name> child
            String name = uti.getChildText("name");
            conformances.addVertex(name); // Add UTI to graph
            // File suffixes are in <suffix> children
            // Iterate over them
            for(Object o2 : uti.getChildren("suffix")) {
                Element suffix = (Element) o2;
                if(suffix.getAttribute("preferred") != null && suffix.getAttribute("preferred").getBooleanValue()) {
                    reverseSuffixTable.put(name, suffix.getText()); // Add UTI->suffix to reverseSuffixTable
                }
                suffixTable.put(suffix.getText(), name); // Add suffix->UTI to suffixTable
            }
            // Conformances are in <conforms-to> children
            // Iterate over them
            for(Object o2 : uti.getChildren("conforms-to")) {
                Element conformsTo = (Element) o2;
                String parentUtiName = conformsTo.getText();
                String edgeName = name + "->" + parentUtiName;
                conformances.addEdge(edgeName, name, parentUtiName, EdgeType.DIRECTED);
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
     * 
     * @param uti String form of a UTI
     * @return Preferred file extension, or null
     */
    public String preferredSuffixForUti(String uti) {
        if(reverseSuffixTable.containsKey(uti))
            return reverseSuffixTable.get(uti);
        else
            return null;
    }
    
    /**
     * Checks if the specified UTI name is found in the DB
     * @param utiName UTI name to search for
     * @return True if found, false if not
     */
    public Boolean isUtiInDb(String utiName) {
        return conformances.containsVertex(utiName);
    }
    
    public Boolean conformsTo(String childUti, String parentUti) {
        DijkstraShortestPath<String,String> alg = new DijkstraShortestPath(conformances);
        
        List<String> path = alg.getPath(childUti, parentUti);
        return !path.isEmpty();
    }
    
    public Set<String> conformancesFor(String utiName) {
        Set<String> set = new HashSet<String>();
        set.add(utiName); // always include self
        for(String successor : conformances.getSuccessors(utiName)) {
            Set<String> parentSet = conformancesFor(successor);
            set.addAll(parentSet);
        }
        
        return set;
    }
    
    public Set<String> conformersFor(String utiName) {
        Set<String> set = new HashSet<String>();
        set.add(utiName); // always include self
        for(String predecessor : conformances.getPredecessors(utiName)) {
            Set<String> childSet = conformersFor(predecessor);
            set.addAll(childSet);
        }
        
        return set;
    }
    
    /**
     * Given a layout class, returns an instance of that class constructed using
     * the JUNG graph we use internally.
     * @param layoutClass
     * @return 
     */
    protected Layout<Integer, String> constructLayout(Class layoutClass) {
        try {
            Constructor ctr = layoutClass.getConstructor(Graph.class);
            Object obj = ctr.newInstance(conformances);
            return (Layout<Integer, String>) obj;
        } 
        catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } 
    }
    
}
