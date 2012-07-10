/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author nicolas
 */
public class FileNodeResult {
    private int numFiles = 0;
    private int numDirectories = 0;
    private DefaultMutableTreeNode node = null;

    public DefaultMutableTreeNode getNode() {
        return node;
    }

    public void setNode(DefaultMutableTreeNode node) {
        this.node = node;
    }

    public int getNumDirectories() {
        return numDirectories;
    }

    public void setNumDirectories(int numDirectories) {
        this.numDirectories = numDirectories;
    }

    public int getNumFiles() {
        return numFiles;
    }

    public void setNumFiles(int numFiles) {
        this.numFiles = numFiles;
    }
    
}
