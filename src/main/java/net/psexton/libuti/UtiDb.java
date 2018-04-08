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

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * Database for UTIs
 * @author psexton
 */
public class UtiDb {
    // Singleton stuff
    private static class SingletonHolder { public static final UtiDb instance = new UtiDb(true); }
    /**
     * Singleton accessor for UTI Database.
     * @return Instance of UtiDb
     */
    public static UtiDb getInstance() { return SingletonHolder.instance; }
    static UtiDb getCleanInstance() { return new UtiDb(false); } // used by unit tests
    // End Singleton stuff
    
    private final Map<String, String> suffixTable;
    private final Map<String, String> reverseSuffixTable;
    private final DirectedSparseGraph<String, String> conformances;
    
    private UtiDb(boolean loadStandardData) {
        suffixTable = new HashMap<>();
        reverseSuffixTable = new HashMap<>();
        conformances = new DirectedSparseGraph<>();
        if(loadStandardData) {
            String[] dataFiles = {"Archive", "Audio", "Image", "Matlab", "MicrosoftOffice", "Other", "RootsAndBases", "Text", "Video"};
            for (String dataFile : dataFiles) {
                try {
                    importXmlData(this.getClass().getResourceAsStream("/net/psexton/libuti/data/" + dataFile + ".xml"));
                } 
                catch (IOException | JDOMException ex) {
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
     * Converts a file suffix to its UTI form.
     * @param suffix Three (or four or two) letter file extension
     * @return Name of matching UTI, or null
     */
    public String utiForSuffix(String suffix) {
        // Normalize suffix to lowercase checking
        // This is painfully non-I18N compatible, but it's better than nothing
        String normalizedSuffix = suffix.toLowerCase();
        
        if(suffixTable.containsKey(normalizedSuffix))
            return suffixTable.get(normalizedSuffix);
        else
            return null;
    }
    
    /**
     * Reverse mapping of UTI to preferred file suffix.
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
    
    /**
     * Checks if one UTI conforms to another.
     * @param childUti The conforming UTI.
     * @param parentUti The UTI being conformed to.
     * @return True if the conformance relation exists.
     */
    public Boolean conformsTo(String childUti, String parentUti) {
        DijkstraShortestPath<String,String> alg = new DijkstraShortestPath<>(conformances);
        
        List<String> path = alg.getPath(childUti, parentUti);
        return !path.isEmpty();
    }
    
    /**
     * Full set of conformances.
     * Returns a Set of all UTIs that the UTI passed in conforms to 
     * (including itself).
     * @param utiName UTI to look up.
     * @return Set of UTIs.
     */
    public Set<String> conformancesFor(String utiName) {
        Set<String> set = new HashSet<>();
        set.add(utiName); // always include self
        for(String successor : conformances.getSuccessors(utiName)) {
            Set<String> parentSet = conformancesFor(successor);
            set.addAll(parentSet);
        }
        
        return set;
    }
    
    /**
     * Full set of conformers.
     * Returns a Set of all UTIs that conform to the UTI passed in 
     * (including itself).
     * @param utiName UTI to look up.
     * @return Set of UTIs.
     */
    public Set<String> conformersFor(String utiName) {
        Set<String> set = new HashSet<>();
        set.add(utiName); // always include self
        for(String predecessor : conformances.getPredecessors(utiName)) {
            Set<String> childSet = conformersFor(predecessor);
            set.addAll(childSet);
        }
        
        return set;
    }
    
}
