package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum PreSort {
    NO_SORT("NO_SORT"),FILE_NAME("FILE_NAME"),FILE_DATE_MODIFIED("FILE_DATE_MODIFIED");
    String c;
    PreSort(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        String translated = null;
        try{
            translated = Context.getMessage("PreSort."+c);
        }catch(Exception e){}        
        return translated != null ? translated : c;
    }
}