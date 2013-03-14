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
	
	public String getQueryType() {
		return queryType;
	}
	
	public boolean isAuthor() {
		return queryType.equals("AUTHOR");
	}
	
	public boolean isKeyword() {
		return queryType.equals("KEYWORD");
	}
	
	public boolean isClassmarkQuery() {
		return queryType.equals("CLASSMARK");
	}
	
	public String getQueryString() {
		return queryString;
	}
	
	public boolean isTitleIdQuery() {
		return queryType.equals("TITLEID");
	}
	
	public boolean isClassmarkIdQuery() {
		return queryType.equals("CLASSMARKID");
	}


}
