package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Holds information for an individual search result. Item id can be used to
 * pull back more information on an item from the ItemFactory.
 * 
 * @author jennie
 * 
 */
public class SearchResult implements Comparable<SearchResult> {

	private String title;
	private String id;
	private List<Facet> facets = new ArrayList<Facet>();
	private int score; // how relevant is this result, used for ordering.

	// Hashtable containing the page numbers and the matching
	// snippets that appear on that page.
	private Hashtable<Integer, List<String>> snippets = new Hashtable<Integer, List<String>>();

	// TODO snippets
	/**
	 * Creates a new SearchResult from the given Node.
	 */
	public SearchResult(Element node) {

		// look at all the child tags
		if (node.getNodeName().equals("docHit")) {

			NodeList metaAndSnippets = node.getChildNodes();

			// META Search Info.
			Element meta = (Element) node.getElementsByTagName("meta").item(0);

			this.title = getValueInHTML(meta.getElementsByTagName("title")
					.item(0));
			this.id = getValueInHTML(meta.getElementsByTagName("fileID")
					.item(0));
			this.score = Integer.parseInt(node.getAttribute("score"));

			Integer startPage = new Integer(meta
					.getElementsByTagName("startPage").item(0).getTextContent());

			NodeList children = meta.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child.getNodeName().startsWith("facet-")) {
					Facet facet = new Facet(child.getNodeName().substring(6),
							getValueInHTML(child));
					facets.add(facet);
				}
			}

			// SNIPPET Search Info
			this.snippets = new Hashtable<Integer, List<String>>();
			List<String> snippetList = new ArrayList<String>();

			List<Node> snippetNodes = getNodes("snippet", metaAndSnippets);
			for (int i = 0; i < snippetNodes.size(); i++) {
				Node snippetNode = snippetNodes.get(i);
				if (snippetNode != null) {

					snippetList.add(getValueInHTML(snippetNode));
				}
			}
			this.snippets.put(startPage, snippetList);
		}
	}

	/**
	 * Looks through the list of nodes and returns the node(s) with the
	 * specified name.
	 * 
	 * @param nodeName
	 * @param nodes
	 * @return
	 */
	private List<Node> getNodes(String nodeName, NodeList nodes) {

		ArrayList<Node> matches = new ArrayList<Node>();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node child = nodes.item(i);
			if (child.getNodeName().equals(nodeName)) {
				matches.add(child);
			}
		}
		return matches;
	}

	private String getValueInHTML(Node node) {

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

	public String getTitle() {
		return this.title;
	}

	public String getId() {
		return this.id;
	}

	public List<Facet> getFacets() {
		return this.facets;
	}

	/**
	 * @return Returns a Hashtable of Page numbers and a List of snippets that
	 *         occur on that page.
	 */
	public Hashtable<Integer, List<String>> getSnippets() {
		return this.snippets;
	}

	public void insertSnippets(Integer pageNumber, List<String> snippets) {
		this.snippets.put(pageNumber, snippets);
	}

	/**
	 * Highest score should appear first.
	 */
	public int compareTo(SearchResult o) {

		return ((SearchResult) o).score - this.score;

	}

	/**
	 * Search Results are considered the same if they have the same ID. So are
	 * about the same book. There should be only one SearchResult object per
	 * item (book).
	 */
	/*
	 * @Override public boolean equals(Object o) { if (o instanceof
	 * SearchResult) { SearchResult r = (SearchResult) o; return this.id ==
	 * r.id; } return false; }
	 * 
	 * @Override public int hashCode(Object o) { if (o instanceof SearchResult)
	 * { SearchResult r = (SearchResult) o; return this.id == r.id; } return
	 * false; }
	 */

}
