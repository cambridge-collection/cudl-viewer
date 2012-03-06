package ulcambridge.foundations.viewer.model;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Holds search facet information, can be used to refine searches.
 * 
 * @author jennie
 * 
 */
public class SearchResultFacet {

	private List<String> groups;
	private String field;

	public SearchResultFacet(Element facet) {

		// Full field is facet-date etc. Record 'date' part. 
		String fullField = facet.getAttribute("field");
		this.field = fullField.substring(fullField.indexOf("-")+1);
		
		this.groups = new ArrayList<String>();
		NodeList groupNodes = facet.getChildNodes();
		for (int i = 0; i < groupNodes.getLength(); i++) {
			Node groupNode = groupNodes.item(i);
			
			if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
			  groups.add(((Element)groupNode).getAttribute("value").toString());
			}
		}
	}
	
	public String getField() {
		return field;
	}
	
	public int getNumGroups() {
		return groups.size();
	}
	
	public List<String> getGroups() {
		return groups;
	}

}
