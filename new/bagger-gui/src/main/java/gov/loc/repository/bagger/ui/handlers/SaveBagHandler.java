package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.Progress;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;

public class SaveBagHandler extends AbstractAction implements Progress {
    private static final Log log = LogFactory.getLog(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;

    public SaveBagHandler() {
        super();        
    }
    //wordt uitgevoerd indien men op de toolbarbutton drukt
    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultBag bag = BagView.getInstance().getBag();
        BagView.getInstance().infoInputPane.updateBagHandler.updateBag(bag);
        if(BagView.getInstance().getBagRootPath().exists()) {
            tmpRootPath = BagView.getInstance().getBagRootPath();
            confirmWriteBag();
        }else {
            saveBag(BagView.getInstance().getBagRootPath());
        }
    }


    @Override
    public void execute() {
        DefaultBag bag = BagView.getInstance().getBag();

        Writer bagWriter = null;
        try {
            BagFactory bagFactory = new BagFactory();
            short mode = bag.getSerialMode();
            if (mode == DefaultBag.NO_MODE) {
                bagWriter = new FileSystemWriter(bagFactory);
            } else if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                bagWriter = new ZipWriter(bagFactory);
            } else if (mode == DefaultBag.TAR_MODE) {
                bagWriter = new TarWriter(bagFactory);
            } else if (mode == DefaultBag.TAR_GZ_MODE) {
                bagWriter = new TarGzWriter(bagFactory);
            } else if (mode == DefaultBag.TAR_BZ2_MODE) {
                bagWriter = new TarBz2Writer(bagFactory);
            }
            bagWriter.addProgressListener(BagView.getInstance().task);
            BagView.getInstance().longRunningProcess = bagWriter;
            messages = bag.write(bagWriter);

            if (messages != null && !messages.trim().isEmpty()) {
                BagView.getInstance().showWarningErrorDialog("Warning - bag not saved", "Problem saving bag:\n" + messages);
            } else {
                BagView.getInstance().showWarningErrorDialog("Bag saved", "Bag saved successfully.\n" );
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run(){
                    DefaultBag bag = BagView.getInstance().getBag();
                    if (bag.isSerialized()) {
                        if (clearAfterSaving) {
                            BagView.getInstance().statusBarEnd();
                            BagView.getInstance().clearBagHandler.clearExistingBag();
                            setClearAfterSaving(false);
                        } else {
                            if (bag.isValidateOnSave()) {
                                BagView.getInstance().validateBagHandler.validateBag();
                            }
                            BagView.getInstance().statusBarEnd();
                            File bagFile = bag.getBagFile();
                            log.info("BagView.openExistingBag: " + bagFile);
                            BagView.getInstance().openBagHandler.openExistingBag(bagFile);
                            BagView.getInstance().updateSaveBag();
                        }
                    } else {
                        ApplicationContextUtil.addConsoleMessage(messages);
                        BagView.getInstance().updateManifestPane();
                    }
                }				
            });
        } finally {
            BagView.getInstance().task.done();
            BagView.getInstance().statusBarEnd();
        }
    }

    public void setTmpRootPath(File f) {
        this.tmpRootPath = f;
    }

    public File getTmpRootPath() {
        return this.tmpRootPath;
    }


    public void setClearAfterSaving(boolean b) {
        this.clearAfterSaving = b;
    }

    public boolean getClearAfterSaving() {
        return this.clearAfterSaving;
    }

    public void saveBag(File file) {
    	DefaultBag bag = BagView.getInstance().getBag();
        bag.setRootDir(file);
        //statusBarBegin aanvaardt als 1ste parameter de interface
        //'Progress' die een methode 'execute' bevat..
        BagView.getInstance().statusBarBegin(this, "Writing bag...", null);
    }

    public void confirmWriteBag() {
        ConfirmationDialog dialog = new ConfirmationDialog() {
            boolean isCancel = true;
            @Override
            protected void onConfirm() {
                DefaultBag bag = BagView.getInstance().getBag();
                if (bag.getSize() > DefaultBag.MAX_SIZE) {
                    confirmAcceptBagSize();
                } else {
                    BagView.getInstance().setBagRootPath(tmpRootPath);
                    saveBag(BagView.getInstance().getBagRootPath());
                }
            }
            @Override
            protected void onCancel(){
                super.onCancel();
                if (isCancel) {
                    cancelWriteBag();
                    isCancel = false;
                }
            }
        };

        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(BagView.getInstance().getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(BagView.getInstance().getPropertyMessage("bag.dialog.message.create"));
        dialog.showDialog();
    }

    private void cancelWriteBag() {
    	clearAfterSaving = false;
    }

    public void confirmAcceptBagSize() {
        ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            protected void onConfirm() {
                BagView.getInstance().setBagRootPath(tmpRootPath);
                saveBag(BagView.getInstance().getBagRootPath());
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(BagView.getInstance().getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(BagView.getInstance().getPropertyMessage("bag.dialog.message.accept"));
        dialog.showDialog();
    }

    public void saveBagAs() {
    	DefaultBag bag = BagView.getInstance().getBag();
        File selectFile = new File(File.separator+".");
        JFrame frame = new JFrame();
        JFileChooser fs = new JFileChooser(selectFile);
    	fs.setDialogType(JFileChooser.SAVE_DIALOG);
    	fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	fs.addChoosableFileFilter(BagView.getInstance().infoInputPane.noFilter);
    	fs.addChoosableFileFilter(BagView.getInstance().infoInputPane.zipFilter);
        fs.addChoosableFileFilter(BagView.getInstance().infoInputPane.tarFilter);
        fs.setDialogTitle("Save Bag As");
    	fs.setCurrentDirectory(bag.getRootDir());
    	if (bag.getName() != null && !bag.getName().equalsIgnoreCase(BagView.getInstance().getPropertyMessage("bag.label.noname"))) {
            String selectedName = bag.getName();
            if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                selectedName += "."+DefaultBag.ZIP_LABEL;
                fs.setFileFilter(BagView.getInstance().infoInputPane.zipFilter);
            }
            else if (bag.getSerialMode() == DefaultBag.TAR_MODE) {
                selectedName += "."+DefaultBag.TAR_LABEL;
                fs.setFileFilter(BagView.getInstance().infoInputPane.tarFilter);
            }
            else {
                fs.setFileFilter(BagView.getInstance().infoInputPane.noFilter);
            }
            fs.setSelectedFile(new File(selectedName));
    	}
    	int option = fs.showSaveDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fs.getSelectedFile();
            save(file);
        }
    }

    public void save(File file) {
    	DefaultBag bag = BagView.getInstance().getBag();
        if (file == null) file = BagView.getInstance().getBagRootPath();
        bag.setName(file.getName());
        File bagFile = new File(file, bag.getName());
    	if (bagFile.exists()) {
            tmpRootPath = file;
            confirmWriteBag();
    	} else {
            if (bag.getSize() > DefaultBag.MAX_SIZE) {
                tmpRootPath = file;
                confirmAcceptBagSize();
            } else {
                BagView.getInstance().setBagRootPath(file);
                saveBag(BagView.getInstance().getBagRootPath());
            }
    	}
        String fileName = bagFile.getAbsolutePath();
        BagView.getInstance().infoInputPane.setBagName(fileName);
        BagView.getInstance().getControl().invalidate();
    }
}