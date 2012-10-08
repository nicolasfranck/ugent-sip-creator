package ugent.bagger.importers;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class ImporterFactory {
    public static Importer createImporter(File file,ImportType importType){
        if(importType == ImportType.CSV){
            throw new UnsupportedOperationException("Not supported yet.");
        }else if(importType == ImportType.BAG_INFO){
            return new NameValueToDCImporter();
        }
        return null;
    }   
}
