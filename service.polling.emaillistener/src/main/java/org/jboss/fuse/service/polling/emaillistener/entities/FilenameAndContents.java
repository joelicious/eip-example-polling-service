package org.jboss.fuse.service.polling.emaillistener.entities;

/***
 * A simple POJO that is a wrapper around a fileName and
 * fileContents
 * 
 * @author jbutler
 *
 */
public class FilenameAndContents {

	private String fileName;
	private String fileContents;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContents() {
		return fileContents;
	}

	public void setFileContents(String fileContents) {
		this.fileContents = fileContents;
	}

}
