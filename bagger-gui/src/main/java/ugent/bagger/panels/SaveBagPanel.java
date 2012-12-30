package ugent.bagger.panels;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Manifest.Algorithm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.command.ActionCommand;
import ugent.bagger.forms.SaveBagParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.SaveBagParams;

/**
 *
 * @author nicolas
 */
public final class SaveBagPanel extends JPanel{
    static final Log log = LogFactory.getLog(CreateBagsPanel.class);
    SaveBagParams saveBagParams;
    SaveBagParamsForm saveBagParamsForm;   
    ActionCommand finishCommand;
    ActionCommand cancelCommand;
    JButton okButton;
    JButton cancelButton;
    
    public SaveBagPanel(){
        setLayout(new BorderLayout());
        add(createContentPane());
    }

    protected JButton getOkButton() {
        if(okButton == null){
            okButton = new JButton(Context.getMessage("ok"));
            okButton.setEnabled(false);
            okButton.addActionListener(new OkSaveBagHandler());
        }
        return okButton;
    }

    protected JButton getCancelButton() {
        if(cancelButton == null){
            cancelButton = new JButton(Context.getMessage("cancel"));
            cancelButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    SaveBagPanel.this.firePropertyChange("close",null,null);
                }            
            });
        }
        return cancelButton;
    }

    
    public SaveBagParams getSaveBagParams() {
        if(saveBagParams == null){
            saveBagParams = new SaveBagParams();
        }
        return saveBagParams;
    }

    public void setSaveBagParams(SaveBagParams saveBagParams) {
        this.saveBagParams = saveBagParams;
        getSaveBagParamsForm().setFormObject(saveBagParams);
    }

    public SaveBagParamsForm getSaveBagParamsForm() {
        if(saveBagParamsForm == null){
            saveBagParamsForm = new SaveBagParamsForm(getSaveBagParams());
            saveBagParamsForm.addValidationListener(new ValidationListener(){
                @Override
                public void validationResultsChanged(ValidationResults vr) {                   
                    getOkButton().setEnabled(!vr.getHasErrors());
                }                
            });
            
        }
        return saveBagParamsForm;
    }

    public void setSaveBagParamsForm(SaveBagParamsForm saveBagParamsForm) {
        this.saveBagParamsForm = saveBagParamsForm;
    }
    
    protected JComponent createContentPane() {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel,BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        panel.add(getSaveBagParamsForm().getControl());
        panel.add(createButtonPanel());
        
        return panel;
    }
    protected JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));       
        panel.add(getOkButton());        
        panel.add(getCancelButton());
        return panel;
    }

    private class OkSaveBagHandler extends AbstractAction {
	private static final long serialVersionUID = 1L;

        @Override
	public void actionPerformed(ActionEvent e) {
            //commit changes
            getSaveBagParamsForm().commit();            
           
            if(getSaveBagParams().getOutputFile().isEmpty()){
                SwingUtils.ShowError(
                    Context.getMessage("SaveBagDialog.filenamemissing.title"),
                    Context.getMessage("SaveBagDialog.filenamemissing.label")
                );
                return;
            }
            MetsBag metsBag = BagView.getInstance().getBag();
            
            //manifests verplicht
            metsBag.isBuildPayloadManifest(true);
            metsBag.isBuildTagManifest(true);
            
            //manifest algoritme
            Algorithm alg = getSaveBagParams().getAlgorithm();
            metsBag.setPayloadManifestAlgorithm(alg.bagItAlgorithm);
            metsBag.setTagManifestAlgorithm(alg.bagItAlgorithm);
            
            //type opslag
            short serialMode = (short)getSaveBagParams().getBagMode().getBagitMode();
            metsBag.setSerialMode(serialMode);
            
            //output - file/name
            File bagFile = getSaveBagParams().getOutputFile().get(0);
            String bagFileName = bagFile.getName();
            
            switch(serialMode){
                case DefaultBag.ZIP_MODE:
                    if(!bagFileName.toLowerCase().endsWith(".zip")){
                        bagFileName += ".zip";
                        bagFile = new File(bagFile.getParentFile(),bagFileName);
                    }
                    break;
                case DefaultBag.TAR_MODE:
                    if(!bagFileName.toLowerCase().endsWith(".tar")){
                        bagFileName += ".tar";
                        bagFile = new File(bagFile.getParentFile(),bagFileName);
                    }
                    break;
                case DefaultBag.TAR_GZ_MODE:
                    if(!bagFileName.toLowerCase().endsWith(".tar.gz")){
                        bagFileName += ".tar.gz";
                        bagFile = new File(bagFile.getParentFile(),bagFileName);
                    }
                    break;
                case DefaultBag.TAR_BZ2_MODE:
                    if(!bagFileName.toLowerCase().endsWith(".tar.bz2")){
                        bagFileName += ".tar.bz2";
                        bagFile = new File(bagFile.getParentFile(),bagFileName);
                    }
                    break;                    
            }            
           
            SaveBagPanel.this.firePropertyChange("close",null,null);
            
            metsBag.setName(bagFileName);
            BagView.getInstance().saveBagHandler.save(bagFile);           
        }
    }
   
}