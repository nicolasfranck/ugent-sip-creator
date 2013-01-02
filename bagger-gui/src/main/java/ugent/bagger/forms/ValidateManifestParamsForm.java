package ugent.bagger.forms;

import gov.loc.repository.bagit.Manifest;
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
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.ValidateManifestParams;

/**
 *
 * @author nicolas
 */
public class ValidateManifestParamsForm extends AbstractForm{
    
    public ValidateManifestParamsForm(ValidateManifestParams validateManifestParams){
        this(FormModelHelper.createFormModel(validateManifestParams,"validateManifestParamsForm"));
    }
    public ValidateManifestParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {    
        
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");  
        
        
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            "manifest", //title
            new FileExtensionFilter(
                new String [] {"txt"},
                Context.getMessage(getId()+".fileFilter.label"),
                true
            ), //filter
            JFileChooser.FILES_ONLY, //modus
            true, //multi selection enabled
            JFileChooser.OPEN_DIALOG //nieuw of bestaand bestand?
        );
        FileSelectBinding fileSelectBinding = new FileSelectBinding(
            getFormModel(),
            "files",
            fileChooser,            
            SwingUtils.getFrame()
        );
        builder.add(fileSelectBinding);        
        builder.row();       
        
        Binding bagInfoConverterBinding = bf.createBoundComboBox("algorithm",Manifest.Algorithm.values());        
        builder.add(bagInfoConverterBinding);
        builder.row();
       
        return builder.getForm();
    }
}