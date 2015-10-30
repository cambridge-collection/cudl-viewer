package ulcambridge.foundations.viewer.admin;

import java.util.List;

public class BrowseFile implements Comparable<Object> {

	private String filename;
	private String filePath;
	private String fileURL;
	private FileType fileType;
	private List<BrowseFile> children;

	public BrowseFile(String filename, String filePath, String fileURL, boolean isDirectory, List<BrowseFile> children) {
		this.filename = filename;
		this.filePath = filePath;
		this.fileURL = fileURL;
		this.fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
		this.children = children;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public String getFileURL() {
		return fileURL;
	}

	public boolean isDirectory() {
		return fileType == FileType.DIRECTORY;
	}

	public FileType getType() {
		assert fileType != null;
		return fileType;
	}

	public List<BrowseFile> getChildren() {
		return children;
	}

	@Override
	public int compareTo(Object o) {
		
		if (o.getClass() != BrowseFile.class) {
			return 0;
		}
			    
		BrowseFile otherFile = (BrowseFile)o;
		
		// always order directories first
		if (isDirectory()) { 
		   if (!otherFile.isDirectory()) {	return -1;  } 
		} else {
		   if (otherFile.isDirectory()) {	return 1;  }
		}
		
		return getFilename().compareTo(((BrowseFile)o).getFilename());
	}

	public enum FileType {
		FILE, DIRECTORY
	}
}
