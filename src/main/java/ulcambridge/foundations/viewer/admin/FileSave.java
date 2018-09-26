package ulcambridge.foundations.viewer.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileSave {

    private static final Logger LOG = LoggerFactory.getLogger(FileSave.class);

    /**
     * Saves specified InputStream to the filename and directory path specified.
     * Also rolls back if any of this process fails.
     *
     * Note: Does not validate params. This should occur before calling this
     * method.
     */
    public static synchronized boolean save(String dirPath, String filename, InputStream is) throws IOException {

        // Setup variables
        File newFile = new File(dirPath, filename + ".new_cudl_viewer_version").getCanonicalFile();
        File parentDir = new File(newFile.getParent()).getCanonicalFile();
        File existingParentDir = existingParent(parentDir);
        File oldFile = new File(dirPath, filename + ".old_cudl_viewer_version").getCanonicalFile();
        File file = new File(dirPath, filename).getCanonicalFile();

        Deque<Callable> rollback = new LinkedList<>();

        try {
            // Task 1 - Create directory (if needed).
            Files.createDirectories(parentDir.toPath());
            rollback.addFirst(() -> deleteAllDirectoriesUpTo(parentDir, existingParentDir));

            // Task 2 - Write file to file system.
            Files.copy(is, newFile.toPath());
            rollback.addFirst(() -> Files.deleteIfExists(newFile.toPath()));

            // Task 3 - Copy existing file (if available) for rollback.
            if (file.exists()) {
                Files.copy(file.toPath(), oldFile.toPath());
            }
            rollback.addFirst(() -> Files.deleteIfExists(oldFile.toPath()));

            // Task 4 - rename file (overwriting if exists already).
            Files.move(newFile.toPath(), file.toPath(), REPLACE_EXISTING, ATOMIC_MOVE);

            // Clean up.
            Files.deleteIfExists(oldFile.toPath());

            // Write response out in JSON.
            return true;

        } catch (IOException | RuntimeException ex) {
            try {
                for (Callable callable : rollback) {
                    callable.call();
                }
            } catch (Exception ex2) {
                ex.addSuppressed(ex2);
            }

            LOG.error("Failed to save file {}", filename, ex);
            return false;
        }

    }

    /**
     * Gets lowest level directory that already exists on file system.
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
     */
    private static synchronized boolean deleteAllDirectoriesUpTo(File childDir, File parentDir)
            throws IOException {

        childDir = childDir.getCanonicalFile();
        parentDir = parentDir.getCanonicalFile();

        if (!childDir.exists() || !parentDir.exists() || !fileIsParent(childDir, parentDir)) {
            return false;
        }

        while (!childDir.equals(parentDir)) {

            // delete dir if empty.
            if (childDir.isDirectory() && childDir.list().length == 0) {
                Files.delete(childDir.toPath());
                childDir = childDir.getParentFile();
            } else {
                return false;
            }
        }

        return true;
    }

    private static synchronized boolean fileIsParent(File child, File parent) {
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
