package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class CrosswalkParams {
    String transformFromNamespace;
    String transformToNamespace;
    ArrayList<File>file;

    public String getTransformFromNamespace() {
        return transformFromNamespace;
    }

    public void setTransformFromNamespace(String transformFromNamespace) {
        this.transformFromNamespace = transformFromNamespace;
    }

    public String getTransformToNamespace() {
        return transformToNamespace;
    }

    public void setTransformToNamespace(String transformToNamespace) {
        this.transformToNamespace = transformToNamespace;
    }

    public ArrayList<File> getFile() {
        if(file == null){
            file = new ArrayList<File>();
        }
        return file;
    }

    public void setFile(ArrayList<File> file) {
        this.file = file;
    }
    
}