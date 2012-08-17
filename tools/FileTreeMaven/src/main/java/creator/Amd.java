/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package creator;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class Amd {
    private ArrayList<File> digiprovMDFile = new ArrayList<File>();
    private ArrayList<File> sourceMDFile = new ArrayList<File>();
    private ArrayList<File> rightsMDFile = new ArrayList<File>();
    private ArrayList<File> techMDFile = new ArrayList<File>();

    public ArrayList<File> getDigiprovMDFile() {
        return digiprovMDFile;
    }

    public void setDigiprovMDFile(ArrayList<File> digiprovMDFile) {
        this.digiprovMDFile = digiprovMDFile;
    }

    public ArrayList<File> getSourceMDFile() {
        return sourceMDFile;
    }

    public void setSourceMDFile(ArrayList<File> sourceMDFile) {
        this.sourceMDFile = sourceMDFile;
    }

    public ArrayList<File> getRightsMDFile() {
        return rightsMDFile;
    }

    public void setRightsMDFile(ArrayList<File> rightsMDFile) {
        this.rightsMDFile = rightsMDFile;
    }

    public ArrayList<File> getTechMDFile() {
        return techMDFile;
    }

    public void setTechMDFile(ArrayList<File> techMDFile) {
        this.techMDFile = techMDFile;
    }

    
    
}
