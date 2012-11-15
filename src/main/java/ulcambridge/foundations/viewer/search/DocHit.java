package ulcambridge.foundations.viewer.search;

public class DocHit {

	private int startPage;
	private String startPageLabel;
	private String snippetHTML;
	
	public DocHit(int startPage, String startPageLabel,String snippetHTML) {
		this.startPage = startPage;
		this.startPageLabel = startPageLabel;
		this.snippetHTML = snippetHTML;		
	}
	
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public String getStartPageLabel() {
		return startPageLabel;
	}
	public void setStartPageLabel(String startPageLabel) {
		this.startPageLabel = startPageLabel;
	}
	public String getSnippetHTML() {
		return snippetHTML;
	}
	public void setSnippetHTML(String snippetHTML) {
		this.snippetHTML = snippetHTML;
	}
	
}
