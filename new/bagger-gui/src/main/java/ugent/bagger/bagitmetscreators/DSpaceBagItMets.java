/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.bagitmetscreators;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.StructMap;
import gov.loc.repository.bagger.bag.impl.BagItMets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class DSpaceBagItMets extends BagItMets{
    private static final Log log = LogFactory.getLog(DSpaceBagItMets.class);
    @Override
    public Mets onOpenBag(Bag bag) {
        String pathMets;            
        Mets mets = null;  
        BagView bagView = BagView.getInstance();
        
        if(bagView.getBag().getSerialMode() != DefaultBag.NO_MODE){            
            pathMets = FUtils.getEntryStringFor(bag.getFile().getAbsolutePath(),bagView.getBag().getName()+"/mets.xml");
        }else{
            pathMets = FUtils.getEntryStringFor(bag.getFile().getAbsolutePath(),"mets.xml");            
        }        
        try{
            mets = MetsUtils.readMets(FUtils.getInputStreamFor(pathMets));
        }catch(Exception e){                        
            log.debug(e.getMessage());            
        }
        if(mets == null){            
            mets = new Mets();
        }
        return mets;
    }

    @Override
    public Mets onSaveBag(Bag bag, Mets mets) {
        return null;
    }

    @Override
    protected FileSec createFileSecPayloads(ArrayList<BagFile> payloads) {
        return null;
    }

    @Override
    protected FileSec createFileSecTagfiles(ArrayList<BagFile> tagfiles) {
        return null;
    }

    @Override
    protected StructMap createStructMapPayloads(ArrayList<BagFile> payloads) {
        return null;
    }

    @Override
    protected StructMap createStructMapTagfiles(ArrayList<BagFile> tagfiles) {
        return null;
    }      
}