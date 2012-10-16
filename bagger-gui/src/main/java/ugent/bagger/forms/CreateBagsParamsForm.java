package ugent.bagger.forms;

import gov.loc.repository.bagit.BagFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bindings.FileSelectBinding;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.CreateBagsParams;
import ugent.rename.ErrorAction;

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
        
        JFileChooser fileChooser = SwingUtils.createFileChooser(null,null,JFileChooser.DIRECTORIES_ONLY,true,JFileChooser.OPEN_DIALOG);        
        Binding fileSelectBinding = new FileSelectBinding(getFormModel(),"directories",fileChooser,"%s geselecteerd",SwingUtils.getFrame());        
        builder.add(fileSelectBinding);
        builder.row();

        
        ArrayList<String> versionModel = new ArrayList<String>();                
        for(BagFactory.Version version:BagFactory.Version.values()){
            versionModel.add(version.versionString);
        }
        Binding bagVersionBinding = bf.createBoundComboBox("bagVersion",versionModel.toArray());
        final JComboBox comboBox = ((JComboBox)bagVersionBinding.getControl());
        comboBox.setSelectedItem(BagFactory.Version.V0_96.versionString);
        builder.add(bagVersionBinding);
        builder.row();
        
        builder.add("bagInPlace");
        builder.row();
        
        fileChooser = SwingUtils.createFileChooser(null,null,JFileChooser.DIRECTORIES_ONLY,false,JFileChooser.OPEN_DIALOG);        
        final Binding outputDirBinding = new FileSelectBinding(getFormModel(),"outputDir",fileChooser,"%s geselecteerd",SwingUtils.getFrame());                        
        SwingUtils.setJComponentEnabled(outputDirBinding.getControl(),!createBagParams.isBagInPlace());
        builder.add(outputDirBinding);
        builder.row();
        
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
                if(list.size() == 0){
                    SwingUtils.ShowError("","Gelieve een map te selecteren");
                }else if(!list.get(0).canWrite()){
                    SwingUtils.ShowError("","Systeem kan niet schrijven naar "+list.get(0).getAbsolutePath());
                }else if(list.get(0).listFiles().length > 0){
                    SwingUtils.ShowError("",list.get(0).getAbsolutePath()+" moet leeg zijn");
                }else{                
                    success = true;
                }                
                
                if(!success){                    
                    getValueModel("bagInPlace").setValue(new Boolean(true));
                }
            }            
        });
        
        return builder.getForm();
    }
}
