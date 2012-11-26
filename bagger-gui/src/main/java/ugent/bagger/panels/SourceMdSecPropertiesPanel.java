/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class SourceMdSecPropertiesPanel extends MdSecPropertiesPanel{
    public SourceMdSecPropertiesPanel(ArrayList<MdSec>data){        
        super(data);        
    }
    @Override
    protected int getMax(){
        return 1;
    }
}
