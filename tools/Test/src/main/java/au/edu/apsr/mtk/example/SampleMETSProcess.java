package au.edu.apsr.mtk.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import au.edu.apsr.mtk.base.*;
import au.edu.apsr.mtk.ch.*;

import org.xml.sax.SAXException;

public class SampleMETSProcess
{
	private static METS mets = null;
	
    public static void main(String[] args) throws METSException, FileNotFoundException, SAXException, ParserConfigurationException, IOException
    {
    	if (args.length != 1)
    	{
    		System.out.println("Usage: java au.edu.apsr.mtk.example.SampleMETSProcess <METS file>");
    		System.exit(1);
    	}
    	long start = System.currentTimeMillis();
    	InputStream in = new FileInputStream(args[0]);

        METSReader mr = new METSReader();
        mr.mapToDOM(in);
        
        METSWrapper mw = new METSWrapper(mr.getMETSDocument());
		mw.validate();
		
        mets = mw.getMETSObject();

        System.out.println("Package Type of " + mets.getType() + ", using profile: " + mets.getProfile());

		MetsHdr mh = mets.getMetsHdr();
		if (mh != null)
		{
			System.out.println("Package create date: " + mh.getCreateDate());
			System.out.println("Package last modified date: " + mh.getLastModDate());
		
			List<Agent> agents = mh.getAgents();
	        for (Iterator<Agent> i = agents.iterator(); i.hasNext();)
    	    {
        		Agent a = i.next();
   				System.out.println("Agent " + a.getName() + " has role " + a.getRole());
	        }
	    }
		      
        FileSec fileSec = mets.getFileSec();
        if (fileSec != null)
        {
            List<FileGrp> fgs = fileSec.getFileGrps();
            for (Iterator<FileGrp> i = fgs.iterator(); i.hasNext();)
            {
        	    FileGrp fg = i.next();
   			    System.out.println("FileGrp of use " + fg.getUse());
            }
        }

		List<StructMap> sms = mets.getStructMaps();
		
		System.out.println("Package has " + sms.size() + " structMap(s)");
		
		// let's look at the first StructMap
		StructMap sm = sms.get(0);
		
		showDivInfo(sm.getDivs());
		long end = System.currentTimeMillis();
		System.out.println("elapsed time"+(end - start));
    }
    
    
    private static void showDivInfo(List<Div> divs) throws METSException
    {
        for (Iterator<Div> divi = divs.iterator(); divi.hasNext();)
        {
            Div div = divi.next();

           	System.out.println("Div type of " + div.getType() + " with DMDID " + div.getDmdID() + " contains metadata of type " + mets.getDmdSec(div.getDmdID()).getMDType());
       	       	
           	showDivInfo(div.getDivs());
        }
    }
}
