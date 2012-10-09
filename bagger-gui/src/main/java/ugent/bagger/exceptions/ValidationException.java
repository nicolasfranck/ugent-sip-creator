/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.exceptions;

/**
 *
 * @author nicolas
 */
public class ValidationException extends Exception {
    private String namespace;
    public ValidationException() {
        super();
    }
    public ValidationException(String message) {
        super(message);
    }    
    public ValidationException(String message,String namespace){
        super(message);
        setNamespace(namespace);                
    }
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }    
}
