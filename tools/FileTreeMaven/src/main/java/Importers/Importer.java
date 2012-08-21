/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Importers;

import java.io.File;
import org.w3c.dom.Document;

/**
 *
 * @author nicolas
 */
public interface Importer {
    Document performImport(File file);
}
