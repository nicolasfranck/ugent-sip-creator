package ugent.bagger.importers;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import org.w3c.dom.Document;

/**
 *
 * @author nicolas
 */
public interface Importer {
    Document performImport(File file);
    Document performImport(URL url);
    Document performImport(InputStream is);
}
