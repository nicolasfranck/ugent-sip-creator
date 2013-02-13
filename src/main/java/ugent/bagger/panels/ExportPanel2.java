package ugent.bagger.panels;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ugent.bagger.helper.Context;
import ugent.bagger.params.ExportParams;

/**
 *
 * @author nicolas
 */
public final class ExportPanel2 extends JPanel{
    ExportParams exportParams;
    JLabel labelMdSecFound;
    JLabel labelValidMdSecFound;
    JLabel mdSecFound;
    JLabel validMdSecFound;
    
    public ExportPanel2(){
        init();
    }
    public ExportParams getExportParams() {
        return exportParams;
    }
    public void setExportParams(ExportParams exportParams) {
        this.exportParams = exportParams;
    }   
    protected void init(){        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JPanel panel = new JPanel(new GridLayout(0,2));      
        panel.add(getLabelMdSecFound());
        panel.add(getMdSecFound());
        panel.add(getLabelValidMdSecFound());
        panel.add(getValidMdSecFound());
        
        add(panel);
        
    }
    public JLabel getLabelMdSecFound() {
        if(labelMdSecFound == null){
            labelMdSecFound = new JLabel(Context.getMessage("ExportPanel2.labelMdSecFound.label"));
            labelMdSecFound.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return labelMdSecFound;
    }
    public JLabel getLabelValidMdSecFound() {
        if(labelValidMdSecFound == null){
            labelValidMdSecFound = new JLabel(Context.getMessage("ExportPanel2.labelValidMdSecFound.label"));                    
            labelValidMdSecFound.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return labelValidMdSecFound;
    }    

    public JLabel getMdSecFound() {
        if(mdSecFound == null){
            mdSecFound = new JLabel();
            mdSecFound.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return mdSecFound;
    }

    public JLabel getValidMdSecFound() {
        if(validMdSecFound == null){
            validMdSecFound = new JLabel();
            validMdSecFound.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return validMdSecFound;
    }
    
}
