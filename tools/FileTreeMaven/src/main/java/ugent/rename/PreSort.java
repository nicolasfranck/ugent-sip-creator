package ugent.rename;

/**
 *
 * @author nicolas
 */
public enum PreSort {
    NONE("no sorting"),FILE_NAME_ASC("file name ascending"),FILE_NAME_DESC("file name descending");
    private String c;
    private PreSort(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        return c;
    }
}