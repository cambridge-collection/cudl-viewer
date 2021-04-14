package ulcambridge.foundations.viewer.testing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLTesting {
    private XMLTesting() {}

    public static Document getTestXml(Object context, String path) {
        Class<?> contextClass = context instanceof Class ? (Class<?>)context : context.getClass();
        InputStream s = contextClass.getResourceAsStream(path);
        if(s == null)
            throw new IllegalArgumentException(String.format(
                "Failed to load test XML document: Resource file not found at path: \"%s\", "
                    + "context: %s", path, contextClass));
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(s);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(String.format(
                "Failed to load test XML document at path: \"%s\", "
                    + "context: %s: %s", path, contextClass, e.getMessage()), e);
        }
    }

    public static String normaliseSpace(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }
}
