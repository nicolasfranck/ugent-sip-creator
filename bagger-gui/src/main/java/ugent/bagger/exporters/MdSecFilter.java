package ugent.bagger.exporters;

import com.anearalone.mets.MdSec;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public interface MdSecFilter {
    ArrayList<MdSec>filter(ArrayList<MdSec>data);
}
