/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.helper;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class FileConstraint {
    private File file;
    private int maxDepth = 1;

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public int getMaxDepth() {
        return maxDepth;
    }
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
