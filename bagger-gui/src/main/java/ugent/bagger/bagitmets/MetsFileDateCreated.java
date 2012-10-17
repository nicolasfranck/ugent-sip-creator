package ugent.bagger.bagitmets;
import ugent.bagger.helper.Context;
/**
 *
 * @author nicolas
 */
public enum MetsFileDateCreated {
    CURRENT_DATE("CURRENT_DATE"),LAST_MODIFIED("LAST_MODIFIED");
    private String c;
    private MetsFileDateCreated(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        String translated = Context.getMessage("MetsFileDateCreated."+c);
        return (translated != null && !translated.isEmpty()) ? translated : c;
    }
}
