package ugent.bagger.converters;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.support.DefaultConversionService;
import org.springframework.richclient.application.DefaultConversionServiceFactoryBean;
/**
 *
 * @author nicolas
 */
public class BaggerConversionService extends DefaultConversionServiceFactoryBean{
    @Override
    protected ConversionService createConversionService(){
        DefaultConversionService service = (DefaultConversionService) super.createConversionService();
        service.addConverter(new StringToCharConverter());
        service.addConverter(new CharToStringConverter());
        return service;
    }
}
