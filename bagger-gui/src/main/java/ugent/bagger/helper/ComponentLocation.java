package ugent.bagger.helper;

import javax.swing.JComponent;

/**
 *
 * @author nicolas
 */
public class ComponentLocation {
    private JComponent parentComponent;
    private int indexChild;

    public ComponentLocation(JComponent parentComponent,int indexChild){
        this.parentComponent = parentComponent;
        this.indexChild = indexChild;
    }
    public int getIndexChild() {
        return indexChild;
    }
    public void setIndexChild(int indexChild) {
        this.indexChild = indexChild;
    }
    public JComponent getParentComponent() {
        return parentComponent;
    }
    public void setParentComponent(JComponent parentComponent) {
        this.parentComponent = parentComponent;
    }    
}
