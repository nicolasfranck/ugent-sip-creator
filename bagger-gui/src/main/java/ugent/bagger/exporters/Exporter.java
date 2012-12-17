package ugent.bagger.exporters;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import ugent.bagger.bagitmets.validation.BagitMetsValidator;
import ugent.bagger.exceptions.BagitMetsValidationException;

/**
 *
 * @author nicolas
 */
public abstract class Exporter {           
    public abstract MetadataConverter getMetadataConverter();
    public abstract void setMetadataConverter(MetadataConverter converter);
    public abstract ArrayList<MdSec>getMetadata(Mets mets);
    public abstract void export(MetsBag metsBag,Mets mets,OutputStream out) 
            throws 
                IOException,
                BagitMetsValidationException,
                DatatypeConfigurationException,
                UnsupportedEncodingException,
                ParserConfigurationException,
                TransformerException,
                SAXException,
                FileNotFoundException,
                ParseException,
                Exception;
    ArrayList<BagitMetsValidationException> validate(MetsBag metsBag,Mets mets) throws BagitMetsValidationException {               
        BagitMetsValidator validator = new BagitMetsValidator();
        ArrayList<BagitMetsValidationException>warnings = validator.validate(metsBag,mets);                
        return warnings;    
    }
}
