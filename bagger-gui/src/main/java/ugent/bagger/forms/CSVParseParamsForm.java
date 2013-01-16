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
import ugent.bagger.params.CSVDelimiterChar;
import ugent.bagger.params.CSVParseParams;
import ugent.bagger.params.CSVQuoteChar;



/**
 *
 * @author nicolas
 */
public class CSVParseParamsForm extends AbstractForm{
    
    public CSVParseParamsForm(CSVParseParams csvParseParams){
        this(FormModelHelper.createFormModel(csvParseParams,"csvParseParamsForm"));
    }
    public CSVParseParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {    
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");   
        
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            "CSV",
            new FileExtensionFilter(new String [] {"csv"},Context.getMessage("csvParseParamsForm.fileFilter.label"),true),
            JFileChooser.FILES_ONLY,false,JFileChooser.OPEN_DIALOG
        );
        FileSelectBinding fileSelectBinding = new FileSelectBinding(
            getFormModel(),
            "files",
            fileChooser,            
            SwingUtils.getFrame()
        );
        builder.add(fileSelectBinding);        
        builder.row();
        
        Binding bindingDelimiterChar = bf.createBoundComboBox("delimiterChar",CSVDelimiterChar.values());
        builder.add(bindingDelimiterChar);
        builder.row();
        
        Binding bindingQuoteChar = bf.createBoundComboBox("quoteChar",CSVQuoteChar.values());
        builder.add(bindingQuoteChar);
        builder.row();        
        
        builder.add("surroundingSpacesNeedQuotes");
        builder.row();
        return builder.getForm();
    }
}