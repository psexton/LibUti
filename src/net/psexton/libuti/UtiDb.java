/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JFrame;
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
    private Set<String> knownUtis;
    private Map<String, String> suffixTable;
    private DirectedSparseGraph<String, String> conformances;
    
    public UtiDb() {
        suffixTable = new HashMap<String, String>();
        knownUtis = new HashSet<String>();
        conformances = new DirectedSparseGraph<String, String>();
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
            knownUtis.add(name); // Add UTI to knownUtis
            conformances.addVertex(name); // Add UTI to graph
            // File suffixes are in <suffix> children
            // Iterate over them
            for(Object o2 : uti.getChildren("suffix")) {
                Element suffix = (Element) o2;
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
     * Checks if the specified UTI name is found in the DB
     * @param utiName UTI name to search for
     * @return True if found, false if not
     */
    public Boolean isUtiInDb(String utiName) {
        return knownUtis.contains(utiName);
    }
    
    public void visualizeConformances() {
        CircleLayout<Integer, String> layout = new CircleLayout(conformances); 
        layout.setSize(new Dimension(500,500)); 
        BasicVisualizationServer<Integer,String> vv =new BasicVisualizationServer<Integer,String>(layout); 
        vv.setPreferredSize(new Dimension(550,550));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        JFrame frame = new JFrame("UTI Conformances"); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.getContentPane().add(vv); frame.pack();
        frame.setVisible(true);
    }
    
}
