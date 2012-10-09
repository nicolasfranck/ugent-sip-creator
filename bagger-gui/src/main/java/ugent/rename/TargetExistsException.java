/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.rename;

/**
 *
 * @author nicolas
 */
public class TargetExistsException extends Exception{
    public TargetExistsException(String message){
        super(message);
    }
}
