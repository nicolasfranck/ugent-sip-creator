package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;
import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class BagInfoImportParams {
    ArrayList<File>files;
    VelocityTemplate template;

    public ArrayList<File> getFiles() {
        if(files == null){
            files = new ArrayList<File>();
        }
        return files;
    }
    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
    public VelocityTemplate getTemplate() {
        if(template == null){
            template = MetsUtils.getBaginfoTemplates().get(0);
        }
        return template;
    }
    public void setTemplate(VelocityTemplate template) {
        this.template = template;
    }  
}
