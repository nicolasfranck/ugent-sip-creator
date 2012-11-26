package ugent.bagger.forms;

import gov.loc.repository.bagit.BagFactory;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.params.NewBagParams;

/**
 *
 * @author nicolas
 */
public class NewBagParamsForm extends AbstractForm{    
    public NewBagParamsForm(NewBagParams newBagParams){
        this(FormModelHelper.createFormModel(newBagParams,"newBagParamsForm"));
    }
    public NewBagParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");        
        
        //bagId
        builder.add("bagId");        
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
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                versionComboBox.setSelectedItem(BagFactory.Version.V0_96.versionString);                
            }            
        });
        
        return builder.getForm();
    }    
}