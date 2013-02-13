package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class ExportParams {    
    String format;
    ArrayList<File>outputFile;
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public ArrayList<File> getOutputFile() {
        if(outputFile == null){
            outputFile = new ArrayList<File>();
        }
        return outputFile;
    }
    public void setOutputFile(ArrayList<File> outputFile) {
        this.outputFile = outputFile;
    }
}
