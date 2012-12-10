package ugent.bagger.properties;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MDTYPE;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.util.Assert;

/**
 *
 * @author nicolas
 */
public class MdSecProperties {
    private MdSec mdSec;
    public MdSecProperties(MdSec mdSec){
        Assert.notNull(mdSec);
        this.mdSec = mdSec;
    }
    public MdSec getMdSec() {
        return mdSec;
    }
    public void setMdSec(MdSec mdSec) {
        this.mdSec = mdSec;
    }    
    /*
    public String getID(){                
        return mdSec.getID();
    }
    public void setID(String id){
        mdSec.setID(id);
    }*/
    public String getGROUPID(){
        return mdSec.getGROUPID();
    }
    public void setGROUPID(String groupid){
        mdSec.setGROUPID(groupid);
    }
    public List<String>getADMID(){        
        return mdSec.getADMID();
    }    
    public XMLGregorianCalendar getCREATED(){                        
        return mdSec.getCREATED();
    }
    public void setCREATED(XMLGregorianCalendar c){
        mdSec.setCREATEDATE(c);
    }
    public String getSTATUS(){
        return mdSec.getSTATUS();
    }
    public void setSTATUS(String status){
        mdSec.setSTATUS(status);
    }
    public String getNamespace(){  
        if(mdSec.getMdWrap() != null && mdSec.getMdWrap().getXmlData() != null && !mdSec.getMdWrap().getXmlData().isEmpty()){
            return mdSec.getMdWrap().getXmlData().get(0).getNamespaceURI();
        }else{
            return "-";
        }        
    }
    public MDTYPE getMDTYPE(){        
        if(mdSec.getMdWrap() != null){
            return mdSec.getMdWrap().getMDTYPE();
        }else{
            return MDTYPE.OTHER;
        }        
    }
    public void setMDTYPE(MDTYPE type){
        mdSec.getMdWrap().setMDTYPE(type);
    }
    public String getOTHERMDTYPE(){
        if(mdSec.getMdWrap() != null){
            return mdSec.getMdWrap().getOTHERMDTYPE();
        }else{
            return "-";
        }
    }
    public void setOTHERMDTYPE(String type){        
        mdSec.getMdWrap().setOTHERMDTYPE(type);
    }
    public String getLabel(){
        if(mdSec.getMdWrap() != null){
            return mdSec.getMdWrap().getLabel();
        }else{
            return "";
        }
    }
    public void setLabel(String label){
        mdSec.getMdWrap().setLabel(label);
    }
}
