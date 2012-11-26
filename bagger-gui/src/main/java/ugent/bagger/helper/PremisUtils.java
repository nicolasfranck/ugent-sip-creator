package ugent.bagger.helper;

import com.anearalone.mets.AmdSec;
import java.util.ArrayList;

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
}
