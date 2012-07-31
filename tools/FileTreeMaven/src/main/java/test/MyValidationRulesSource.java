/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;

/**
 *
 * @author nicolas
 */
public class MyValidationRulesSource extends DefaultRulesSource{
    public MyValidationRulesSource(){
        
        Rules customerRules = new Rules(Human.class);
        customerRules
                .add(required("name"))
                .add(required("firstName"))
                .add(required("address.street"))
                .add(required("address.streetNr"))
                .add(required("address.postalCode"))
                .add(required("address.place"))
                .add(required("address.country"))
                ;

        addRules(customerRules);
/*
        Rules addressRules = new Rules(Address.class);
        addressRules
                .add(required("street"))
                .add(required("streetNr"))
                .add(required("postalCode"))
                .add(required("place"))
                .add(required("country"));
        addRules(addressRules);
 * 
 */
    }
}
