package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public class BagInfoImportParams {
    private ArrayList<File>files;
    private BagInfoConverter bagInfoConverter = BagInfoConverter.DC;

    public ArrayList<File> getFiles() {
        return files;
    }
    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
    public BagInfoConverter getBagInfoConverter() {
        return bagInfoConverter;
    }
    public void setBagInfoConverter(BagInfoConverter bagInfoConverter) {
        this.bagInfoConverter = bagInfoConverter;
    }
    public static enum BagInfoConverter  {
        DC("DC"),OAI_DC("OAI-DC");
        private String c;        
        private BagInfoConverter(String c){
            this.c = c;
        }
        @Override
        public String toString(){
            String out = c;
            try{
                String formatted = Context.getMessage(c);
                out = formatted != null ? formatted:c;
            }catch(Exception e){}
            return out; 
        }
    }
    
}
