package ugent.bagger.forms;

import gov.loc.repository.bagger.ui.handlers.ExportUtils;
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
import ugent.bagger.params.ExportParams;



/**
 *
 * @author nicolas
 */
public class ExportParamsForm extends AbstractForm{
    
    public ExportParamsForm(ExportParams exportParams){
        this(FormModelHelper.createFormModel(exportParams,"exportParamsForm"));
    }
    public ExportParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {    
        
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");   
        
        ExportParams exportParams = (ExportParams) getFormObject();       
        
        //outputFile
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            null,
            null,
            JFileChooser.FILES_AND_DIRECTORIES,//save dialog type only accepts non existing files, so one need to see this            
            false,
            JFileChooser.SAVE_DIALOG
        );        
        final Binding outputDirBinding = new FileSelectBinding(
            getFormModel(),
            "outputFile",
            fileChooser,
            Context.getMessage("JFileChooser.selected"),
            Context.getMessage("JFileChooser.buttonText"),
            SwingUtils.getFrame()
        );                                
        builder.add(outputDirBinding);
        builder.row();
        
        //format
        Binding formatBinding = bf.createBoundComboBox(
            "format",
            ExportUtils.getExporterNames()
        );        
        builder.add(formatBinding);
        builder.row();
       
        return builder.getForm();
    }
}