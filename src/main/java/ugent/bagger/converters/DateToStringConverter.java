package ugent.bagger.converters;

import java.text.DateFormat;
import java.util.Date;
import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.Converter;

/**
 *
 * @author nicolas
 */
public class DateToStringConverter implements Converter{
    static DateFormat dateFormatter = DateFormat.getInstance();
    @Override
    public Class[] getSourceClasses() {
        return new Class [] {Date.class};
    }
    @Override
    public Class[] getTargetClasses() {
        return new Class [] {String.class};
    }
    @Override
    public Object convert(Object source, Class targetClass, ConversionContext context) throws ConversionException {
        return dateFormatter.format((Date)source);        
    }    
}