package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;
import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class BagInfoImportParams {
    private ArrayList<File>files;
    private String bagInfoConverter;

    public ArrayList<File> getFiles() {
        if(files == null){
            files = new ArrayList<File>();
        }
        return files;
    }
    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public String getBagInfoConverter() {
        if(bagInfoConverter == null){
            if(MetsUtils.getBagInfoImporters().size() > 0){
                bagInfoConverter = MetsUtils.getBagInfoImporters().keySet().iterator().next();
            }
        }
        return bagInfoConverter;
    }
    public void setBagInfoConverter(String bagInfoConverter) {
        this.bagInfoConverter = bagInfoConverter;
    }     
}
