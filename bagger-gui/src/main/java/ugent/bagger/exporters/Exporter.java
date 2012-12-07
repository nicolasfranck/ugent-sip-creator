package ugent.bagger.exporters;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
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
    abstract ArrayList<MdSec>getMetadata(Mets mets);
    public abstract void export(File dir,OutputStream out) 
            throws 
                IOException,
                BagitMetsValidationException,
                DatatypeConfigurationException,
                UnsupportedEncodingException,
                ParserConfigurationException,
                TransformerException,
                SAXException,
                FileNotFoundException,
                ParseException;
    ArrayList<BagitMetsValidationException> validate(File file) throws BagitMetsValidationException {
        ArrayList<BagitMetsValidationException>warnings = new ArrayList<BagitMetsValidationException>();
        
        BagitMetsValidator validator = new BagitMetsValidator();
        validator.validate(file);
                
        return warnings;    
    }
}
