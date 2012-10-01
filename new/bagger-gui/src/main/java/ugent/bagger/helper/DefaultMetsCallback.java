package ugent.bagger.helper;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.BehaviorSec;
import com.anearalone.mets.FileSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr;
import com.anearalone.mets.StructLink;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author nicolas
 */
public class DefaultMetsCallback implements MetsCallback{
    @Override
    public void onCreateMets(Mets mets) {
        
    }
    @Override
    public void onCreateMetsHdr(MetsHdr metsHdr) {
        
    }
    @Override
    public void onCreateDmdSec(MdSec mdSec) {
        
    }
    @Override
    public void onCreateAmdSec(AmdSec mdSec) {
        
    }
    @Override
    public void onCreateFileSec(FileSec fileSec) {
        
    }
    @Override
    public void onCreateStructMap(StructMap structMap,DefaultMutableTreeNode node) {
        
    }
    @Override
    public void onCreateStructLink(StructLink structLink) {
        
    }
    @Override
    public void onCreateBehaviorSec(BehaviorSec behaviorSec) {
        
    }
    @Override
    public void onCreateDiv(Div div,DefaultMutableTreeNode node) {
 
    }
}