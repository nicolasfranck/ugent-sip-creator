package gov.loc.repository.bagger.app;

import java.awt.Dimension;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.config.ApplicationWindowConfigurer;
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
    private static final Log log = LogFactory.getLog(BaggerLifecycleAdvisor.class);
    private static void init(){        
        //set dimension
        //Nicolas Franck: van belang dat dit LATER gebeurd (anders klapt frame dicht)        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();                
                SwingUtils.getFrame().setPreferredSize(dim);
                SwingUtils.getFrame().setBounds(0,0,(int)dim.getWidth(),(int)dim.getHeight());                
                SwingUtils.getFrame().pack();
            }            
        });
        
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
    public void onPreWindowOpen(ApplicationWindowConfigurer configurer){
        super.onPreWindowOpen(configurer);      
        init(); 
    }    
    @Override
    public StatusBar getStatusBar(){       
        return new BaggerStatusBar();
    } 
}