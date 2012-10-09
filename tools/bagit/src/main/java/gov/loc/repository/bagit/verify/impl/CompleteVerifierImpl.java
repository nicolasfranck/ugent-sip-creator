package gov.loc.repository.bagit.verify.impl;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.utilities.BagVerifyResult;
import gov.loc.repository.bagit.utilities.FilenameHelper;
import gov.loc.repository.bagit.utilities.IgnoringFileSelector;
import gov.loc.repository.bagit.utilities.LongRunningOperationBase;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.utilities.VFSHelper;
import gov.loc.repository.bagit.verify.CompleteVerifier;
import java.text.MessageFormat;
import static java.text.MessageFormat.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.UriParser;

public class CompleteVerifierImpl extends LongRunningOperationBase implements CompleteVerifier {

	private static final Log log = LogFactory.getLog(CompleteVerifierImpl.class);
	
	private boolean missingBagItTolerant = false;
	private boolean additionalDirectoriesInBagDirTolerant = false;	
	private List<String> ignoreAdditionalDirectories = new ArrayList<String>();
	private boolean ignoreSymlinks = false;
	
	@Override
	public void setIgnoreSymlinks(boolean ignore) {
		this.ignoreSymlinks = ignore;
	}
	
	@Override
	public void setMissingBagItTolerant(boolean missingBagItTolerant) {
		this.missingBagItTolerant = missingBagItTolerant;
	}
	
	@Override
	public void setAdditionalDirectoriesInBagDirTolerant(
			boolean additionalDirectoriesInBagDirTolerant) {
		this.additionalDirectoriesInBagDirTolerant = additionalDirectoriesInBagDirTolerant;
		
	}
	
	@Override
	public void setIgnoreAdditionalDirectories(List<String> dirs) {
		this.ignoreAdditionalDirectories = dirs;
	}
	
