package ugent.bagger.bagitmets;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;

/**
 *
 * @author nicolas
 */
public interface BagitMetsAnalyser {
    void analyse(MetsBag metsBag,Mets mets);
}
