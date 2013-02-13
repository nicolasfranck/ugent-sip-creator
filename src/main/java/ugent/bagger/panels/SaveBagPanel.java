package ugent.bagger.panels;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Manifest.Algorithm;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.forms.SaveBagParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.BagMode;
import ugent.bagger.params.SaveBagParams;

/**
 *
 * @author nicolas
 */
public final class SaveBagPanel extends JPanel{
    static final Log log = LogFactory.getLog(SaveBagPanel.class);
    SaveBagParams saveBagParams;
    SaveBagParamsForm saveBagParamsForm;           
    
    public SaveBagPanel(){
        setLayout(new BorderLayout());
        add(createContentPane());
    }
    
    public SaveBagParams getSaveBagParams() {
        if(saveBagParams == null){
            saveBagParams = new SaveBagParams();            
            MetsBag metsBag = BagView.getInstance().getBag();
            if(metsBag.getFile() != null){
                saveBagParams.getOutputFile().add(metsBag.getFile());                
                saveBagParams.setAlgorithm(MetsBag.resolveAlgorithm(metsBag.getPayloadManifestAlgorithm()));
                switch(metsBag.getSerialMode()){
                    case MetsBag.ZIP_MODE:
                        saveBagParams.setBagMode(BagMode.ZIP_MODE);
                        break;
                    case MetsBag.TAR_MODE:
                        saveBagParams.setBagMode(BagMode.TAR_MODE);
                        break;
                    case MetsBag.TAR_GZ_MODE:
                        saveBagParams.setBagMode(BagMode.TAR_GZ_MODE);
                        break;
                    case MetsBag.TAR_BZ2_MODE:
                        saveBagParams.setBagMode(BagMode.TAR_BZ2_MODE);
                        break;
                    default:
                        saveBagParams.setBagMode(BagMode.NO_MODE);
                }                
            }else{
                ArrayList<String>bagIds = metsBag.getInfo().getFieldMap().get("Bag-Id");
                if(!bagIds.isEmpty()){                   
                    saveBagParams.getOutputFile().add(new File(SwingUtils.getLastDirectory(),bagIds.get(0)));
                }
            }
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
        }
        return saveBagParamsForm;
    }

    public void setSaveBagParamsForm(SaveBagParamsForm saveBagParamsForm) {
        this.saveBagParamsForm = saveBagParamsForm;
    }
    
    protected JComponent createContentPane() {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
        panel.setLayout(layout);        
        panel.add(getSaveBagParamsForm().getControl());                
        return panel;
    }    
    public void save(){
        if(getSaveBagParamsForm().hasErrors()){
           return; 
        }
        new OkSaveBagHandler().actionPerformed(null);
    }

    class OkSaveBagHandler extends AbstractAction {

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