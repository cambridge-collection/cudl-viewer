package ulcambridge.foundations.viewer.genizah;

/**
 * Just a couple of utility methods to convert classmarks between normalized and non-normalized.
 * 
 * @author maclean
 *
 */
public class ClassmarkConverter {
	
	/**
	 * Make a more structured, normalized new-style classmark.
	 * 
	 * @param classmark
	 * @return
	 */
	public static String toNormal(String classmark) {
		
		String text = classmark;	// tmp

	    //upper case
	    text = text.toUpperCase();
	    
	    //sorts out T-S->TS
	    text = text.replaceAll("^T\\-S", "TS");
	    
	    //spaces to hyphens
	    text = text.replaceAll("\\s", "-");
	    
	    //dots to hyphens
	    text = text.replaceAll("\\.", "-");
	    
	    //surround brackets with hyphens
	    text = text.replaceAll("\\((.+)\\)", "\\-\\($1\\)\\-");
	    
	    //put a hyphen between letters and digits
	    text = text.replaceAll("([A-Z])(\\d)", "$1\\-$2");

	    //put a hyphen between digits and letters
	    text = text.replaceAll("(\\d)([A-Z])", "$1\\-$2");

	    //splits into constituent parts
	    StringBuilder output = new StringBuilder("MS-");
	    int counter = 0;
	    String[] parts = text.split("\\-");
	    for (String part : parts) {
	    	part = part.replaceAll("^\\(", "");
	    	part = part.replaceAll("\\)$", "");
	    	if (part.matches("(\\d+)")) {
	    		part = String.format("%05d", Integer.parseInt(part));
	    	}
	    	if (counter < parts.length - 1) {
	    		output.append(part).append("-");
	    	} else {
	    		output.append(part);
	    	}
	    	counter++;
	    }
	    text = output.toString();

	    //just in case double hyphens have crept in ... -

	    text = text.replaceAll("\\-\\-", "\\-");

	    //hack for oddities
//	    $text=~s/LEAF\-001\-\+\-006/L1\-L6/;
//	    $text=~s/LEAF\-002\-\+\-005/L2\-L5/;
//	    $text=~s/LEAF\-003\-\+\-004/L3\-L4/;
	    
	    return text;
	}
	
	/**
	 * Make a slightly more readable or old-style version.
	 * 
	 * @param classmark
	 * @return
	 */
	public static String fromNormal(String classmark) {
	     StringBuilder output = new StringBuilder();
	     String[] parts = classmark.substring(3, classmark.length()).split("\\-");
	     int counter = 0;
	     for (String part : parts) {
	    	 if (counter < parts.length - 1) {
	    		 output.append(part.substring(part.lastIndexOf('0') + 1)).append(' ');
	    	 } else {
	    		 output.append(part.substring(part.lastIndexOf('0') + 1));
	    	 }
	    	 counter++;
	     }
	     
	     return output.toString();
	}

}
