package ugent.bagger.forms;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bindings.FileSelectBinding;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.CSVParseParams;

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
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());        
        builder.setLabelAttributes("colSpan=1 align=left");   
        
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            "CSV",
            new FileExtensionFilter(new String [] {"csv"},"csv files only",true),
            JFileChooser.FILES_ONLY,false,JFileChooser.OPEN_DIALOG
        );
        FileSelectBinding fileSelectBinding = new FileSelectBinding(
            getFormModel(),
            "file",
            fileChooser,
            "%s",
            SwingUtils.getFrame()
        );
        builder.add(fileSelectBinding);
        builder.row();
        
        String [] fields = {
            "delimiterChar","quoteChar","endOfLineSymbols","surroundingSpacesNeedQuotes"
        };
        for(String field:fields){
            builder.add(field);
            builder.row();
        }
        return builder.getForm();
    }
}