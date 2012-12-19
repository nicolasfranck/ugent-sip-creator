package ugent.bagger.forms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
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
import ugent.bagger.params.CreateBagsParams;

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
        
        //directories
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            null,
            null,
            JFileChooser.DIRECTORIES_ONLY,
            true,
        JFileChooser.OPEN_DIALOG);          
        Binding fileSelectBinding = new FileSelectBinding(
            getFormModel(),
            "directories",
            fileChooser,
            Context.getMessage("JFileChooser.selected"),
            Context.getMessage("JFileChooser.buttonText"),
            SwingUtils.getFrame()
        );        
        builder.add(fileSelectBinding);
        builder.row();
        
        //keep empty directories
        builder.add("keepEmptyDirectories");
        builder.row();
        
        for(String key:new String [] {"metadataPaths","keepMetadata","addDC","writeToBagInfo"}){
            builder.add(key);
            builder.row();
        }   
        
        //bagInPlace
        builder.add("bagInPlace");
        builder.row();
        
        //outputDir
        fileChooser = SwingUtils.createFileChooser(null,null,JFileChooser.DIRECTORIES_ONLY,false,JFileChooser.OPEN_DIALOG);        
        final Binding outputDirBinding = new FileSelectBinding(
            getFormModel(),
            "outputDir",
            fileChooser,
            Context.getMessage("JFileChooser.selected"),
            Context.getMessage("JFileChooser.buttonText"),
            SwingUtils.getFrame()
        );                        
        SwingUtils.setJComponentEnabled(outputDirBinding.getControl(),!createBagParams.isBagInPlace());
        builder.add(outputDirBinding);
        builder.row();
        
        //relatie bagInPlace en outputDir
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
                if(list.isEmpty()){
                    SwingUtils.ShowError(
                        Context.getMessage("createBagsParamsForm.error.title"),
                        Context.getMessage("createBagsParamsForm.error.selectoutputDir")                        
                    );
                }else if(!list.get(0).canWrite()){
                    SwingUtils.ShowError(
                        Context.getMessage("createBagsParamsForm.error.title"),
                        Context.getMessage(
                            "createBagsParamsForm.error.outputDirNotWritable",
                            new Object [] {list.get(0).getAbsolutePath()}
                        )
                    );
                }else if(list.get(0).listFiles().length > 0){
                    SwingUtils.ShowError(
                        Context.getMessage("CreateBagsParamsForm.error.title"),
                        Context.getMessage(
                            "createBagsParamsForm.error.outputDirNotEmpty",
                            new Object [] {list.get(0).getAbsolutePath()}
                        )                        
                    );
                }else{                
                    success = true;
                }                
                
                if(!success){                    
                    getValueModel("bagInPlace").setValue(true);
                }
            }            
        });
        
        getValueModel("metadataPaths").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                   
                getValueModel("metadata").setValue(parseMetadataPaths((String) pce.getNewValue()));
            }            
        });        
        
        return builder.getForm();
    }
    protected static String [] parseMetadataPaths(String text){        
        String [] data = text.replaceAll("\\s","").split(",");
        if(data == null){
            data = new String [] {};
        }                
        return data;        
    }
}