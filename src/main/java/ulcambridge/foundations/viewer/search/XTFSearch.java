package ulcambridge.foundations.viewer.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Properties;
import ulcambridge.foundations.viewer.utils.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class XTFSearch implements Search {

    private static final Log LOG = LogFactory.getLog(XTFSearch.class.getName());



    private static final String INDEX_NAME_REGULAR = "index-cudl",
                                INDEX_NAME_VARIABLE_RECALL = "index-cudl-tagging";

    /**
     * Returns the 'maxDocs' number of results starting at the first one.
     * Maxdocs is specified inside XTF configuration.
     */
    @Override
    public SearchResultSet makeSearch(SearchForm searchForm) {
        return makeSearch(searchForm, 1, 1);
    }

    /**
     * Request XTF keyword search (raw=1 to return XML)
     * Returns the 'maxDocs' number of results starting at the specified start.
     * Maxdocs is specified inside XTF configuration.
     */
    @Override
    public SearchResultSet makeSearch(SearchForm searchForm, int start, int end) {

        // Construct the URL we are going to use to query XTF
        String searchXTFURL = buildQueryURL(searchForm, start, end);

        // if the query URL is null return empty result set.
        if (searchXTFURL == null) {
            return new SearchResultSet(0, "", 0f,
                    new ArrayList<SearchResult>(), new ArrayList<FacetGroup>(),
                    "A problem occurred making the search (xtf).");
        }

        // parse search results into a SearchResultSet
        return parseSearchResults(getDocument(searchXTFURL));

    }

    protected Document getDocument(String url) {

        // Read document from URL and put results in Document.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(url);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    /**
     * Get the name of the search index to query for a given SearchForm.
     */
    private String getIndexName(SearchForm searchForm) {
        return searchForm.hasRecallScale() ? INDEX_NAME_VARIABLE_RECALL
                                           : INDEX_NAME_REGULAR;
    }

    protected String buildQueryURL(SearchForm searchForm, int start, int end) {

        String xtfURL = Properties.getString("xtfURL");
        String searchXTFURL = xtfURL + "search?";

        try {
             searchXTFURL +=
                    "indexName=" + URLEncoder.encode(getIndexName(searchForm), "UTF-8") +
                    "&raw=1;smode=advanced;startDoc=" + start;

            // Keywords
            if (searchForm.getKeyword() != null) {

                searchXTFURL += "&keyword="
                        + URLEncoder.encode(searchForm.getKeyword(), "UTF-8");
            }

            if (searchForm.hasRecallScale()) {
                searchXTFURL += String.format("&recallScale=%f",
                                              searchForm.getRecallScale());
            }

            if (searchForm.getFullText() != null) {

                searchXTFURL += "&text="
                        + URLEncoder.encode(searchForm.getFullText(), "UTF-8");
            }
            if (searchForm.getExcludeText() != null) {

                searchXTFURL += "&text-exclude="
                        + URLEncoder.encode(searchForm.getExcludeText(),
                                "UTF-8");
            }

            // Join
            if (searchForm.getTextJoin() != null) {

                if (searchForm.getTextJoin().equals("or")) {
                    searchXTFURL += "&text-join=or";
                } else {
                    searchXTFURL += "&text-join=";
                }

            }

            // File ID
            if (searchForm.getFileID() != null) {

                searchXTFURL += "&fileID="
                        + URLEncoder.encode(searchForm.getFileID(), "UTF-8");
            }

            // Classmark
            if (searchForm.getShelfLocator() != null) {

                // remove all punctuation and run a search-shelfLocator
                // search (for full and partial classmark match)
                String sLoc = searchForm.getShelfLocator().replaceAll("\\W+", " ");

                searchXTFURL += "&search-shelfLocator="
                        + URLEncoder.encode(sLoc, "UTF-8");
            }

            // Metadata
            if (searchForm.getTitle() != null) {

                searchXTFURL += "&title="
                        + URLEncoder.encode(searchForm.getTitle(), "UTF-8");
            }
            if (searchForm.getAuthor() != null) {

                searchXTFURL += "&nameFullForm="
                        + URLEncoder.encode(searchForm.getAuthor(), "UTF-8");
            }
            if (searchForm.getSubject() != null) {

                searchXTFURL += "&subjectFullForm="
                        + URLEncoder.encode(searchForm.getSubject(), "UTF-8");
            }
            if (searchForm.getLocation() != null) {

                searchXTFURL += "&placeFullForm="
                        + URLEncoder.encode(searchForm.getLocation(), "UTF-8");
            }
            if (searchForm.getYearStart() != null) {

                searchXTFURL += "&year=" + searchForm.getYearStart();
            }
            if (searchForm.getYearEnd() != null) {

                searchXTFURL += "&year-max=" + searchForm.getYearEnd();
            }

            // Facets
            int facetCount = 0; // xtf needs to know order of facets.
            if (searchForm.getFacetCollection() != null) {

                facetCount++;
                searchXTFURL += "&f"
                        + facetCount
                        + "-collection="
                        + URLEncoder.encode(searchForm.getFacetCollection(),
                                "UTF-8");
            }

            if (searchForm.getFacetSubject() != null) {

                facetCount++;
                searchXTFURL += "&f"
                        + facetCount
                        + "-subject="
                        + URLEncoder.encode(searchForm.getFacetSubject(),
                                "UTF-8");
            }

            if (searchForm.getFacetDate() != null) {

                facetCount++;
                searchXTFURL += "&f" + facetCount + "-date="
                        + URLEncoder.encode(searchForm.getFacetDate(), "UTF-8");
            }

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        // System.out.println(searchXTFURL);
        return searchXTFURL;
    }

    /**
     * Parse the XML dom document and put the results into a list of
     * SearchResult objects.
     *
     * @param dom
     * @return List of the search results
     */
    protected SearchResultSet parseSearchResults(Document dom) {

        // Check input - XTF may be down.
        if (dom == null) {
            return new SearchResultSet(0, "", 0f,
                    new ArrayList<SearchResult>(), new ArrayList<FacetGroup>(),
                    "A problem occurred making the search (xtf).");
        }

        // Get the root element
        Element docEle = dom.getDocumentElement();
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();

        // Catch any errors
        if (!docEle.getNodeName().equals("crossQueryResult")) {
            return new SearchResultSet(
                    0,
                    "",
                    0f,
                    new ArrayList<SearchResult>(),
                    new ArrayList<FacetGroup>(),
                    "Too many results, try a smaller range, eliminating wildcards, or making them more specific. ");
        }

        // Add in all the (docHit) results into a Hashtable by Item Number
        NodeList docHits = docEle.getElementsByTagName("docHit");

        if (docHits != null) {
            for (int i = 0; i < docHits.getLength(); i++) {

                Element node = (Element) docHits.item(i);
                Element meta = (Element) node.getElementsByTagName("meta")
                        .item(0);

                Element itemIdElement = (Element) meta.getElementsByTagName(
                        "fileID").item(0);

                // Sometimes results may appear without any metadata, ignore
                // these.
                if (itemIdElement != null) {
                    String itemId = getValueInText(itemIdElement);
                    LOG.debug("itemId = " + itemId);

                    itemId = itemId.replaceAll("\\s+", ""); // remove whitespace

                    SearchResult result = createSearchResult(node);
                    results.add(result);

                }
            }

            // ensure results are in the right order by score.
            if (results.size() > 0) {
                Collections.sort(results);
            }

        }

        // Get general search result data
        int totalDocs = Integer.parseInt(docEle.getAttribute("totalDocs"));
        float queryTime = Float.parseFloat(docEle.getAttribute("queryTime"));
        Element spelling = (Element) docEle.getElementsByTagName("spelling")
                .item(0);
        String suggestedTerm = "";
        if (spelling != null) {
            Element suggestion = (Element) spelling.getElementsByTagName(
                    "suggestion").item(0);
            suggestedTerm = suggestion.getAttribute("suggestedTerm");
        }

        // facets
        ArrayList<FacetGroup> facetGroups = new ArrayList<FacetGroup>();
        for (int i=0; i<docEle.getChildNodes().getLength(); i++) {
            Node childNode = docEle.getChildNodes().item(i);

            if (childNode.getNodeName().equals("facet")) {

                Element facetElement = (Element) childNode;
                ArrayList<Facet> facets = new ArrayList<Facet>();
                String field = facetElement.getAttributes().getNamedItem("field").getTextContent();
                field = field.replace("facet-", ""); // remove facet- from that start.

                int facetGroupOccurances = Integer.parseInt(facetElement.getAttributes().getNamedItem("totalDocs").getTextContent());

                NodeList groups = facetElement.getElementsByTagName("group");
                for (int j=0; j<groups.getLength(); j++) {
                    Node facetNode = groups.item(j);
                    String band = facetNode.getAttributes().getNamedItem("value").getTextContent();
                    int occurances = Integer.parseInt(facetNode.getAttributes().getNamedItem("totalDocs").getTextContent());
                    int rank = Integer.parseInt(facetNode.getAttributes().getNamedItem("rank").getTextContent());
                    Facet facet = new Facet(field, band, occurances, rank);
                    facets.add(facet);
                }

                FacetGroup facetGroup = new FacetGroup(field, facets, facetGroupOccurances);
                facetGroups.add(facetGroup);
            }
        }

        return new SearchResultSet(totalDocs, suggestedTerm, queryTime,
                results, facetGroups, "");
    }

    /**
     * Creates a new SearchResult from the given Node.
     */
    public SearchResult createSearchResult(Element node) {

        String title = "";
        String id = "";
        int score = -1;
        int startPage = 0;
        String startPageLabel = "";
        List<String> snippets = new ArrayList<String>();
        String itemType = "bookormanuscript"; // default
        String thumbnailURL = null;
        String thumbnailOrientation = null;

        // look at all the child tags
        if (node.getNodeName().equals("docHit")) {

            // META Search Info.
            Element meta = (Element) node.getElementsByTagName("meta").item(0);

            title = getValueInHTML(meta.getElementsByTagName("title").item(0));
            id = getValueInText(meta.getElementsByTagName("fileID").item(0));

            id = id.replaceAll("\\s+", ""); // remove whitespace

            score = Integer.parseInt(node.getAttribute("score"));

            final String startPageString = meta.getElementsByTagName("startPage")
                    .item(0).getFirstChild().getTextContent();

            if (Utils.isSimpleIntFormat(startPageString)){
                startPage = Integer.parseInt(startPageString);
            }
            else{
                // TODO fix logging on AWS server, so data errors can be picked up.
                LOG.error("Possible data error - unable to parse string '" + startPageString +
                    "' expected to be int format.\nDoc title '" + title +
                    "'\nError in item ID " + id + "\n\n");

                // Possibly the wrong thing to do, but to allow user to access
                // the page, set the page number to 1 rather than guessing a page number.
                startPage = 1;
            }

            startPageLabel = node.getElementsByTagName("startPageLabel")
                    .item(0).getTextContent();

            NodeList snippetNodes = node.getElementsByTagName("snippet");

            for (int i = 0; i < snippetNodes.getLength(); i++) {
                Node snippetNode = snippetNodes.item(i);
                String snippet = getValueInHTML(snippetNode);
                snippets.add(snippet);
            }

            // itemType
            if (node.getElementsByTagName("itemType").item(0)!=null) {
                itemType = getValueInText(node.getElementsByTagName("itemType").item(0));
            }

            // Thumbnail
            if (node.getElementsByTagName("thumbnailUrl").item(0)!=null) {
                thumbnailURL = getValueInText(node.getElementsByTagName("thumbnailUrl").item(0));
            }
            if (node.getElementsByTagName("thumbnailOrientation").item(0)!=null) {
                thumbnailOrientation = getValueInText(node.getElementsByTagName("thumbnailOrientation").item(0));
            }

        }

        return new SearchResult(title, id, startPage, startPageLabel,
                snippets, score, itemType, thumbnailURL, thumbnailOrientation);

    }

    /**
     * Return a flat string with just the text value of a specified node (and
     * any sub-nodes).
     *
     * @param node
     * @return
     */
    public String getValueInText(Node node) {

        if (node.getNodeType() == Node.TEXT_NODE) {
            if (node.getParentNode().getNodeName().equals("term")) {
                return node.getNodeValue().replaceAll("<.*>", "");
            }
            // remove complete and partial tags as much as possible
            String noCompleteTags = node.getNodeValue().replaceAll("<.*>", "");
            return noCompleteTags.replaceAll("<\\w*|\\w*>", "");
        }

        NodeList children = node.getChildNodes();
        StringBuffer textValue = new StringBuffer();
        if (node.getNodeValue() == null && children != null) {

            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                textValue.append(getValueInText(child));
            }

            return textValue.toString();
        }

        return "";

    }

    /**
     * Strips out the tags from node and flattens content. Where a value appears
     * in <hit><term> tags this is translated into <b> html.
     *
     * Recursive.
     *
     * @param node
     * @return
     */
    public String getValueInHTML(Node node) {

        if (node == null)
            return "";

        if (node.getNodeType() == Node.TEXT_NODE) {
            // if this is a snippet, bold the matching word(s).
            if (node.getParentNode().getNodeName().equals("term")) {
                return "<b>" + node.getNodeValue().replaceAll("<.*>", "")
                        + "</b>";
            }
            // remove complete and partial tags as much as possible
            String noCompleteTags = node.getNodeValue().replaceAll("<.*>", "");
            return noCompleteTags.replaceAll("<\\w*|\\w*>", "");
        }

        NodeList children = node.getChildNodes();
        StringBuffer textValue = new StringBuffer();
        if (node.getNodeValue() == null && children != null) {

            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                textValue.append(getValueInHTML(child));
            }

            return textValue.toString();
        }

        return "";

    }
}
