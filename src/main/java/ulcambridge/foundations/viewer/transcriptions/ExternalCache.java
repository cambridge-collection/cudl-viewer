package ulcambridge.foundations.viewer.transcriptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Hashtable;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.codec.binary.Hex;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import ulcambridge.foundations.viewer.model.Properties;

public class ExternalCache {

    private static final String cachePath = Properties.getString("cachePath");
    private static final String cacheURL = Properties.getString("cacheURL");

    // holds ref to files that have been requested recently
    private static Hashtable<String, Date> cachedFiles = new Hashtable<String, Date>();

    private static final long cacheCheckTimeout = Long.parseLong(Properties
            .getString("cacheCheckTimeout").trim());
    private static final long cacheFileTimeout = Long.parseLong(Properties
            .getString("cacheFileTimeout").trim());
    private static final int externalConnectionTimeout = Integer
            .parseInt(Properties.getString("externalConnectionTimeout").trim());

    public static boolean existsInCache(String url, String docId) {

        // look for corresponding file in memory (from last check on disk).
        String filename = getCachedItemFilename(url);
        if (cachedFiles.containsKey(docId + filename)) {
            Date dateFileChecked = cachedFiles.get(docId + filename);

            if (dateFileChecked.getTime() + cacheCheckTimeout > (new Date())
                    .getTime()) {
                // file is in hashtable, so recently been found on file system.
                return true;
            } else {
                // file is timed out in hashtable, checking disk
                cachedFiles.remove(docId + filename);
            }
        }

        // look for corresponding file on disk
        String filepath = cachePath + File.separator + docId + File.separator
                + filename;
        File file = new File(filepath);
        if (file.exists()) {

            // Check if the file is older than the allowed File timeout.
            if (file.lastModified() + cacheFileTimeout < (new Date()).getTime()) {

                // File which has timed out is considered not in the cache
                // to prompt it to be reloaded.
                return false;
            }

            cachedFiles.put(filename, new Date());
            return true;
        }

        return false;
    }

    // This returns the local url to pull the item from the cache.
    // does not guarantee this file exists.
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
        return null;

    }

    // This returns the local url to pull the item from the cache.
    // does not guarantee this url exists
    public static String getCachedItemLocalURL(String url, String docId) {

        String filename = getCachedItemFilename(url);
        if (filename != null) {
            return cacheURL + docId + "/" + filename;
        }
        return url; // default to original url.
    }

    public static void loadIntoCache(String url, String docId) {

        URL site;
        try {
            site = new URL(url);
            URLConnection uc = site.openConnection();
            uc.setConnectTimeout(externalConnectionTimeout);

            if (uc instanceof HttpURLConnection) {
                HttpURLConnection connection = (HttpURLConnection) uc;
                if (connection.getResponseCode() == 200) {
                    // URL exists and returns ok (200)
                    if (connection.getContentType().toLowerCase()
                            .startsWith("text/html")) {

                        loadPageIntoCache(url, docId);

                    } else if (connection.getContentType().toLowerCase()
                            .startsWith("text/css")) {

                        loadCSSIntoCache(url, docId);

                    } else {

                        loadResourceIntoCache(url, docId);
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
    private static void loadPageIntoCache(String url, String docId) {

        // request URL
        try {
            // System.out.println("loading PAGE into cache: " + url);

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
            tidy.setXHTML(true);
            tidy.setOutputEncoding("UTF-8");

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
            String attributeName, Document dom, String baseURL, String docId)
            throws MalformedURLException {

        NodeList css = dom.getElementsByTagName(elementName);
        for (int i = 0; i < css.getLength(); i++) {
            Node cssFile = css.item(i);
            Node href = cssFile.getAttributes().getNamedItem(attributeName);
            if (href != null) {
                String linkSRC = href.getNodeValue();
                if (!linkSRC.startsWith("http")) {
                    // make any relative links absolute
                    linkSRC = new URL(new URL(baseURL), linkSRC).toString();
                }
                // rewrite link to local resource
                href.setNodeValue(getCachedItemFilename(linkSRC));
                if (!existsInCache(linkSRC, docId)) {
                    loadIntoCache(linkSRC, docId);
                }
            }
        }
        return dom;
    }

    private static void loadCSSIntoCache(String url,
            String docId) {
        //System.out.println("loading into cache: " + url);

        try {
            String filename = getCachedItemFilename(url);

            String filepath = cachePath + File.separator + docId
                    + File.separator + filename;
            File file = new File(filepath);
            file.getParentFile().mkdirs();

            if (!url.startsWith("http")) {
                return;
            }
            URL site = new URL(url);

            URLConnection uc = site.openConnection();

            BufferedReader is = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));

            String strLine;
            while ((strLine = is.readLine()) != null) {

                if (strLine.contains("url(") && !strLine.contains("url(data")) {
                    // replace url(/external/url) with url(local/cached/url)
                    int start = strLine.indexOf("url(");
                    int end = strLine.indexOf(')', start);
                    String urlExtRef = strLine.substring(start + 4, end);
                    if (!urlExtRef.startsWith("http")
                            && !urlExtRef.startsWith("/")) {
                        urlExtRef = url.substring(0, url.lastIndexOf("/") + 1)
                                + urlExtRef;
                    } else if (!urlExtRef.startsWith("http")
                            && urlExtRef.startsWith("/")) {
                        urlExtRef = url.substring(0, url.indexOf("/",8) + 1)
                        + urlExtRef;
                    }
                    //System.out.println("CSS URL: " + urlExtRef);

                    out.write(strLine.substring(0, start));
                    out.write("url(" + getCachedItemFilename(urlExtRef) + ")");
                    out.write(strLine.substring(end+1) + "\n");

                    if (!existsInCache(urlExtRef,docId)) {
                        loadIntoCache(urlExtRef, docId);
                    }

                } else {

                    out.write(strLine + "\n");
                }
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

    private static void loadResourceIntoCache(String url, String docId) {

        // System.out.println("loading into cache: " + url);

        try {
            String filename = getCachedItemFilename(url);

            String filepath = cachePath + File.separator + docId
                    + File.separator + filename;
            File file = new File(filepath);
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
}
