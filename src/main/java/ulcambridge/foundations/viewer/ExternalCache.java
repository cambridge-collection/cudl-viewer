package ulcambridge.foundations.viewer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.codec.binary.Hex;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

public class ExternalCache {

	static ResourceBundle config = ResourceBundle.getBundle("cudl-global");
	static final String cachePath = config.getString("cachePath");
	static final String cacheURL = config.getString("cacheURL");
	// holds those pointers to files that have been requested since the last
	// restart
	static Hashtable<String, Date> cachedFiles = new Hashtable<String, Date>();
	// Time since last disk check to trust that the cached file is still there.
	static final long aMinute = 60000;
	static final long anHour = 3600000;
	static final long aDay = 86400000;

	static final long cacheCheckTimeout = aMinute; // in ms
	static final long cacheFileTimeout = aDay * 3; // in ms

	public static boolean existsInCache(String url, String docId) {

		// look for corresponding file in memory (from last check on disk).
		String filename = getCachedItemFilename(url);
		if (cachedFiles.containsKey(filename)) {
			Date dateFileChecked = cachedFiles.get(filename);

			if (dateFileChecked.getTime() + cacheCheckTimeout > (new Date())
					.getTime()) {
				// file is in hashtable, so recently been found on file system.
				return true;
			} else {
				// file is timed out in hashtable, checking disk
				cachedFiles.remove(filename);
			}
		}

		// look for corresponding file on disk
		String filepath = cachePath + File.separator + docId
		+ File.separator + filename;
		File file = new File(filepath);
		System.out.println(filepath + " exists in cache: " + file.exists());
		if (file.exists()) {

			// Check if the file is older than the allowed File timeout.
			if (file.lastModified() + cacheFileTimeout < (new Date()).getTime()) {

				loadIntoCache(url, docId);
			}

			cachedFiles.put(filename, new Date());
			return true;
		}

		return false;
	}

	// This returns the local url to pull the item from the cache.
	public static String getCachedItemFilename(String url) {

		MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");

			digest.update(url.getBytes());
			String filename = new String(Hex.encodeHex(digest.digest()));

			// find out if the url has an extension (.css, .png etc. default to
			// .html)
			String extension = ".html";
			if (url.matches(".*\\.[(a-z)(A-Z)]{2,4}$")) {
				extension = url.substring(url.lastIndexOf('.'));
			}
			return filename + extension;

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; // default to original url

	}

	// This returns the local url to pull the item from the cache.
	public static String getCachedItemLocalURL(String url, String docId) {

		String filename = getCachedItemFilename(url);
		if (filename != null) {
			return cacheURL +  docId + "/" + filename;
		}
		return url; // default to original url.
	}

	public static void loadIntoCache(String url, String docId) {
		URL site;
		try {
			site = new URL(url);
			URLConnection uc = site.openConnection();
			System.out.println("connection type: " + uc.getContentType());
			if (uc instanceof HttpURLConnection) {
				HttpURLConnection connection = (HttpURLConnection) uc;
				if (connection.getResponseCode() == 200) {
					// URL exists and returns ok (200)
					if (connection.getContentType().toLowerCase()
							.startsWith("text/html")) {
						loadPageIntoCache(url, connection, docId);
					} else {
						loadResourceIntoCache(url, connection, docId);
					}
				} else {
					throw new IOException("Problems retrieving url :" + url
							+ " response code: " + connection.getResponseCode());
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This assumes resource URLs used by this page do not have any line breaks
	// in.
	// resources include images (jpg, png, gif) and js/css used by this page
	// resources must be referred to with 'href=' or 'src='
	private static void loadPageIntoCache(String url,
			HttpURLConnection connection, String docId) {

		// request URL
		try {
			System.out.println("loading PAGE into cache: " + url);

			URL site = new URL(url);
			String baseURL = site.getProtocol()
					+ "://"
					+ site.getHost()
					+ ":"
					+ site.getPort()
					+ site.getPath().substring(0,
							site.getPath().lastIndexOf('/'));

			URLConnection uc = site.openConnection();

			Tidy tidy = new Tidy();
			tidy.setQuiet(true);
			tidy.setShowWarnings(false);
			Document tidyDOM = tidy.parseDOM(uc.getInputStream(), null);

			tidyDOM = cacheLinkedResourceAndUpdateRef("img", "src", tidyDOM,
					baseURL, docId);
			tidyDOM = cacheLinkedResourceAndUpdateRef("script", "src", tidyDOM,
					baseURL, docId);
			tidyDOM = cacheLinkedResourceAndUpdateRef("link", "href", tidyDOM,
					baseURL, docId);

			String filename = getCachedItemFilename(url);

			String filepath = cachePath + File.separator + docId
					+ File.separator + filename;
			File file = new File(filepath);
			file.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(file);
			tidy.pprint(tidyDOM, out);
			out.flush();
			out.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// look at any resources used in that content.
		// load them into cache.
		// rewrite the source html to point to our resources
		// Save sourec html under the /mnt/pres

		catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Document cacheLinkedResourceAndUpdateRef(String elementName,
			String attributeName, Document dom, String baseURL, String docId) {

		NodeList css = dom.getElementsByTagName(elementName);
		for (int i = 0; i < css.getLength(); i++) {
			Node cssFile = css.item(i);
			Node href = cssFile.getAttributes().getNamedItem(attributeName);
			if (href != null) {
				String linkSRC = href.getNodeValue();
				if (!linkSRC.startsWith("http")) {
					// make any relative links absolute
					linkSRC = baseURL + "/" + linkSRC;
				}
				// rewrite link to local resource
				href.setNodeValue(getCachedItemFilename(linkSRC));
				loadIntoCache(linkSRC, docId);
			}
		}
		return dom;
	}

	private static void loadResourceIntoCache(String url,
			HttpURLConnection connection, String docId) {

		System.out.println("loading into cache: " + url);

		try {
			String filename = getCachedItemFilename(url);

			String filepath = cachePath + File.separator + docId
			+ File.separator + filename;
			File file = new File (filepath);
			file.getParentFile().mkdirs();
			
			if (!url.startsWith("http")) {
				return;
			}
			URL site = new URL(url);

			URLConnection uc = site.openConnection();

			InputStream is = uc.getInputStream();
			FileOutputStream out = new FileOutputStream(file);

			byte[] buf = new byte[4 * 1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}

			out.flush();
			out.close();
			is.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	private static void printHeaders(URLConnection uc) {

		Map headers = uc.getHeaderFields();
		Iterator keys = headers.keySet().iterator();
		System.out.println("HEADER");
		while (keys.hasNext()) {
			String key = (String) keys.next();
			List<String> values = (List<String>) headers.get(key);
			StringBuffer value = new StringBuffer();
			for (int i = 0; i < values.size(); i++) {
				value.append(values.get(i) + " , ");
			}
			System.out.println(key + ": " + value);
		}
		System.out.println("DONE");
	}
}
