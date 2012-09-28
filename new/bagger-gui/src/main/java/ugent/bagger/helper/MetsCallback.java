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

/**
 *
 * @author nicolas
 */
public interface MetsCallback {
    void onCreateMets(Mets mets);
    void onCreateMetsHdr(MetsHdr metsHdr);
    void onCreateDmdSec(MdSec mdSec);
    void onCreateAmdSec(AmdSec mdSec);    
    void onCreateFileSec(FileSec fileSec);
    void onCreateStructMap(StructMap structMap);
    void onCreateStructLink(StructLink structLink);
    void onCreateBehaviorSec(BehaviorSec behaviorSec);
    void onCreateDiv(Div div);
}
