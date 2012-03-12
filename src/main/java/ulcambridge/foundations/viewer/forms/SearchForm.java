package ulcambridge.foundations.viewer.forms;

import java.util.List;

import javax.validation.constraints.Size;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SearchForm {

    protected final Log logger = LogFactory.getLog(getClass());

    @Size(max = 200)
    private String keyword;
    private String resultDisplay;
    
    // facet format: subject=value 
    private List<String> facets;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setFacets(List<String> facets) {
        this.facets = facets;
    }

    public List<String> getFacets() {
        return facets;
    }

	public void setResultDisplay(String resultDisplay) {
		this.resultDisplay = resultDisplay;
	}

	public String getResultDisplay() {
		return resultDisplay;
	}

}