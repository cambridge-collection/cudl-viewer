package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Lei
 * 
 */
public class FileReader {

	public List<File> listFiles(final File folder) {
		return (List<File>) FileUtils.listFiles(folder, null, true);
	}

	public String read(String path, Charset encoding) {
		String content = "";
		try {
			byte[] encoded = com.google.common.io.Files.toByteArray(new File(path));
			content = new String(encoded, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public void save(String path, String content) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
