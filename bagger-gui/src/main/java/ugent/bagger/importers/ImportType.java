package ugent.bagger.importers;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum ImportType {
    CSV("csv"),BAG_INFO("bag-info.txt");
    private String c;
    private ImportType(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        String out = c;
        try{
            String formatted = Context.getMessage(c);
            out = formatted != null ? formatted:c;
        }catch(Exception e){}
        return out; 
    }
}
