package gov.loc.repository.bagger.ui;

import javax.swing.JScrollPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class BagTreePanel extends JScrollPane {
    private static final long serialVersionUID = 5134745573017768256L;
    private static final Log log = LogFactory.getLog(BagTreePanel.class);
    private BagTree bagTree;

    public BagTreePanel(BagTree bagTree){    	
        setBagTree(bagTree);    	
    	init();
    }    
    private void init() {
    	log.debug("BagTreePanel.init");        
        setViewportView(getBagTree());       
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setPreferredSize(getBagTree().getTreeSize());
    }    
    public void setBagTree(BagTree bagTree) {
    	this.bagTree = bagTree;
    }    
    public BagTree getBagTree() {
    	return bagTree;
    }    
    public void refresh(BagTree bagTree) {
    	this.bagTree = bagTree;        
    	if(getComponentCount() > 0){
            if (this.bagTree != null && this.bagTree.isShowing()) {
                this.bagTree.invalidate();
            }
    	}
    	init();
    	invalidate();
    	repaint();
    }
}