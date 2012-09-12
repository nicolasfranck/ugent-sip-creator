/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.importers;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class ImporterFactory {
    public static Importer createImporter(File file){
        String extension = getExtension(file.getName());
        if(extension.compareToIgnoreCase("txt") == 0){
            return new NameValueToDCImporter();
        }
        return null;
    }
    private static String getExtension(String name){
        int posDot = name.lastIndexOf(".");
        if(
            posDot >= 0 && posDot < (name.length() - 1)
        ){
            return name.substring(posDot + 1);
        }
        return null;
    }
}
