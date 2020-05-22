package ulcambridge.foundations.viewer.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Profile("!test")
public class XTFSearch implements Search {

    private static final Logger LOG = LoggerFactory.getLogger(XTFSearch.class.getName());

    private static final String INDEX_NAME_REGULAR = "index-cudl",
        INDEX_NAME_VARIABLE_RECALL = "index-cudl-tagging";

    private final URI xtfURL;

    public XTFSearch(@Qualifier("xtfURL") URI xtfURL) {
        Assert.notNull(xtfURL, "xtfURL is required");
        this.xtfURL = xtfURL;
    }

    /**
     * Returns the 'maxDocs' number of results starting at the first one.
     * Maxdocs is specified inside XTF configuration.
     */
    @Override
    public SearchResultSet makeSearch(final SearchForm searchForm) {
        return makeSearch(searchForm, 1, 1);
    }

    /**
     * Request XTF keyword search (raw=1 to return XML)
     * Returns the 'maxDocs' number of results starting at the specified start.
     * Maxdocs is specified inside XTF configuration.
     */
    @Override
    public SearchResultSet makeSearch(final SearchForm searchForm,
                                      final int start,
                                      final int end) {

        // Construct the URL we are going to use to query XTF
        final String searchXTFURL = buildQueryURL(searchForm, start, end);

        // if the query URL is null return empty result set.
        if (searchXTFURL == null) {
            return new SearchResultSet(0, "", 0f,
                new ArrayList<>(), new ArrayList<>(),
                "A problem occurred making the search (xtf).");
        }

        // parse search results into a SearchResultSet
        return parseSearchResults(getDocument(searchXTFURL));
    }


