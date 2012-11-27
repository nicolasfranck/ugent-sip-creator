package ugent.bagger.helper;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import java.util.ArrayList;
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
        System.out.println("checking namespace: "+e.getNamespaceURI());
        System.out.println("agains ns "+MetsUtils.getTypeMap().get("PREMIS-2"));
        return e.getNamespaceURI().equals(MetsUtils.getTypeMap().get("PREMIS-2"));
    }
    
}