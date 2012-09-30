package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.TagFilesFrame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationWindow;

/*
 * Nicolas Franck: behouden of niet? Heeft weinig toegevoegde waarde!
 */

public class ShowTagFilesHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private TagFilesFrame tagFilesFrame;   
    DefaultBag bag;

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public ShowTagFilesHandler() {
        super();        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showTagFiles();
    }

    public void showTagFiles(){
        BagView bagView = BagView.getInstance();
    	bag = bagView.getBag();
    	bagView.getTagManifestPane().updateCompositePaneTabs(bag);
    	ApplicationWindow window = Application.instance().getActiveWindow();
    	JFrame f = window.getControl();
        tagFilesFrame = new TagFilesFrame(f, bagView.getPropertyMessage("bagView.tagFrame.title"));
        tagFilesFrame.addComponents(bagView.getTagManifestPane());
    	tagFilesFrame.addComponents(bagView.getTagManifestPane());
    	tagFilesFrame.setVisible(true);
    	tagFilesFrame.setAlwaysOnTop(true);
    }
}