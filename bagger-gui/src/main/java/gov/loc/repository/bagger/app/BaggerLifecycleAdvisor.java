package gov.loc.repository.bagger.app;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.GraphicsEnvironment;
import java.util.Set;
import javax.swing.UIManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.config.DefaultApplicationLifecycleAdvisor;
import org.springframework.richclient.application.statusbar.StatusBar;
import ugent.bagger.helper.SwingUtils;

/**
 * Custom application lifecycle implementation that configures the app at well defined points within
 * its lifecycle.
 *
 * @author Jon Steinbach
 */
public class BaggerLifecycleAdvisor extends DefaultApplicationLifecycleAdvisor{
    static final Log log = LogFactory.getLog(BaggerLifecycleAdvisor.class);
    static void init(){               
                
        //set dimensions
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        SwingUtils.getFrame().setMaximizedBounds(env.getMaximumWindowBounds());
        SwingUtils.getFrame().setExtendedState(
                SwingUtils.getFrame().getExtendedState() | SwingUtils.getFrame().MAXIMIZED_BOTH
        );
            
        //set ui-manager keys
        Set<String>keys = SwingUtils.getUIManagerMessages().keySet();
        for(String key:keys){
            UIManager.put(key,SwingUtils.getUIManagerMessages().get(key));
        }
        
    }    

    /**
     * Additional window configuration before it is created.
     */
    @Override
    public void onWindowCreated(ApplicationWindow window){
        super.onWindowCreated(window);                        
        init(); 
    }    
    @Override
    public boolean onPreWindowClose(ApplicationWindow window){
        BagView bagView = BagView.getInstance();
        MetsBag metsBag = bagView.getBag();        
        
        
        if(metsBag.isChanged()){
            try{
                bagView.clearBagHandler.confirmCloseBag();
                if(bagView.clearBagHandler.isConfirmSaveFlag()){
                    bagView.saveBagHandler.setClearAfterSaving(false);
                    bagView.saveBagAsHandler.openSaveBagAsFrame();
                    bagView.clearBagHandler.setConfirmSaveFlag(false);
                }
                SwingUtils.waitForWorkers();
            }catch(Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
            }   
        }
                 
        
        return true;
    }
    @Override
    public StatusBar getStatusBar(){       
        return new BaggerStatusBar();
    } 
}