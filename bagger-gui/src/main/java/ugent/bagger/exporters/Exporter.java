package ugent.bagger.exporters;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagit.Bag;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public interface Exporter {
    ArrayList<MdSec>getConversionCandidates(Mets mets);
    void export(Bag bag,Mets mets);
}
