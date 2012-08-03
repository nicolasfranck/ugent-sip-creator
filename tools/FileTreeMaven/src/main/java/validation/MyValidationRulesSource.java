/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package validation;

import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;
import renaming.CleanParams;
import renaming.RenameParams;

/**
 *
 * @author nicolas
 */
public class MyValidationRulesSource extends DefaultRulesSource{
    public MyValidationRulesSource(){

        Rules renamePairRules = new Rules(RenameParams.class);
        renamePairRules
                .add(required("sourcePattern"))
                .add(required("destinationPattern"));
        addRules(renamePairRules);

        Rules cleanParamsRules = new Rules(CleanParams.class);
        addRules(cleanParamsRules);
    }
}
