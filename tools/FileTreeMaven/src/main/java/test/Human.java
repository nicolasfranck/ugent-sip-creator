/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author nicolas
 */
public class Human {
    private String name;
    private String firstName;
    private Address address;

    public Human(String name,String firstName){
        setName(name);
        setFirstName(firstName);
        setAddress(new Address());
    }
    public Human(Human h){
        setName(h.getName());
        setFirstName(h.getFirstName());
        setAddress(h.getAddress());
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        System.out.println("setFirstName:"+firstName);
        this.firstName = firstName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        System.out.println("setName:"+name);
        this.name = name;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
}
