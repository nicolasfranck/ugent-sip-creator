package ugent.bagger.bindings;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

/**
 *
 * @author nicolas
 */
public final class JSpinnerNumberBinding extends CustomBinding{
    private int min;
    private int max;
    private int init;
    private int step;
    private JSpinner spinner;
    private SpinnerModel spinnerModel;
    public JSpinnerNumberBinding(FormModel formModel, String formPropertyPath,int init,int min,int max,int step){
        super(formModel,formPropertyPath,Integer.class);
        setMin(min);
        setMax(max);
        setStep(step);  
        setInit(init);
    }
    public int getInit() {
        return init;
    }

    public void setInit(int init) {
        this.init = init;
    }

    public SpinnerModel getSpinnerModel() {
        if(spinnerModel == null){             
            spinnerModel = new SpinnerNumberModel(getInit(),getMin(),getMax(),getStep());        
        }
        return spinnerModel;
    }

    public void setSpinnerModel(SpinnerModel spinnerModel) {
        this.spinnerModel = spinnerModel;
    }
    
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
    
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public JSpinner getSpinner() {
        if(spinner == null){           
            spinner = new JSpinner(
                getSpinnerModel()
            );
            spinner.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent ce) {                    
                    getFormModel().getValueModel(
                        formPropertyPath
                    ).setValue(getSpinnerModel().getValue());
                }                
            });            
        }
        return spinner;
    }
    public void setSpinner(JSpinner spinner) {
        this.spinner = spinner;
    }   
    @Override
    protected void valueModelChanged(Object o){
        getSpinner().setValue(o);
    }
    @Override
    protected JComponent doBindControl() {
        return getSpinner();
    }

    @Override
    protected void readOnlyChanged() {        
    }

    @Override
    protected void enabledChanged() {        
    }    
}