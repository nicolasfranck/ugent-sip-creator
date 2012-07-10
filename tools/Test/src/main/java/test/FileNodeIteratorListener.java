/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author nicolas
 */
public interface FileNodeIteratorListener {
    public void call(DefaultMutableTreeNode node,File file);
}