    protected Document getDocument(final String url) {

        // Read document from URL and put results in Document.
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(url);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the name of the search index to query for a given SearchForm.
     */
    private String getIndexName(SearchForm searchForm) {
        return searchForm.hasRecallScale() ? INDEX_NAME_VARIABLE_RECALL
            : INDEX_NAME_REGULAR;
    }

    // TODO - the end parameter has never been used in this function.  Confirm behaviour
    protected String buildQueryURL(final SearchForm searchForm, final int start, final int end) {
        final UriComponentsBuilder uriB = UriComponentsBuilder.fromUri(this.xtfURL.resolve("search"));

        uriB.queryParam("indexName", getIndexName(searchForm));
        uriB.queryParam("raw", "1");
        uriB.queryParam("smode", "advanced");
        uriB.queryParam("startDoc", start);

        // Expand/contract facet
        if (searchForm.getExpandFacet() != null) {
            uriB.queryParam("expand", searchForm.getExpandFacet());
        }

        // Keywords
        if (searchForm.getKeyword() != null) {
            uriB.queryParam("keyword",searchForm.getKeyword());
        }

        if (searchForm.hasRecallScale()) {
            uriB.queryParam("recallScale", String.format("%f", searchForm.getRecallScale()));
        }

        if (searchForm.getFullText() != null) {
            uriB.queryParam("text", searchForm.getFullText());
        }
        if (searchForm.getExcludeText() != null) {
            uriB.queryParam("text-exclude", searchForm.getExcludeText());
        }

        // Join
        if (searchForm.getTextJoin() != null) {
            if ("or".equals(searchForm.getTextJoin())) {
                uriB.queryParam("text-join", "or");
            } else {
                uriB.queryParam("text-join", "");
            }
        }

        // File ID
        if (searchForm.getFileID() != null) {
            uriB.queryParam("fileID", searchForm.getFileID());
        }

        // Classmark
        if (searchForm.getShelfLocator() != null) {
            // remove all punctuation and run a search-shelfLocator
            // search (for full and partial classmark match)
            final String sLoc = searchForm.getShelfLocator().replaceAll("\\W+", " ");
            uriB.queryParam("search-shelfLocator", sLoc);
        }

        // Metadata
        if (searchForm.getTitle() != null) {
            uriB.queryParam("title", searchForm.getTitle());
        }

        if (searchForm.getAuthor() != null) {
            uriB.queryParam("nameFullForm", searchForm.getAuthor());
        }

        if (searchForm.getSubject() != null) {
            uriB.queryParam("subjectFullForm", searchForm.getSubject());
        }

        if (searchForm.getLanguage() != null) {
            uriB.queryParam("languageString", searchForm.getLanguage());
        }

        if (searchForm.getPlace() != null) {
            uriB.queryParam("placeFullForm", searchForm.getPlace());
        }

        if (searchForm.getLocation() != null) {
            uriB.queryParam("physicalLocation", searchForm.getLocation());
        }

        if (searchForm.getYearStart() != null) {
            uriB.queryParam("year", searchForm.getYearStart());
        }

        if (searchForm.getYearEnd() != null) {
            uriB.queryParam("year-max", searchForm.getYearEnd());
        }

        // Facets
        int facetCount = 0; // xtf needs to know order of facets.
        if (searchForm.getFacetCollection() != null) {
            final String key = String.format("f%d-collection", ++facetCount);
            uriB.queryParam(key, searchForm.getFacetCollection());
        }

        if (searchForm.getFacetSubject() != null) {
            uriB.queryParam(
                String.format("f%d-subject", ++facetCount),
                searchForm.getFacetSubject()
            );
        }

        if (searchForm.getFacetLanguage() != null) {
            uriB.queryParam(
                String.format("f%d-language", ++facetCount),
                searchForm.getFacetLanguage()
            );
        }

        if (searchForm.getFacetPlace() != null) {
            uriB.queryParam(
                String.format("f%d-place", ++facetCount),
                searchForm.getFacetPlace()
            );
        }

        if (searchForm.getFacetLocation() != null) {
            uriB.queryParam(
                String.format("f%d-location", ++facetCount),
                searchForm.getFacetLocation()
            );
        }

        if (searchForm.getFacetDate() != null) {
            uriB.queryParam(
                String.format("f%d-date", ++facetCount),
                searchForm.getFacetDate()
            );
        }

        return uriB.toUriString();
    }

    /**
     * Parse the XML dom document and put the results into a list of
     * SearchResult objects.
     *
     * @param dom
     * @return List of the search results
     */
    protected SearchResultSet parseSearchResults(final Document dom) {

        // Check input - XTF may be down.
        if (dom == null) {
            return new SearchResultSet(0, "", 0f,
                new ArrayList<>(), new ArrayList<>(),
                "A problem occurred making the search (xtf).");
        }

        // Get the root element
        final Element docEle = dom.getDocumentElement();
        final ArrayList<SearchResult> results = new ArrayList<>();

        // Catch any errors
        if (!"crossQueryResult".equals(docEle.getNodeName())) {
            return new SearchResultSet(
                0,
                "",
                0f,
                new ArrayList<>(),
                new ArrayList<>(),
                "Too many results, try a smaller range, eliminating wildcards, or making them more specific. ");
        }

        // Add in all the (docHit) results into a Hashtable by Item Number
        final NodeList docHits = docEle.getElementsByTagName("docHit");

        if (docHits != null) {
            for (int i = 0; i < docHits.getLength(); i++) {
                final Element node = (Element) docHits.item(i);
                final Element meta = (Element) node.getElementsByTagName("meta")
                    .item(0);

                final Element itemIdElement = (Element) meta.getElementsByTagName(
                    "fileID").item(0);

                // Sometimes results may appear without any metadata, ignore
                // these.
                if (itemIdElement != null) {
                    results.add(createSearchResult(node));
                }
            }

            // ensure results are in the right order by score.
            if (results.size() > 0) {
                Collections.sort(results);
            }
        }

        // Get general search result data
        final int totalDocs = Integer.parseInt(docEle.getAttribute("totalDocs"));
        final float queryTime = Float.parseFloat(docEle.getAttribute("queryTime"));
        final Element spelling = (Element) docEle.getElementsByTagName("spelling")
            .item(0);
        String suggestedTerm = "";
        if (spelling != null) {
            Element suggestion = (Element) spelling.getElementsByTagName(
                "suggestion").item(0);
            suggestedTerm = suggestion.getAttribute("suggestedTerm");
        }

        // facets
        final ArrayList<FacetGroup> facetGroups = new ArrayList<>();
        for (int i=0; i<docEle.getChildNodes().getLength(); i++) {
            final Node childNode = docEle.getChildNodes().item(i);

            if ("facet".equals(childNode.getNodeName())) {

                final Element facetElement = (Element) childNode;
                final ArrayList<Facet> facets = new ArrayList<>();
                String field = facetElement.getAttributes().getNamedItem("field").getTextContent();
                field = field.replace("facet-", ""); // remove facet- from that start.

                final int facetGroupOccurrences = Integer.parseInt(facetElement.getAttributes().getNamedItem("totalDocs").getTextContent());

                final NodeList groups = facetElement.getElementsByTagName("group");
                for (int j = 0; j < groups.getLength(); j++) {
                    final Node facetNode = groups.item(j);
                    final String band = facetNode.getAttributes().getNamedItem("value").getTextContent();
                    final int occurrences = Integer.parseInt(facetNode.getAttributes().getNamedItem("totalDocs").getTextContent());
                    final int rank = Integer.parseInt(facetNode.getAttributes().getNamedItem("rank").getTextContent());
                    final Facet facet = new Facet(field, band, occurrences, rank);
                    facets.add(facet);
                }

                final FacetGroup facetGroup = new FacetGroup(field, facets, facetGroupOccurrences);
                facetGroups.add(facetGroup);
            }
        }

        return new SearchResultSet(totalDocs, suggestedTerm, queryTime,
            results, facetGroups, "");
    }

    /**
     * Creates a new SearchResult from the given Node.
     */
    public SearchResult createSearchResult(final Element node) {

        String title = "";
        String id = "";
        int score = -1;
        int startPage = 0;
        String startPageLabel = "";
        List<String> snippets = new ArrayList<>();
        String itemType = "bookormanuscript"; // default
        String thumbnailURL = null;
        String thumbnailOrientation = null;

        // look at all the child tags
        if ("docHit".equals(node.getNodeName())) {

            // META Search Info.
            final Element meta = (Element) node.getElementsByTagName("meta").item(0);

            title = getValueInHTML(meta.getElementsByTagName("title").item(0));
            id = getValueInText(meta.getElementsByTagName("fileID").item(0));

            id = id.replaceAll("\\s+", ""); // remove whitespace

            score = Integer.parseInt(node.getAttribute("score"));

            final String startPageString = meta.getElementsByTagName("startPage")
                .item(0).getFirstChild().getTextContent();

            try {
                startPage = Integer.parseInt(startPageString);
            }
            catch(NumberFormatException e) {
                // TODO Send email to dev team regarding incorrect data format
                LOG.error("Error in item ID {}: Unable to parse value as an int: '{}' (Doc title: '{}')",
                        id, startPageString, title);
                startPage = 1;
            }

            startPageLabel = node.getElementsByTagName("startPageLabel")
                .item(0).getTextContent();

            final NodeList snippetNodes = node.getElementsByTagName("snippet");

            for (int i = 0; i < snippetNodes.getLength(); i++) {
                final Node snippetNode = snippetNodes.item(i);
                snippets.add(getValueInHTML(snippetNode));
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
    public String getValueInText(final Node node) {

        if (node.getNodeType() == Node.TEXT_NODE) {
            if ("term".equals(node.getParentNode().getNodeName())) {
                return node.getNodeValue().replaceAll("<.*>", "");
            }
            // remove complete and partial tags as much as possible
            return node.getNodeValue()
                .replaceAll("<.*>", "")
                .replaceAll("<\\w*|\\w*>", "");
        }

        final NodeList children = node.getChildNodes();
        final StringBuilder textValue = new StringBuilder();

        if (node.getNodeValue() == null && children != null) {

            for (int i = 0; i < children.getLength(); i++) {
                final Node child = children.item(i);
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
    public String getValueInHTML(final Node node) {

        if (node == null)
            return "";

        if (node.getNodeType() == Node.TEXT_NODE) {
            // if this is a snippet, bold the matching word(s).
            if ("term".equals(node.getParentNode().getNodeName())) {
                return String.format(
                    "<b>%s</b>",
                    node.getNodeValue().replaceAll("<.*>", "")
                );
            }
            // remove complete and partial tags as much as possible
            return node.getNodeValue()
                .replaceAll("<.*>", "")
                .replaceAll("<\\w*|\\w*>", "");
        }

        final NodeList children = node.getChildNodes();
        final StringBuilder textValue = new StringBuilder();
        if (node.getNodeValue() == null && children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                final Node child = children.item(i);
                textValue.append(getValueInHTML(child));
            }

            return textValue.toString();
        }
        return "";
    }
}
