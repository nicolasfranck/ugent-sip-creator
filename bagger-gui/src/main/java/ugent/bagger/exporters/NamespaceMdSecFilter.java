package ugent.bagger.exporters;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MdWrap;
import java.util.ArrayList;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

/**
 *
 * @author nicolas
 */
public class NamespaceMdSecFilter implements MdSecFilter{
    ArrayList<String>namespaces;
    
    public NamespaceMdSecFilter(){       
    }
    public NamespaceMdSecFilter(ArrayList<String>namespaces){
        this.namespaces = namespaces;
    }
    public ArrayList<String> getNamespaces() {
        if(namespaces == null){
            namespaces = new ArrayList<String>();
        }
        return namespaces;
    }
    public void setNamespaces(ArrayList<String> namespaces) {
        this.namespaces = namespaces;
    }    
    @Override
    public ArrayList<MdSec> filter(ArrayList<MdSec> data) {
        Assert.notNull(data);
        
        ArrayList<MdSec>filtered = new ArrayList<MdSec>();
        
        for(MdSec item:data){
            MdWrap mdWrap = item.getMdWrap();
            if(mdWrap == null || mdWrap.getXmlData().isEmpty()){
                continue;
            }
            Element e = mdWrap.getXmlData().get(0);            
            
            if(getNamespaces().contains(e.getNamespaceURI())){
                filtered.add(item);
            }                    
        }
        
        
        return filtered;
    }    
}
