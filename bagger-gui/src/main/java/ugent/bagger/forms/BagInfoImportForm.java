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
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.BagInfoImportParams;



/**
 *
 * @author nicolas
 */
public class BagInfoImportForm extends AbstractForm{
    
    public BagInfoImportForm(BagInfoImportParams bagInfoImportParams){
        this(FormModelHelper.createFormModel(bagInfoImportParams,"bagInfoImportForm"));
    }
    public BagInfoImportForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {    
        
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");   
        
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            "bag-info.txt",
            new FileExtensionFilter(new String [] {"txt"},Context.getMessage("bagInfoImportForm.fileFilter.label"),true),
            JFileChooser.FILES_ONLY,false,JFileChooser.OPEN_DIALOG
        );
        FileSelectBinding fileSelectBinding = new FileSelectBinding(
            getFormModel(),
            "files",
            fileChooser,
            "%s",
            Context.getMessage("bagInfoImportForm.files.buttonText"),
            SwingUtils.getFrame()
        );
        builder.add(fileSelectBinding);        
        builder.row();
       
        Binding bagInfoConverterBinding = bf.createBoundComboBox("",BagInfoImportParams.BagInfoConverter.values());
        builder.add(bagInfoConverterBinding);
        builder.row();
       
        return builder.getForm();
    }
}