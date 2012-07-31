/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package validation;

import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;
import renaming.RenamePair;

/**
 *
 * @author nicolas
 */
public class MyValidationRulesSource extends DefaultRulesSource{
    public MyValidationRulesSource(){        
        Rules renamePairRules = new Rules(RenamePair.class);
        System.out.println("validation.MyValidationRulesSource loaded!");
        renamePairRules
                .add(required("sourcePattern"))
                .add(required("destinationPattern"));
        addRules(renamePairRules);
    }
}
