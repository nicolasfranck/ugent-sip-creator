package ugent.bagger.params;

import gov.loc.repository.bagit.utilities.SimpleResult;
import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagValidationResult {
    private File file;
    private SimpleResult result;
    private boolean valid;
    private boolean complete;
    
    public BagValidationResult(File file,SimpleResult result,boolean valid,boolean complete){
        this.file = file;
        this.result = result;
        this.valid = valid;
        this.complete = complete;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public SimpleResult getResult() {
        return result;
    }

    public void setResult(SimpleResult result) {
        this.result = result;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    
}
