package ugent.rename;

/**
 *
 * @author nicolas
 */
public interface Sequence {
    String next();
    String previous();
    void setPadding(int padding);
    int getPadding();    
    int getCounter();
    void setCounter(int i);
    void setStep(int step);
    int getStep();
}
