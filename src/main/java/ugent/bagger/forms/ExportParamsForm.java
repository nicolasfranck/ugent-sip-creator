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
        String [] exporterNames = ExportUtils.getExporterNames();
        
        if(exportParams.getFormat() == null || exportParams.getFormat().isEmpty()){
            exportParams.setFormat(exporterNames[0]);
        }
        
        //outputFile
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            null,
            null,
            JFileChooser.FILES_ONLY,//save dialog type only accepts non existing files, so one need to see this            
            false,
            JFileChooser.SAVE_DIALOG
        );        
        final Binding outputDirBinding = new FileSelectBinding(
            getFormModel(),
            "outputFile",
            fileChooser,            
            SwingUtils.getFrame()
        );                                
        builder.add(outputDirBinding);
        builder.row();
        
        //format
        Binding formatBinding = bf.createBoundComboBox(
            "format",
            exporterNames
        );        
        
        builder.add(formatBinding);
        builder.row();
        
        getFormModel().validate();
       
        return builder.getForm();
    }
}