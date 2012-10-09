package ugent.bagger.bagitmets;

/**
 *
 * @author nicolas
 */
public enum MetsFileDateCreated {
    CURRENT_DATE("huidige datum"),LAST_MODIFIED("laatst gewijzigd");
    private String c;
    private MetsFileDateCreated(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        return c;
    }
}
