package ugent.bagger.exporters;

import org.w3c.dom.Document;

/**
 *
 * @author nicolas
 */
public interface MetadataConverter {
    Document convert(Document doc) throws Exception;
}
