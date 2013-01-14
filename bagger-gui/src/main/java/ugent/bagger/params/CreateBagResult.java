package ugent.bagger.params;

import java.io.File;
import ugent.bagger.helper.ArrayUtils;

/**
 *
 * @author nicolas
 */
public class CreateBagResult {
    File inputFile;
    File outputFile;
    String [] errors; 

    public CreateBagResult(File inputFile,File outputFile,String [] errors){
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.errors = errors;
    }
    public File getInputFile() {
        return inputFile;
    }
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }
    public File getOutputFile() {
        return outputFile;
    }
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }
    public String[] getErrors() {
        if(errors == null){
            errors = new String [] {};
        }
        return errors;
    }
    public void setErrors(String[] errors) {
        this.errors = errors;
    }
    public boolean isSuccess(){
        return getErrors().length == 0;
    }
    public String getErrorString(){        
        return ArrayUtils.join(getErrors(),", ");
    }
}