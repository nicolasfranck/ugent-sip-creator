package ugent.bagger.panels;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ugent.bagger.forms.ExportParamsForm;
import ugent.bagger.params.ExportParams;

/**
 *
 * @author nicolas
 */
public class ExportParamsPanel extends JPanel{
    ExportParams exportParams;
    ExportParamsForm exportParamsForm; 
   
    public ExportParamsPanel(){
        init();
    }
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));                
        JComponent form = getExportParamsForm().getControl();
        form.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(form);        
    }

    public ExportParams getExportParams() {
        if(exportParams == null){
            exportParams = new ExportParams();
        }
        return exportParams;
    }

    public void setExportParams(ExportParams exportParams) {
        this.exportParams = exportParams;
    }

    public ExportParamsForm getExportParamsForm() {
        if(exportParamsForm == null){
            exportParamsForm = new ExportParamsForm(getExportParams());             
        }
        return exportParamsForm;
    }

    public void setExportParamsForm(ExportParamsForm exportParamsForm) {
        this.exportParamsForm = exportParamsForm;
    }   
   
  
}
