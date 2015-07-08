package ulcambridge.foundations.viewer.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileSave {

	/**
	 * Saves specified InputStream to the filename and directory path specified. 
	 * Also commits to GIT any changes and rolls back if any of this process fails. 
	 *
	 * Note: Does not validate params. This should occur before calling this
	 * method.
	 * 
	 * @param filename
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static synchronized boolean save(String dirPath, String filename, InputStream is) throws IOException {		

		// Setup variables
		boolean task1done = false, task2done = false, task3done = false, task4done = false, task5done = false;
		File newFile = new File(dirPath, filename
				+ ".new_cudl_viewer_version").getCanonicalFile();		
		File parentDir = new File(newFile.getParent()).getCanonicalFile();
		File existingParentDir = existingParent(parentDir);
		File oldFile = new File(dirPath, filename
				+ ".old_cudl_viewer_version").getCanonicalFile();
		File file = new File(dirPath, filename).getCanonicalFile();

		try {

			// Task 1 - Create directory (if needed).
			parentDir.mkdirs();
			task1done = true;

			// Task 2 - Write file to file system.
			Files.copy(is, newFile.toPath());
			task2done = true;

			// Task 3 - Copy existing file (if available) for rollback.
			if (file.exists()) {
				Files.copy(file.toPath(), oldFile.toPath());
			}
			task3done = true;

			// Task 4 - rename file (overwriting if exists already).
			newFile.renameTo(file);
			task4done = true;

			// TODO commit to git 
			task5done = true;
			
			// Clean up.
			if (oldFile.exists()) {
				oldFile.delete();
			}					

			// Write response out in JSON.
			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		} finally {

			if (!task5done) {
				if (task4done) {
					// Rollback task 4
					Files.copy(file.toPath(), newFile.toPath());
					if (oldFile.exists()) {
						oldFile.renameTo(file);
					} else {
						file.delete();
					}
					task4done = false;
				}			
			}
			if (!task4done) {
				if (task3done) {
					// Rollback task 3
					if (oldFile.exists()) {
						oldFile.delete();
					}
					task3done = false;
				}
			}
			if (!task3done) {
				if (task2done) {
					// Rollback task 2
					if (newFile.exists()) {
						newFile.delete();
					} // NB: Possible to lose any .new_cudl_viewer_version File
						// that was overwritten
					task2done = false;
				}
			}
			if (!task2done) {
				if (task1done) {
					// Rollback task 1 - Creating the parent directory(ies).
					deleteAllDirectoriesUpTo(parentDir, existingParentDir);
					task1done = false;
				}
			}
		}
		
	}

	/**
	 * Gets lowest level directory that already exists on file system.
	 * 
	 * @param dir
	 * @return
	 */
	private static synchronized File existingParent(File dir) {
		if (dir.exists()) {
			return dir;
		}

		while (!dir.exists()) {
			dir = dir.getParentFile();
		}

		return dir;
	}

	/**
	 * Deletes all directories in the hierarchy up to parentDir childDir should
	 * be a sub-directory of parentDir. Only deletes empty directories.
	 * 
	 * @param parentDir
	 * @param childDir
	 * @return
	 * @throws IOException
	 */
	private static synchronized boolean deleteAllDirectoriesUpTo(File childDir, File parentDir)
			throws IOException {

		childDir = childDir.getCanonicalFile();
		parentDir = parentDir.getCanonicalFile();

		if (!childDir.exists() || !parentDir.exists()
				|| !fileIsParent(childDir, parentDir)) {
			return false;
		}

		while (!childDir.equals(parentDir)) {

			// delete dir if empty.
			if (childDir.exists() && childDir.isDirectory()
					&& childDir.list().length == 0) {
				childDir.delete();
				childDir = childDir.getParentFile();
			} else {
				return false;
			}
		}

		return true;

	}

	private static synchronized boolean fileIsParent(File child, File parent) throws IOException {
		if (!parent.exists() || !parent.isDirectory()) {
			return false;
		}

		while (child != null) {
			if (child.equals(parent)) {
				return true;
			}
			child = child.getParentFile();
		}

		// No match found
		return false;
	}
}
