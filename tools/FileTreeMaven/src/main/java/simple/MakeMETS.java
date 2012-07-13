/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple;

import au.edu.apsr.mtk.base.Agent;
import au.edu.apsr.mtk.base.Div;
import au.edu.apsr.mtk.base.DmdSec;
import au.edu.apsr.mtk.base.FLocat;
import au.edu.apsr.mtk.base.File;
import au.edu.apsr.mtk.base.FileGrp;
import au.edu.apsr.mtk.base.FileSec;
import au.edu.apsr.mtk.base.METS;
import au.edu.apsr.mtk.base.METSException;
import au.edu.apsr.mtk.base.METSWrapper;
import eu.medsea.mimeutil.MimeUtil;
import au.edu.apsr.mtk.base.MdWrap;
import au.edu.apsr.mtk.base.MetsHdr;
import au.edu.apsr.mtk.base.StructMap;
import eu.medsea.mimeutil.MimeType;
import helper.FileUtils;
import helper.XML;
import helper.XSLT;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.util.UUID;

/**
 *
 * @author nicolas
 */
public class MakeMETS {
    private static Random rand = new Random();

    public static void main(String []args) {

        HashMap<String,String>mapping = new HashMap();
        mapping.put("http://www.loc.gov/MARC21/slim","file:///home/nicolas/Bagger-LC/doc/metadata/xslt/marc2dc.xsl");
        mapping.put("http://www.loc.gov/mods/v3","file:///home/nicolas/Bagger-LC/doc/metadata/xslt/MODS3-22simpleDC.xsl");       


        ArrayList<java.io.File>metadata_files = FileUtils.listFiles("/home/nicolas/metadata");
        try{
            METSWrapper mw = new METSWrapper();
            METS mets = mw.getMETSObject();

            //attributen root element
            mets.setObjID("Example1");
            mets.setProfile("http://localhost/profiles/scientific-datasets-profile");
            mets.setType("investigation");

            //metsHdr
            MetsHdr mh = mets.newMetsHdr();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            String currentTime = df.format(cal.getTime());
            mh.setCreateDate(currentTime);
            mh.setLastModDate(currentTime);

            Agent agent = mh.newAgent();
            agent.setRole("CREATOR");
            agent.setType("OTHER");
            agent.setName("SampleMETSBuild");
            mh.addAgent(agent);

            mets.setMetsHdr(mh);

            //xslt-docs
            HashMap<String,Document>xslt_docs = new HashMap();

            //dmdSec
            for(java.io.File metadata_file:metadata_files){
                Document doc = XML.XMLToDocument(metadata_file);

                String ns = doc.getDocumentElement().getNamespaceURI();
                String xslt_location = mapping.get(ns);
                if(xslt_location != null){

                    System.out.println("transforming..");
                    Document xdoc = null;
                    //cache
                    if(xslt_docs.get(ns) == null){
                        xdoc = XML.XMLToDocument(new URL(xslt_location));
                        xslt_docs.put(ns, xdoc);
                    }
                    //transformatie
                    Document outputDoc = XML.getDocumentBuilder().newDocument();
                    XSLT.transform(doc,xdoc,outputDoc);                   

                    DmdSec meta = createDmdSec(mets, outputDoc);
                    mets.addDmdSec(meta);
                }

                DmdSec meta = createDmdSec(mets, doc);                                
                mets.addDmdSec(meta);
                
            }

            ArrayList<java.io.File>list_files = FileUtils.listFiles("/home/nicolas/data");
            
            FileSec fs = mets.newFileSec();
            FileGrp fg = fs.newFileGrp();
            fg.setUse("original");

            for(int i = 0;i < list_files.size();i++){
                java.io.File file = list_files.get(i);
                File metsFile = fg.newFile();
                metsFile.setID(file.getName());
                Collection mimeTypes = MimeUtil.getMimeTypes(file);
                String mimeType = null;
                Iterator iterator = mimeTypes.iterator();
                while(iterator.hasNext()){
                    MimeType m = (MimeType) iterator.next();
                    mimeType = m.getMediaType()+"/"+m.getSubType();
                    break;
                }
                metsFile.setMIMEType(mimeType);
                metsFile.setSize(file.length());
                metsFile.setSeq(""+i);

                FLocat loc = metsFile.newFLocat();
                loc.setHref(file.toURI().toString());
                loc.setLocType("URL");

                metsFile.addFLocat(loc);

                fg.addFile(metsFile);
            }

            //files           

            fs.addFileGrp(fg);
            mets.setFileSec(fs);

            StructMap sm = mets.newStructMap();
            mets.addStructMap(sm);

            Div d = sm.newDiv();
            d.setType("investigation");
            sm.addDiv(d);
            
            mw.validate("file:///home/nicolas/mets/mets.xsd");
            mw.write(new FileOutputStream("/tmp/mets.xml"));

        }catch(FileNotFoundException e){
            System.err.println("Bestand kon niet gevonden worden: "+e.getMessage());
        }catch(IOException e){
            System.err.println("Fout bij het lezen of schrijven van een bestand: "+e.getMessage());
        }catch(ParserConfigurationException e){
            System.err.println("Fout in configuratie van de XML parser: "+e.getMessage());
        }catch(SAXException e){
            System.err.println("Fout bij het parsen van een xml-bestand: "+e.getMessage());
        }catch(METSException e){
            System.err.println("Fout bij de creatie van het METS document: "+e.getMessage());
        }catch(TransformerException e){

        }
    }

    private static String newId(int length){
        length = Math.abs(length);
        StringBuffer buf = new StringBuffer();
        for(int i = 0;i< length;i++){
            char c = (char)(rand.nextInt(26) + 'a');
            buf.append(c);
        }
        return buf.toString();
        //return UUID.randomUUID().toString().replace("-","");
    }
    public static DmdSec createDmdSec(METS mets,Document doc) throws METSException{

        DmdSec meta = mets.newDmdSec();
        MdWrap meta_xml = meta.newMdWrap();
        meta_xml.setXmlData(doc.getFirstChild());
        meta_xml.setMDType("OTHER");
        meta_xml.setOtherMDType(doc.getDocumentElement().getNamespaceURI());
        meta_xml.setMIMEType("text/xml");
        meta.setMdWrap(meta_xml);
        meta.setID(newId(100));

        return meta;
    }
}
