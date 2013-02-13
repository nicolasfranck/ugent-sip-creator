package ugent.bagger.converters;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.Converter;

/**
 *
 * @author nicolas
 */
public class StringToCharConverter implements Converter{
    @Override
    public Class[] getSourceClasses() {
        return new Class [] {
            String.class
        };        
    }
    @Override
    public Class[] getTargetClasses() {
        return new Class [] {
            Character.class
        };
    }
    @Override
    public Object convert(Object source,Class targetClass,ConversionContext context) throws ConversionException {                 
        String s = (String)source;
        Character c = s != null && !s.isEmpty() ? s.charAt(0) : new Character((char)0);        
        return c;
    }
    
}
