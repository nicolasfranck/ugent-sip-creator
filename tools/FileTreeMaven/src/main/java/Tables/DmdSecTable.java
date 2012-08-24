/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import com.anearalone.mets.MdSec;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class DmdSecTable extends MdSecTable {    
    public DmdSecTable(ArrayList<MdSec>data,String [] cols,String id){
        super(data,cols,id);        
    }
}
