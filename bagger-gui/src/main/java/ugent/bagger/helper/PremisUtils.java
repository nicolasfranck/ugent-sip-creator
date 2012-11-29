package ugent.bagger.helper;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;

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
            System.out.println("digiprovMD with id: "+m.getID());           
            if(m.getID().equals("bagit")){
                iterator.remove();
                System.out.println("digiprovMD with id: "+m.getID()+" removed!");
            }
        }
    }
    public static MdSec getPremisMdSec(ArrayList<MdSec>list){
        MdSec m = null;
        for(MdSec mdSec:list){
            if(isPremisMdSec(mdSec)){                
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
    
}