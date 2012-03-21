package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Holds information for an individual search result. Item id can be used to
 * pull back more information on an item from the ItemFactory.
 * 
 * @author jennie
 * 
 */
public class SearchResult {

	private String title;
	private String id;
	private List<Facet> facets = new ArrayList<Facet>();
	private List<String> snippets = new ArrayList<String>();

	// TODO snippets
	/**
	 * Creates a new SearchResult from the given Node.
	 */
	public SearchResult(Node node) {

		// look at all the child tags
		if (node.getNodeName().equals("docHit")) {

			NodeList metaAndSnippets = node.getChildNodes();

			// META Search Info.
			Node meta = (Node) getNodes("meta", metaAndSnippets).get(0);

			NodeList children = meta.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {

				Node child = children.item(i);

				if (child.getNodeName().equals("title")) {
					this.title = getValueInHTML(child);
				}

				else if (child.getNodeName().equals("fileID")) {
					this.id = getValueInHTML(child);
				}
				
				else if (child.getNodeName().startsWith("facet-")) {
					Facet facet = new Facet(child.getNodeName().substring(6),getValueInHTML(child));
					facets.add(facet);
				}				
			}

			// SNIPPET Search Info
			List<Node> snippetNodes = getNodes("snippet", metaAndSnippets);
			for (int i = 0; i < snippetNodes.size(); i++) {
				Node snippetNode = snippetNodes.get(i);
				if (snippetNode != null) {
					snippets.add(getValueInHTML(snippetNode));
				}
			}
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
				return "<b>" + node.getNodeValue().replaceAll("<.*>", "") + "</b>";
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
	
	public List<String> getSnippets() {
		return this.snippets;
	}
}
