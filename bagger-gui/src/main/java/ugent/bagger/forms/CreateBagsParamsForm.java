package ugent.bagger.forms;

import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.BagFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bagitmets.MetsFileDateCreated;
import ugent.bagger.bindings.FileSelectBinding;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.CreateBagsParams;

/**
 *
 * @author nicolas
 */
public class CreateBagsParamsForm extends AbstractForm{    
    public CreateBagsParamsForm(CreateBagsParams createBagsParams){
        this(FormModelHelper.createFormModel(createBagsParams,"createBagsParamsForm"));
    }
    public CreateBagsParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");        
        CreateBagsParams createBagParams = (CreateBagsParams) getFormObject();
        
        //directories
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            null,
            null,
            JFileChooser.DIRECTORIES_ONLY,
            true,
        JFileChooser.OPEN_DIALOG);          
        Binding fileSelectBinding = new FileSelectBinding(getFormModel(),"directories",fileChooser,"%s geselecteerd",Context.getMessage("csvParseParamsForm.files.buttonText"),SwingUtils.getFrame());        
        builder.add(fileSelectBinding);
        builder.row();
        
        //keep empty directories
        builder.add("keepEmptyDirectories");
        builder.row();

        //version
        ArrayList<String> versionModel = new ArrayList<String>();                
        for(BagFactory.Version version:BagFactory.Version.values()){
            versionModel.add(version.versionString);
        }
        Binding versionBinding = bf.createBoundComboBox("version",versionModel.toArray());
        final JComboBox versionComboBox = ((JComboBox)versionBinding.getControl());
        
        builder.add(versionBinding);
        builder.row();
        
        
        //profile
        /*String [] profileNames = BagView.getInstance().getProfileStore().getProfileNames();
        Binding profileBinding = bf.createBoundComboBox("profile",profileNames);
        final JComboBox profileComboBox = ((JComboBox)profileBinding.getControl());
        builder.add(profileBinding);
        builder.row();*/
        
        for(String key:new String [] {"metadataPaths","keepMetadata","addDC","writeToBagInfo"}){
            builder.add(key);
            builder.row();
        }   
        
        //metsFileDateCreated
        Binding metsFileDateCreatedBinding = bf.createBoundComboBox(
            "metsFileDateCreated",
            MetsFileDateCreated.values()
        );
        builder.add(metsFileDateCreatedBinding);
        builder.row();
        
        //bagInPlace
        builder.add("bagInPlace");
        builder.row();
        
        //outputDir
        fileChooser = SwingUtils.createFileChooser(null,null,JFileChooser.DIRECTORIES_ONLY,false,JFileChooser.OPEN_DIALOG);        
        final Binding outputDirBinding = new FileSelectBinding(getFormModel(),"outputDir",fileChooser,"%s geselecteerd",Context.getMessage("csvParseParamsForm.files.buttonText"),SwingUtils.getFrame());                        
        SwingUtils.setJComponentEnabled(outputDirBinding.getControl(),!createBagParams.isBagInPlace());
        builder.add(outputDirBinding);
        builder.row();
        
        //relatie bagInPlace en outputDir
        getValueModel("bagInPlace").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Boolean b = (Boolean) pce.getNewValue();
                SwingUtils.setJComponentEnabled(outputDirBinding.getControl(),!b);                
            }            
        });
        
        getValueModel("outputDir").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                ArrayList<File>list = (ArrayList<File>)pce.getNewValue();
                
                boolean success = false;
                if(list.isEmpty()){
                    SwingUtils.ShowError("","Gelieve een map te selecteren");
                }else if(!list.get(0).canWrite()){
                    SwingUtils.ShowError("","Systeem kan niet schrijven naar "+list.get(0).getAbsolutePath());
                }else if(list.get(0).listFiles().length > 0){
                    SwingUtils.ShowError("",list.get(0).getAbsolutePath()+" moet leeg zijn");
                }else{                
                    success = true;
                }                
                
                if(!success){                    
                    getValueModel("bagInPlace").setValue(true);
                }
            }            
        });
        
        getValueModel("metadataPaths").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                getValueModel("metadata").setValue(parseMetadataPaths((String) pce.getNewValue()));
            }            
        });
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                versionComboBox.setSelectedItem(BagFactory.Version.V0_96.versionString);                
            }            
        });
        
        return builder.getForm();
    }
    protected static String [] parseMetadataPaths(String text){        
        String [] data = text.replaceAll("\\s","").split(",");
        if(data == null){
            data = new String [] {};
        }                
        return data;        
    }
}