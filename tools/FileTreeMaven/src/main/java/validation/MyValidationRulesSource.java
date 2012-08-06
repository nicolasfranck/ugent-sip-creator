/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package validation;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.support.DefaultRulesSource;
import renaming.CleanParams;
import renaming.AdvancedRenameParams;
import renaming.SimpleRenameParams;

/**
 *
 * @author nicolas
 */
public class MyValidationRulesSource extends DefaultRulesSource{
    public MyValidationRulesSource(){

        Rules advancedRenameParamsRules = new Rules(AdvancedRenameParams.class);
        advancedRenameParamsRules
                .add(required("sourcePattern"))
                .add(required("destinationPattern"));
        addRules(advancedRenameParamsRules);

        Rules cleanParamsRules = new Rules(CleanParams.class);
        addRules(cleanParamsRules);
       
    }
}
