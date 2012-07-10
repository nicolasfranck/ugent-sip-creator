package au.edu.apsr.mtk.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.edu.apsr.mtk.base.*;

import org.xml.sax.SAXException;

public class SampleMETSBuild
{
	private static METS mets = null;

    public static void main(String[] args) throws METSException, FileNotFoundException, SAXException, ParserConfigurationException, IOException
    {
        METSWrapper mw = new METSWrapper();
        mets = mw.getMETSObject();

		mets.setObjID("Example1");
		mets.setProfile("http://localhost/profiles/scientific-datasets-profile");
		mets.setType("investigation");

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
		
		DmdSec dmd = mets.newDmdSec();
		dmd.setID("J-1");
		MdWrap mdw = dmd.newMdWrap();
		mdw.setMDType("MODS");
		mdw.setXmlData(createMODS("Structure of a Thermophilic Serpin in the Native State", "experiment").getDocumentElement());
		dmd.setMdWrap(mdw);
		
		mets.addDmdSec(dmd);
		
		DmdSec dmd2 = mets.newDmdSec();
		dmd2.setID("J-2");
		MdWrap mdw2 = dmd2.newMdWrap();
		mdw2.setMDType("MODS");
		mdw2.setXmlData(createMODS("APS Source 125 Deg (includes xfiles, denzo)", "dataset").getDocumentElement());
		dmd2.setMdWrap(mdw2);
		
		mets.addDmdSec(dmd2);
		
		FileSec fs = mets.newFileSec();
		FileGrp fg = fs.newFileGrp();
		fg.setUse("original");
		File f = fg.newFile();
		f.setID("F-1");
		f.setSize(2097152000);
		f.setMIMEType("application/x-bzip");
		f.setOwnerID("de.tar.bz.0");
		f.setChecksum("498d584f08389d40cff70c4adf0659ff");
		f.setChecksumType("MD5");
		
		FLocat fl = f.newFLocat();
		fl.setHref("http://localhost/myapp/get/XTAL_DATASET_de.tar.bz.0");
		fl.setLocType("URL");
		
		f.addFLocat(fl);
		fg.addFile(f);
		
		File f2 = fg.newFile();
		f2.setID("F-2");
		f2.setSize(65193743);
		f2.setMIMEType("application/x-bzip");
		f2.setOwnerID("de.tar.bz.1");
		f2.setChecksum("940faa9ab023cb0d42ef103a34b8c5bd");
		f2.setChecksumType("MD5");
		
		FLocat fl2 = f2.newFLocat();
		fl2.setHref("http://localhost/myapp/get/XTAL_DATASET_de.tar.bz.1");
		fl2.setLocType("URL");
		
		f2.addFLocat(fl2);
		fg.addFile(f2);
		
		fs.addFileGrp(fg);
		mets.setFileSec(fs);
		
		StructMap sm = mets.newStructMap();
		mets.addStructMap(sm);
		
		Div d = sm.newDiv();
		d.setType("investigation");
		d.setDmdID("J-1");
		sm.addDiv(d);

		Div d2 = d.newDiv();
		d2.setType("dataset");
		d2.setDmdID("J-2");
		d.addDiv(d2);

		Fptr fp = d2.newFptr();
		fp.setFileID("F-1");
		d2.addFptr(fp);

		Fptr fp2 = d2.newFptr();
		fp2.setFileID("F-2");
		d2.addFptr(fp2);

		mw.validate();

		mw.write(System.out);
    }
    
    
    static private Document createMODS(String title, String genre) throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElementNS("http://www.loc.gov/mods/v3", "mods");
        doc.appendChild(root);
        
        Element ti = doc.createElement("titleInfo");
        Element t = doc.createElement("title");
        t.setTextContent(title);
        ti.appendChild(t);
        root.appendChild(ti);
        
        Element g = doc.createElement("genre");
        g.setTextContent(genre);
        root.appendChild(g);
        
        return doc;
    }
}