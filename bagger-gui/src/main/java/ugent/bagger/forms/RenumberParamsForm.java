package ugent.bagger.forms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bindings.JSpinnerNumberBinding;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.RenumberParams;
import ugent.rename.ErrorAction;
import ugent.rename.PaddingChar;
import ugent.rename.PreSort;
import ugent.rename.Radix;
import ugent.rename.StartPosRelative;
import ugent.rename.StartPosType;

/**
 *
 * @author nicolas
 */
public class RenumberParamsForm extends AbstractForm{
    
    public RenumberParamsForm(RenumberParams renumberParams){
        this(FormModelHelper.createFormModel(renumberParams,"renumberForm"));
    }
    public RenumberParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        RenumberParams renumberParams = (RenumberParams)getFormObject();
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");        
                   
        builder.add(new JSpinnerNumberBinding(
            getFormModel(),"start",renumberParams.getStart(),0,Integer.MAX_VALUE,1
        ));              
        builder.row();  
        
        builder.add(new JSpinnerNumberBinding(
            getFormModel(),"step",renumberParams.getStep(),1,Integer.MAX_VALUE,1
        ));              
        builder.row(); 
        
        final Binding radixBinding = bf.createBoundComboBox("radix",Radix.values());
        builder.add(radixBinding); 
        builder.row();
        
        builder.add("separatorBefore");   
        builder.row();
        builder.add("separatorAfter");
        builder.row();
        
        Binding bindingStartPosType = bf.createBoundComboBox("startPosType",StartPosType.values());        
        builder.add(bindingStartPosType);
        builder.row();
        
        final JComponent [] startPosComponents = builder.add(new JSpinnerNumberBinding(
            getFormModel(),"startPos",renumberParams.getStartPos(),0,Integer.MAX_VALUE,1
        ));              
        builder.row();
        
        final Binding startPosRelativeBinding = bf.createBoundComboBox("startPosRelative",StartPosRelative.values());
        builder.add(startPosRelativeBinding); 
        builder.row();
        
        getFormModel().getValueModel("startPosType").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                StartPosType startPosType = (StartPosType)pce.getNewValue();                
                if(startPosType.equals(StartPosType.ABSOLUTE)){
                    startPosRelativeBinding.getControl().setVisible(false);
                    startPosRelativeBinding.getControl().setEnabled(false);
                    for(JComponent component:startPosComponents){
                        component.setVisible(true);
                        component.setEnabled(true);
                    }                    
                }else{
                    startPosRelativeBinding.getControl().setVisible(true);                    
                    startPosRelativeBinding.getControl().setEnabled(true);
                    for(JComponent component:startPosComponents){
                        component.setVisible(false);
                        component.setEnabled(false);
                    }                    
                }
            }            
        });
        
        if(renumberParams.getStartPosType().equals(StartPosType.RELATIVE)){
            startPosRelativeBinding.getControl().setVisible(true);                    
            startPosRelativeBinding.getControl().setEnabled(true);
            for(JComponent component:startPosComponents){
                component.setVisible(false);
                component.setEnabled(false);
            }
        }else{
            startPosRelativeBinding.getControl().setVisible(false);
            startPosRelativeBinding.getControl().setEnabled(false);
            for(JComponent component:startPosComponents){
                component.setVisible(true);
                component.setEnabled(true);
            }
        }
        
        builder.add(bf.createBoundComboBox("preSort",PreSort.values()));
        builder.row();
                
        
        final JComponent [] paddingComponents = builder.add(new JSpinnerNumberBinding(
            getFormModel(),"padding",renumberParams.getPadding(),0,Integer.MAX_VALUE,1
        ));              
        builder.row();
              
        //paddingChar
        final Binding paddingCharBinding = bf.createBoundComboBox("paddingChar",PaddingChar.values());        
        builder.add(paddingCharBinding);
        builder.row();
        
        getFormModel().getValueModel("radix").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Radix radix = (Radix) pce.getNewValue();
                boolean isEnabled = radix != Radix.ALPHABETHICAL;
                for(JComponent component:paddingComponents){
                    component.setEnabled(isEnabled);
                }
                SwingUtils.setJComponentEnabled(paddingCharBinding.getControl(),isEnabled);
            }
            
        });
        
        //overWrite
        builder.add("overWrite");
        builder.row();
        
        //onError
        Binding onErrorActionBinding = bf.createBoundComboBox("onErrorAction",ErrorAction.values());
        final JComboBox comboBox = ((JComboBox)onErrorActionBinding.getControl());
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                comboBox.setEditable(false)                ;
                comboBox.setSelectedItem(ErrorAction.ignore);
            }        
        });
        builder.add(onErrorActionBinding);
        builder.row();
        
        //valideer huidige toestand?        
        getFormModel().commit();       
        return builder.getForm();
    }
}