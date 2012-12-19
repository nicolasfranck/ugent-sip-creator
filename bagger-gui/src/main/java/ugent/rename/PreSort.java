package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum PreSort {
    FILE_NAME_ASC("FILE_NAME_ASC"),FILE_NAME_DESC("FILE_NAME_DESC");
    private String c;
    private PreSort(String c){
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