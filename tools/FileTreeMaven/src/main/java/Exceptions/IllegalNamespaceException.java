/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author nicolas
 */
public class IllegalNamespaceException extends Exception {
    private String namespace;
    public IllegalNamespaceException() {
        super();
    }
    public IllegalNamespaceException(String message) {
        super(message);
    }    
    public IllegalNamespaceException(String message,String namespace){
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
