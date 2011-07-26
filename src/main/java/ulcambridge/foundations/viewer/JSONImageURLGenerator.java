package ulcambridge.foundations.viewer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Outputs a JSON object containing the URLs for the images in the specified document. 
 * 
 * @author jennie
 * 
 */
public class JSONImageURLGenerator extends HttpServlet {
	
	ResourceBundle props = ResourceBundle.getBundle("viewer");
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("at JSON IMAGE GENERATOR");
		// Load in the properties specified
		// e.g. MSADD-03958-001
		String document = request.getParameter("doc");
		// remove all non word characters (except hyphen) for security.
		document = document.replaceAll("\\W&&[^-]", "");  
				
		System.out.println("doc: "+document);
		
		String dataURLPath = props.getString("data.directory.urlpath");
		String documentURLPath = dataURLPath+document; 
		
		System.out.println("dataURLPath: "+dataURLPath);
		System.out.println("documentURLPath: "+documentURLPath);
		
		ServletContext context = request.getSession().getServletContext();
		String path = context.getRealPath("/"+documentURLPath);
		File imageDir = new File (path);
		
		System.out.println("path: "+path);
				
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".dzi");
		    }
		};
		
		List images = Collections.synchronizedList(new ArrayList());
		File[] files = imageDir.listFiles(filter);
		for (int i=0; i<files.length; i++) {
			
			File file = files[i];
			String imageURLPath = documentURLPath+"/"+file.getName();			
			JSONObject obj = new JSONObject();
			obj.put("name", file.getName());
			obj.put("imageURL", imageURLPath);
			images.add(obj);
		}
		
		Collections.sort(images, new JSONComparator());
		
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("results", new Integer(files.length));
		jsonResponse.put("images", images);
		
		System.out.println("jsonResponse: "+jsonResponse.toString());
				
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		jsonResponse.writeJSONString(out);
		out.close();				

	}
		
	class JSONComparator implements Comparator {
		   
	    public int compare(Object json1, Object json2){
	   
	        /*
	         * parameter are of type Object, so we have to downcast it
	         * to JSONObjects
	         */
	       
	    	String name1 = ((JSONObject)json1).get("name").toString();        
	    	String name2 = ((JSONObject)json2).get("name").toString();
	       
	        return name1.compareTo(name2);
	    }
	   
	}
}
