/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.exceptions;

/**
 *
 * @author nicolas
 */
public class NoImporterFoundException extends Exception{
    public NoImporterFoundException() {
        super();
    }
    public NoImporterFoundException(String message) {
        super(message);
    }
}
