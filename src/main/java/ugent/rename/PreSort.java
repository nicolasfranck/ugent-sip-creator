package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum PreSort {
    NO_SORT("NO_SORT"),
    FILE_NAME_ASC("FILE_NAME_ASC"),
    FILE_NAME_DESC("FILE_NAME_DESC"),
    FILE_DATE_MODIFIED_ASC("FILE_DATE_MODIFIED_ASC"),
    FILE_DATE_MODIFIED_DESC("FILE_DATE_MODIFIED_DESC");
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