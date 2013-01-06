package ugent.rename;

/**
 *
 * @author nicolas
 */
public class DecimalSequence implements Sequence{
    int i = 0;   
    int padding = 0;
    boolean isSet = false;
    int step = 1;
    
    private String getFormatString(){
        String formatString = "%";
        if(padding > 0){
            formatString += "0"+padding;
        }
        formatString += "d";
        return formatString;
    }
    
    @Override
    public String next() {
        if(isSet){
            i += step;            
        }else{
            isSet = true;
        }
        return String.format(getFormatString(),i);        
    }
    @Override
    public String previous(){
        if(isSet){
            i -= step;            
        }else{
            isSet = true;
        }
        return String.format(getFormatString(),i);
    }
    @Override
    public void setPadding(int padding) {
        this.padding = padding;
    }
    @Override
    public int getPadding() {
        return padding;
    }
    public static void main(String [] args){
        Sequence seq = new DecimalSequence();        
        seq.setPadding(10);        
        seq.setCounter(100);
        for(int i = 0;i<1000;i++){
            System.out.println(seq.next());
        }
    }

    @Override
    public int getCounter() {
        return i;
    }

    @Override
    public void setCounter(int i) {
        this.i = i;
    }

    @Override
    public int getStep() {
        return step;
    }

    @Override
    public void setStep(int step) {
        this.step = step;
    }
    
}