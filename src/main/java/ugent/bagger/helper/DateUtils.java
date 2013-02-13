package ugent.bagger.helper;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author nicolas
 */
public class DateUtils {
    public static XMLGregorianCalendar DateToGregorianCalender() throws DatatypeConfigurationException{
        return DateToGregorianCalender(new Date());
    }
    public static XMLGregorianCalendar DateToGregorianCalender(Date date) throws DatatypeConfigurationException{
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);        
    }
}
