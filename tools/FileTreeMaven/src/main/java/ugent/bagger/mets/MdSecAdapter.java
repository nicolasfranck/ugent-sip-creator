/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.mets;

import com.anearalone.mets.MdSec;

/**
 *
 * @author nicolas
 */
public class MdSecAdapter extends MdSec {
    public MdSecAdapter(){
        super();
    }
    public MdSecAdapter(String id){        
        super(id);
    }
    public MdSecAdapter(MdSec mdSec){
        setCREATEDATE(mdSec.getCREATED());
        setGROUPID(mdSec.getGROUPID());
        setID(mdSec.getID());
        setMdRef(mdSec.getMdRef());
        setMdWrap(mdSec.getMdWrap());
        setSTATUS(mdSec.getSTATUS());        
    }
    public MDTYPE getMDTYPE(){
       if(getMdWrap() != null && getMdWrap().getXmlData().size() > 0){
           return getMdWrap().getMDTYPE();
       }else if(getMdRef() != null){
           return getMdRef().getMDTYPE();
       }
       return MDTYPE.OTHER;
    }
}
