package ulcambridge.foundations.viewer.genizah;

/**
 * Query parameters.
 * 
 * @author gilleain
 *
 */
public class GenizahQuery {
	
	private String queryString;
	
	private String queryType;
	
	public GenizahQuery(String queryString, String queryType) {
		this.queryString = queryString;
		this.queryType = queryType;
	}
	
	public boolean isAuthorQuery() {
		return queryType.equals("AUTHOR");
	}
	
	public boolean isKeywordQuery() {
		return queryType.equals("KEYWORD");
	}
	
	public boolean isClassmarkQuery() {
		return queryType.equals("CLASSMARK");
	}
	
	public boolean isClassmarkQueryOld() {
		return queryType.equals("CLASSMARK-OLD");
	}
	
	public String getQueryString() {
		return queryString;
	}

}
