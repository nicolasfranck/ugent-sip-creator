/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import gov.loc.repository.bagit.utilities.namevalue.NameValueReader;
import gov.loc.repository.bagit.utilities.namevalue.impl.NameValueReaderImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.w3c.dom.Document;
import ugent.bagger.params.VelocityTemplate;

/**
 *
 * @author nicolas
 */
public class NameValueUtils {
    public static HashMap<String,ArrayList<String>>readMap(File file) throws IOException{        
        return readMap(new FileInputStream(file));
    }
    public static HashMap<String,ArrayList<String>>readMap(URL url) throws IOException{
        return readMap(url.openStream());
    }
    public static HashMap<String,ArrayList<String>>readMap(InputStream is){
        HashMap<String,ArrayList<String>>map = new HashMap<String, ArrayList<String>>();
        NameValueReaderImpl reader = new NameValueReaderImpl("UTF-8",is,"bagInfoTxt");
        while(reader.hasNext()){
            NameValueReader.NameValue pair = reader.next();
            if(!map.containsKey(pair.getKey())){
                map.put(pair.getKey(),new ArrayList<String>());
            }
            map.get(pair.getKey()).add(pair.getValue());
        }
        return map;
    }
    public static Document templateToDocument(VelocityTemplate vt,HashMap<String,ArrayList<String>>record) throws IOException, ResourceNotFoundException, ParseErrorException, Exception{
        VelocityEngine ve = VelocityUtils.getVelocityEngine();
        Template template = ve.getTemplate(vt.getPath());            
        HashMap<String,Object>r = new HashMap<String,Object>();

        r.put("record",record);
        VelocityContext vcontext = new VelocityContext(r);
        StringWriter writer = new StringWriter();
        template.merge(vcontext,writer);                        

        String output = writer.toString();

        //zet xml om naar w3c.document
        return XML.XMLToDocument(new StringReader(output));
    }
}
