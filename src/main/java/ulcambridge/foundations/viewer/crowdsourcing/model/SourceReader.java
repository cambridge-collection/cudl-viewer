package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.JsonObject;

/**
 *
 * @author Lei
 *
 */
public class SourceReader {

    private final String metaPath;

    private final String fragPath;

    public SourceReader(String metaPath, String fragPath) {
        this.metaPath = metaPath;
        this.fragPath = fragPath;
    }

    public List<File> listMetadata() {
        File f = new File(this.metaPath);

        if (!f.exists())
            return null;

        return new FileReader().listFiles(f);
    }

    public List<File> listFragments() {
        File f = new File(this.fragPath);

        if (!f.exists())
            return null;

        return new FileReader().listFiles(f);
    }

    /**
     * combine metadataXml with extraXml
     *
     * @param metaXml
     * @param extraXml
     * @return
     */
    public String combineXMLs(String metaXml, String extraXml) {
        // get doctype from metadata
        String docType = getDOCTYPE(metaXml);

        // remove xml declaration from metadata
        metaXml = metaXml.replaceAll("(<\\?xml(.+?)\\?>)|(<\\!DOCTYPE((.|\\n|\\r)*?)\\]>)", "").trim();

        // remove xml declaration from extra
        extraXml = extraXml.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();

        return new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(docType).append("<combined><metadata>").append(metaXml)
                .append("</metadata><extra>").append(extraXml).append("</extra></combined>").toString();
    }

    public String getDOCTYPE(String xml) {
        String docType = "";
        Pattern p = Pattern.compile("<\\!DOCTYPE((.|\\n|\\r)*?)\\]>");
        Matcher m = p.matcher(xml);
        while (m.find()) {
            docType = m.group(0);
        }
        return docType;
    }

    /**
     * Parse text mining keywords
     *
     * @param documentName
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    //public List<Tag> extractKeywords(String documentName) throws ParserConfigurationException, SAXException, IOException {
    public List<Tag> extractKeywords(String fragPath) throws ParserConfigurationException, SAXException, IOException {
        File f = new File(fragPath);

        if (!f.exists())
            return null;

        List<Tag> tags = new ArrayList<Tag>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(f);

        // optional, but recommended
        // @see http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        //
        doc.getDocumentElement().normalize();

        NodeList keywordsList = doc.getElementsByTagName("KEYWORDS");
        Node keywords = keywordsList.getLength() > 0 ? keywordsList.item(0) : null;
        if (keywords != null) {
            NodeList keywordList = keywords.getChildNodes();

            for (int i = 0; i < keywordList.getLength(); i++) {
                Node kw = keywordList.item(i);
                if (kw.getNodeType() == Node.ELEMENT_NODE) {
                    Element kwElem = (Element) kw;

                    String name = kwElem.getAttribute("name");
                    String row = kwElem.getAttribute("raw");
                    String value = kwElem.getAttribute("value");

                    JsonObject json = new JsonObject();
                    json.addProperty("name", name);
                    json.addProperty("raw", row);
                    json.addProperty("value", value);

                    Tag tag = new JSONConverter().toTag(json);
                    tags.add(tag);
                }
            }
        }

        return tags;
    }

}