	@Override
	public SimpleResult verify(Bag bag) {
		BagVerifyResult result = new BagVerifyResult(true);
		try
		{
			//Is at least one payload manifest
			log.debug("Checking that at least one payload manifest");
			if (bag.getPayloadManifests().isEmpty()) {
				result.setSuccess(false);
				result.addMessage("Bag does not have any payload manifests.");
			}
			//Has bagit file
			log.debug("Checking that has BagIt.txt");
			if (! this.missingBagItTolerant && bag.getBagItTxt() == null) {
				result.setSuccess(false);
				result.addMessage(MessageFormat.format("Bag does not have {0}.", bag.getBagConstants().getBagItTxt()));				
			}
			
			//Bagit is right version
			log.debug("Checking that BagIt.txt is right version");
			if (! this.missingBagItTolerant && bag.getBagItTxt() != null && ! bag.getBagConstants().getVersion().versionString.equals(bag.getBagItTxt().getVersion())) {
				result.setSuccess(false);
				result.addMessage(MessageFormat.format("Version is not {0}.", bag.getBagConstants().getVersion()));				
			}

			if (this.isCancelled()) return null;
			
			//All payload files are in data directory
			log.debug("Checking that all payload files in data directory");
			int total = bag.getPayload().size();
			int count = 0;
			for(BagFile bagFile : bag.getPayload()) {
				if (this.isCancelled()) return null;
				String filepath = bagFile.getFilepath();
				count++;
				this.progress("verifying payload file in data directory", filepath, count, total);
				log.trace(MessageFormat.format("Verifying payload {0} in data directory", filepath));
				if (! filepath.startsWith(bag.getBagConstants().getDataDirectory() + '/')) {
					result.setSuccess(false);
					result.addMessage(MessageFormat.format("Payload file {0} not in the {1} directory.", filepath, bag.getBagConstants().getDataDirectory()));
					log.warn(MessageFormat.format("Payload file {0} not in data directory", filepath));
				}
			}
			
			// Ensure no tag files are listed in the payload manifest.
			log.debug("Checking that no tag files are listed in payload manifests.");
			String payloadDirName = bag.getBagConstants().getDataDirectory();
			
			for (Manifest manifest : bag.getPayloadManifests())
			{
				if (this.isCancelled()) return null;

				this.progress("checking payload manifest for tag files", manifest.getFilepath());
				
				for (String path : manifest.keySet())
				{
					String normalizedPath = FilenameHelper.normalizePath(path);
					log.trace(format("Normalized path: {0} -> {1}", path, normalizedPath));
					
					if (!normalizedPath.startsWith(payloadDirName))
					{
						result.setSuccess(false);
						result.addMessage(format("Tag file is listed in payload manifest {0}: {1}", manifest.getFilepath(), path));
					}
				}
			}
			
			//Every payload BagFile in at least one manifest
			log.debug("Checking that every payload file in at least one manifest");
			total = bag.getPayload().size();
			log.trace(MessageFormat.format("{0} payload files to check", total));
			count = 0;
			for(BagFile bagFile : bag.getPayload()) {
				String filepath = bagFile.getFilepath();
				count++;
				this.progress("verifying payload file in at least one manifest", filepath, count, total);
				log.trace(MessageFormat.format("Verifying payload file {0} in at least one manifest", filepath));
				boolean inManifest = false;
				for(Manifest manifest : bag.getPayloadManifests()) {
					if (this.isCancelled()) return null;
					if (manifest.containsKey(filepath)) {
						inManifest = true;
						break;
					}
				}
				if (! inManifest) {
					result.setSuccess(false);
					result.addMessage(MessageFormat.format("Payload file {0} not found in any payload manifest.", filepath));														
					log.warn(MessageFormat.format("Payload file {0} not found in any payload manifest.", filepath));
				}
			}
			
			//Every payload file exists
			log.debug("Checking that every payload file exists");
			total = bag.getPayloadManifests().size();
			log.trace(MessageFormat.format("{0} payload manifests to check", total));
			count = 0;
			for(Manifest manifest : bag.getPayloadManifests()) {			
				count++;
				this.progress("verifying payload files in manifest exist", manifest.getFilepath(), count, total);
				this.checkManifest(manifest, bag, result);
				if (this.isCancelled()) return null;
			}

			//Every tag file exists
			log.debug("Checking that every tag file exists");
			total = bag.getTagManifests().size();
			log.trace(MessageFormat.format("{0} tag manifests to check", total));
			count = 0;
			for(Manifest manifest : bag.getTagManifests()) {
				count++;
				this.progress("verifying tag files in manifest exist", manifest.getFilepath(), count, total);
				this.checkManifest(manifest, bag, result);
				if (this.isCancelled()) return null;
			}
			
			//Additional checks if an existing Bag
			if (bag.getFile() != null) {
				FileObject bagFileObject = VFSHelper.getFileObjectForBag(bag.getFile());
				//Only directory is a data directory
				log.debug("Checking that only directory is data directory");
				if (! this.additionalDirectoriesInBagDirTolerant) {
					for(FileObject fileObject : bagFileObject.getChildren())
					{
						if (this.isCancelled()) return null;
						if (fileObject.getType() == FileType.FOLDER) {
							String folderName = bagFileObject.getName().getRelativeName(fileObject.getName());
							if (! (folderName.equals(bag.getBagConstants().getDataDirectory()) || this.ignoreAdditionalDirectories.contains(folderName))) {
								result.setSuccess(false);
								result.addMessage(MessageFormat.format("Directory {0} not allowed in bag_dir.", folderName));
							}
						}
					}
				}
				
				//If there is a bagFileObject, all payload FileObjects have payload BagFiles
				log.debug("Checking that all payload files on disk included in bag");
				FileObject dataFileObject = bagFileObject.getChild(bag.getBagConstants().getDataDirectory());
				if (dataFileObject != null) {
					FileObject[] fileObjects = dataFileObject.findFiles(new IgnoringFileSelector(this.ignoreAdditionalDirectories, this.ignoreSymlinks));
					total = fileObjects.length;
					count = 0;
					for(FileObject fileObject : fileObjects) {
						if (this.isCancelled()) return null;
						if (fileObject.getType() == FileType.FILE) {
							String filepath = UriParser.decode(bagFileObject.getName().getRelativeName(fileObject.getName()));
							count++;
							this.progress("verifying payload files on disk are in bag", filepath, count, total);
							log.trace(MessageFormat.format("Checking that payload file {0} is in bag", filepath));
							if (bag.getBagFile(filepath) == null) {
								result.setSuccess(false);
								String msg = MessageFormat.format("Bag has file {0} not found in manifest file.", filepath);
								result.addMessage(msg);
								log.warn(msg);
							}
						}
					}
				}				
			} else {
				log.debug("Not an existing bag");
			}
		}
		catch(FileSystemException ex) {
			throw new RuntimeException(ex);
		}
		log.info("Completed");
		log.info("Result is: " + result.toString());
		return result;

	}
	
	protected void checkManifest(Manifest manifest, Bag bag, BagVerifyResult result) {
		log.trace("Checking manifest " + manifest.getFilepath());
		int manifestTotal = manifest.keySet().size();
		int manifestCount = 0;
		for(String filepath : manifest.keySet()) {
			if (this.isCancelled()) return;
			manifestCount++;
			this.progress("verifying files in manifest exist", filepath, manifestCount, manifestTotal);
			log.trace(MessageFormat.format("Checking that file {0} in manifest {1} exists", filepath, manifest.getFilepath()));
			BagFile bagFile = bag.getBagFile(filepath);					
			if (bagFile == null || ! bagFile.exists())
			{
				result.setSuccess(false);
				result.addMissingOrInvalidFile(filepath);
				String message = MessageFormat.format("File {0} in manifest {1} missing from bag.", filepath, manifest.getFilepath());
				log.warn(message);
				result.addMessage(message);
			}
		}				
		
	}
	
}
