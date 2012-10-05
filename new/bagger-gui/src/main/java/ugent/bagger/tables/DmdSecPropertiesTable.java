package ugent.bagger.tables;

import com.anearalone.mets.MdSec;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class DmdSecPropertiesTable extends MdSecPropertiesTable{
    public DmdSecPropertiesTable(final ArrayList<MdSec>data,String [] cols,String id){
        super(data,cols,id);
    }
}