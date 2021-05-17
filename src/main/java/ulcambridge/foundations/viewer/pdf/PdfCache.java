package ulcambridge.foundations.viewer.pdf;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This stores pdfs that have previously been requested on the file system (location in config)
 * and serves them if available instead of generating them again.
 *
 * This could be replaced with something like memcached.
 *
 * These classes could be moved out to a separate service.
 */
class PdfCache {

    private final String cachePath;

    PdfCache(String cachePath) {
        // create cache dir if not exists
        File dir = new File(cachePath);
        if (!cachePath.trim().equals("") && !dir.exists()){
            dir.mkdirs();
        }
        this.cachePath = cachePath;

    }

    boolean existsInCache(String filename) {
        File file = getCacheFile(filename);
        return file.exists() && file.canRead();
    }

    File getCacheFile(String filename) {
        return new File(this.cachePath+File.separator+filename);
    }

    void streamPdfFromCache(String filename, OutputStream outputStream) throws IOException {
        InputStream inputStream = FileUtils.openInputStream(getCacheFile(filename));
        IOUtils.copy(inputStream,outputStream);
        outputStream.flush();;
        outputStream.close();
    }


}
