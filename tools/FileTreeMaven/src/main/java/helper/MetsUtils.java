/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;


import Exceptions.MdRefException;
import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsReader;
import com.anearalone.mets.MetsWriter;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class MetsUtils {
    public static Mets readMets(File file) throws ParserConfigurationException, DatatypeConfigurationException, FileNotFoundException, SAXException, ParseException, IOException{
        return new MetsReader().read(new FileInputStream(file));
    }
    public static Mets readMets(URL url) throws SAXException, ParseException, DatatypeConfigurationException, ParserConfigurationException, IOException{
        return new MetsReader().read(url.openStream());
    }
    public static void writeMets(Mets mets,File file) throws FileNotFoundException, TransformerException, DatatypeConfigurationException, ParserConfigurationException{
        writeMets(mets,new FileOutputStream(file));
    }
    public static void writeMets(Mets mets,OutputStream out) throws ParserConfigurationException, DatatypeConfigurationException, TransformerException{
        new MetsWriter().writeToOutputStream(mets,out);
    }
    public static Mets documentToMets(Document doc){
        Mets mets = new Mets();        
        mets.unmarshal(doc.getDocumentElement());
        return mets;
    }
    public static StructMap toStructMap(Node node){
        StructMap struct = new StructMap();        
        Div div = toDiv(node);
        struct.setDiv(div);                
        return struct;
    }
    public static Div toDiv(Node node){
        Div div = new Div();
        div.setLabel(node.getObject().toString());
        for(Node n:node.getChildren()){
            div.getDiv().add(toDiv(n));
        }
        return div;
    }
    /*
     * TODO: check binData within mdWrap (not possible in Mets api)
     */
    public static void validate(Mets mets) throws MdRefException{
        for(MdSec mdSec:mets.getDmdSec()){
           validate(mdSec);
        }
        for(AmdSec amdSec:mets.getAmdSec()){           
           for(MdSec m:amdSec.getDigiprovMD()){
               validate(m);
           }
           for(MdSec m:amdSec.getRightsMD()){
               validate(m);
           }
           for(MdSec m:amdSec.getSourceMD()){
               validate(m);
           }
           for(MdSec m:amdSec.getTechMD()){
               validate(m);
           }
        }
    }
    public static void validate(MdSec mdSec) throws MdRefException{        
        if(mdSec.getMdRef() != null) {
            throw new MdRefException("MdRef not allowed. XML need to be wrapped in a xmlData element");
        }
    }
    public static void main(String [] args){
        try{
            Mets mets = new Mets();
            ArrayList<Path>paths = Node.structureToList(new File("/tmp/a.txt"));
            Node node = new Node(".");
            for(Path path:paths){
                node.addPath(path);
                System.out.println("adding path "+path);
            }
            for(Node n:node.getChildren()){
                mets.getStructMap().add(toStructMap(n));
            }
            MetsWriter mw = new MetsWriter();
            mw.writeToOutputStream(mets,new FileOutputStream(new File("/tmp/output.txt")));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
