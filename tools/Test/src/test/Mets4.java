/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import edu.harvard.hul.ois.mets.*;
import edu.harvard.hul.ois.mets.helper.*;
import java.io.FileOutputStream;
import java.util.Date;
/**
 *
 * @author nicolas
 */
public class Mets4 {
    public static void main(String []args){
        Mets mets = new Mets ();
        mets.setOBJID ("254933");
        mets.setLABEL ("Bangs, Outram and James L. Peters. 1928. " +
                       "Birds Collected by Dr. Joseph Rock....");
        mets.setTYPE  ("citationleaf");
        mets.setPROFILE ("PDS");

        MetsHdr metsHdr = new MetsHdr ();
        metsHdr.setCREATEDATE  (new Date ());
        metsHdr.setRECORDSTATUS ("Production");

        Agent agent = new Agent ();
        agent.setROLE (Role.DISSEMINATOR);
        agent.setTYPE (Type.OTHER);
        agent.setOTHERTYPE ("depositingAgent");

        Name name = new Name ();
        name.getContent ().add (new PCData ("HCL DIG"));
        agent.getContent ().add (name);

        Note note = new Note ();
        note.getContent ().add (new PCData ("Depositing on behalf " +
                                          "of Arnold Arboretum"));
        agent.getContent ().add (note);
        metsHdr.getContent ().add (agent);

        AltRecordID alt = new AltRecordID ();
        alt.setTYPE ("NRS");
        alt.getContent ().add (new PCData ("nrs:fhcl:1234567890"));
        metsHdr.getContent ().add (alt);
        mets.getContent ().add (metsHdr);

        DmdSec dmdSec = new DmdSec ();
        dmdSec.setID ("a");

        MdWrap mdWrap = new MdWrap ();
        mdWrap.setMIMETYPE ("text/xml");
        mdWrap.setMDTYPE (Mdtype.DC);

        XmlData xmlData = new XmlData ();
	xmlData.setSchema ("dc", "http://purl.org/dc/elements/1.1/");

        Any any = new Any ("dc:title");
        any.getContent ().add (new PCData ("Birds "));
        Any an2 = new Any ("a:b");
        an2.getContent ().add (new PCData ("Collected by"));
        any.getContent ().add (an2);
        any.getContent ().add (new PCData(" Dr. Joseph Rock...."));
        xmlData.getContent ().add (any);
        any = new Any ("dc:author");
        any.getContent ().add (new PCData ("Bangs, Outram"));
        xmlData.getContent ().add (any);
        mdWrap.getContent ().add (xmlData);
        dmdSec.getContent ().add (mdWrap);
        mets.getContent ().add (dmdSec);

        AmdSec amdSec = new AmdSec ();

        SourceMD sourceMD = new SourceMD ();
        sourceMD.setID ("b");

        mdWrap = new MdWrap ();
        mdWrap.setMIMETYPE ("text/xml");
        mdWrap.setMDTYPE (Mdtype.DC);

        xmlData = new XmlData ();
        xmlData.setSchema ("dc","http://purl.org/dc/elements/1.1/");
        /* Use the PreformedXML element */
        PreformedXML preformed = new PreformedXML("<dc:source>" +
							   "Bulletin of the " +
		      "the Museum of Comparative Zoology at Harvard College " +
					   "68(7): 311-381. 1928</dc:source>");
        xmlData.getContent ().add (preformed);
        /* Use the Any element */
        /*
          any = new Any ("dc:source");
          any.getContent ().add (new PCData ("Bulletin of the " +
            "Museum of Comparative Zoology at Harvard College " +
            "68(7): 311-381. 1928"));
        xmlData.getContent ().add (any);
        */
        mdWrap.getContent ().add (xmlData);
        sourceMD.getContent ().add (mdWrap);
        amdSec.getContent ().add (sourceMD);
	mets.getContent ().add (amdSec);

	FileSec fileSec = new FileSec ();

	FileGrp fileGrp = new FileGrp ();

        File file = new File ();
        file.setID ("c");
        file.setMIMETYPE ("plain/text");
        file.setGROUPID ("d");

        FLocat fLocat = new FLocat ();
        fLocat.setXlinkHref ("254792");
        fLocat.setLOCTYPE (Loctype.OTHER);
        fLocat.setOTHERLOCTYPE ("DRS");
        file.getContent ().add (fLocat);
        fileGrp.getContent ().add (file);

        file = new File ();
        file.setID ("e");
        file.setMIMETYPE ("plain/text");
        file.setGROUPID ("f");

        fLocat = new FLocat ();
        fLocat.setXlinkHref ("254794");
        fLocat.setLOCTYPE (Loctype.OTHER);
        fLocat.setOTHERLOCTYPE ("DRS");
        file.getContent ().add (fLocat);
        fileGrp.getContent ().add (file);
        fileSec.getContent ().add (fileGrp);

        fileGrp = new FileGrp ();

        file = new File ();
        file.setID ("g");
        file.setMIMETYPE ("image/tiff");
        file.setGROUPID ("h");

        fLocat = new FLocat ();
        fLocat.setXlinkHref ("254791");
        fLocat.setLOCTYPE (Loctype.OTHER);
        fLocat.setOTHERLOCTYPE ("DRS");
        file.getContent ().add (fLocat);
        fileGrp.getContent ().add (file);

        file = new File ();
        file.setID ("i");
        file.setMIMETYPE ("image/tiff");
        file.setGROUPID ("j");

        fLocat = new FLocat ();
        fLocat.setXlinkHref ("254793");
        fLocat.setLOCTYPE (Loctype.OTHER);
        fLocat.setOTHERLOCTYPE ("DRS");
        file.getContent ().add (fLocat);
        fileGrp.getContent ().add (file);
        fileSec.getContent ().add (fileGrp);
        mets.getContent ().add (fileSec);

        StructMap structMap = new StructMap ();
        structMap.setTYPE ("LOGICAL");

        Div div = new Div ();
        div.setTYPE ("article");
        div.setORDER (1);
        div.setDMDID ("a");
        div.setLABEL ("Bangs, Outram and James L. Peters. 1928. " +
                  "Birds collected by Dr. Joseph Rock....");
        Div div2 = new Div ();
        div2.setTYPE ("page");
        div2.setORDER (1);
        div2.setORDERLABEL ("311");
        div2.setLABEL (", page 311");

        Fptr fptr = new Fptr ();
        fptr.setFILEID ("c");
        div2.getContent ().add (fptr);

        fptr = new Fptr ();
        fptr.setFILEID ("i");
        div2.getContent ().add (fptr);
        div.getContent ().add (div2);

        div2 = new Div ();
        div2.setTYPE ("page");
        div2.setORDER (2);
        div2.setORDERLABEL ("312");
        div2.setLABEL (", page 312");

        fptr = new Fptr ();
        fptr.setFILEID ("e");
        div2.getContent ().add (fptr);

        fptr = new Fptr ();
        fptr.setFILEID ("g");
        div2.getContent ().add (fptr);
        div.getContent ().add (div2);
        structMap.getContent ().add (div);
        mets.getContent ().add (structMap);

        try{
            mets.validate (new MetsValidator ());            
            mets.write (new MetsWriter (new FileOutputStream("/tmp/mets-out.xml")));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
