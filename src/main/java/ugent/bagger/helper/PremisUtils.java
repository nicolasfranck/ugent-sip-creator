package ugent.bagger.helper;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.premis.Premis;
import ugent.premis.PremisIO;

/**
 *
 * @author nicolas
 */
public class PremisUtils {
    public static AmdSec getAmdSecBagit(ArrayList<AmdSec>list){
        AmdSec amdSecBagit = null;
        for(AmdSec amdSec:list){
            if(amdSec.getID() != null && amdSec.getID().equals("bagit")){                
                amdSecBagit = amdSec;
                break;
            }
        }
        return amdSecBagit;
    }  
    public static void cleanupAmdSec(ArrayList<AmdSec>list){
        Iterator<AmdSec>iterator = list.iterator();
        while(iterator.hasNext()){
            AmdSec a = iterator.next();
            if(a.getID().equals("bagit")){
                iterator.remove();
            }
        }
    }
    public static void cleanupDigiprovMD(ArrayList<MdSec>list){
        Iterator<MdSec>iterator = list.iterator();
        while(iterator.hasNext()){
            MdSec m = iterator.next();             
            if(m.getID().equals("bagit_digiprovMD")){
                iterator.remove();                                
            }
        }
    }
    public static MdSec getPremisMdSec(ArrayList<MdSec>list){
        MdSec m = null;
        for(MdSec mdSec:list){
            if(mdSec.getID().equals("bagit_digiprovMD") && isPremisMdSec(mdSec)){                
                m = mdSec;
            }
        }
        return m;
    }
    public static boolean isPremisMdSec(MdSec mdSec){
        return mdSec.getMdWrap() != null && mdSec.getMdWrap().getXmlData() != null && 
        !mdSec.getMdWrap().getXmlData().isEmpty() &&
        isPremisElement(mdSec.getMdWrap().getXmlData().get(0));
    }
    public static boolean isPremisElement(Element e){        
        return e.getNamespaceURI().equals(MetsUtils.getTypeMap().get("PREMIS-2"));
    }
    public static Premis setPremis(Mets mets) throws ParseException, ParserConfigurationException, NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException, TransformerException, Exception{
        AmdSec amdSecBagit = getAmdSecBagit((ArrayList<AmdSec>)mets.getAmdSec());
        if(amdSecBagit == null){            
            amdSecBagit = new AmdSec();
            amdSecBagit.setID("bagit");
            mets.getAmdSec().add(amdSecBagit);
        }
        MdSec mdSec = getPremisMdSec((ArrayList<MdSec>)amdSecBagit.getDigiprovMD());
            
        Premis premis;
        if(mdSec == null){
            premis = new Premis();                
            mdSec = MetsUtils.createMdSec(PremisIO.toDocument(premis),false);
            mdSec.setID("bagit_digiprovMD");            
            amdSecBagit.getDigiprovMD().add(mdSec);
        }else{
            premis = PremisIO.toPremis(mdSec.getMdWrap().getXmlData().get(0));
        }
        
        return premis;
    }
    
}