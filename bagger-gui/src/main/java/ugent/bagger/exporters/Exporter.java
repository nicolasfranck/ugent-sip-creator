package ugent.bagger.exporters;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public interface Exporter {       
    ArrayList<MdSec>getConversionCandidates(Mets mets);
    void export(MetsBag metsbag,Mets mets);
}
