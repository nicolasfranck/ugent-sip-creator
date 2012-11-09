package ugent.bagger.forms;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bindings.FileSelectBinding;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.BagValidateParams;



/**
 *
 * @author nicolas
 */
public class BagValidateParamsForm extends AbstractForm{
    
    public BagValidateParamsForm(BagValidateParams bagValidateParams){
        this(FormModelHelper.createFormModel(bagValidateParams,"bagValidateParamsForm"));
    }
    public BagValidateParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {    
        
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");   
        
        builder.add("valid");
        
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            "bags", //title
            null, //filter
            JFileChooser.FILES_AND_DIRECTORIES, //modus
            true, //multi selection enabled
            JFileChooser.OPEN_DIALOG //nieuw of bestaand bestand?
        );
        FileSelectBinding fileSelectBinding = new FileSelectBinding(
            getFormModel(),
            "files",
            fileChooser,
            Context.getMessage("bagValidateParamsForm.files.selected"),
            Context.getMessage("bagValidateParamsForm.files.buttonText"),
            SwingUtils.getFrame()
        );
        builder.add(fileSelectBinding);                            
       
        return builder.getForm();
    }
}