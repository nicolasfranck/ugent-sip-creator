package ugent.bagger.forms;

import gov.loc.repository.bagit.Manifest.Algorithm;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bindings.FileSelectBinding;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.BagMode;
import ugent.bagger.params.SaveBagParams;

/**
 *
 * @author nicolas
 */
public class SaveBagParamsForm extends AbstractForm{
    
    public SaveBagParamsForm(SaveBagParams saveBagParams){
        this(FormModelHelper.createFormModel(saveBagParams,"saveBagParamsForm"));
    }
    public SaveBagParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");        
        
        //outputFile        
        JFileChooser fileChooser = SwingUtils.createFileChooser(
             null,
             null,
             JFileChooser.FILES_AND_DIRECTORIES,
             true,
             JFileChooser.SAVE_DIALOG
        );          
        Binding fileSelectBinding = new FileSelectBinding(
            getFormModel(),
            "outputFile",
            fileChooser,            
            SwingUtils.getFrame()
        );        
        builder.add(fileSelectBinding);
        builder.row();   
        
        //bagMode
        Binding bm = bf.createBoundComboBox("bagMode",BagMode.values());        
        builder.add(bm);
        builder.row();
        
        //algorithm for both payloads and tags
        Binding ba = bf.createBoundComboBox("algorithm",Algorithm.values());        
        builder.add(ba);
        builder.row();
        
        getFormModel().validate();
        
        return builder.getForm();
    }
}
