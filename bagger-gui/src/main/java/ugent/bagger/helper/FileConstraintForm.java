/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.helper;

import javax.swing.JComponent;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 *
 * @author nicolas
 */
public class FileConstraintForm extends AbstractForm{
    public FileConstraintForm(FileConstraint fileConstraint){
        super(fileConstraint);
        setId("fileConstraint.edit");
    }
    @Override
    protected JComponent createFormControl() {
        logger.info("entering createFormControl");
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        builder.add("file");
        builder.row();
        builder.add("maxDepth");
        logger.info("end createFormControl");
        return builder.getForm();
    }

}