package ulcambridge.foundations.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Reads in the URL specified by the newtonDoc parameter and filters the content
 * to only display what we want.
 * 
 * @author jennie
 * 
 */
public class NewtonTranscriptionViewer extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Load in the url for the transcription specified.
		//http://www.newtonproject.sussex.ac.uk/view/extract/normalized/THEM00009/start=p001r&end=p001r

		String url = request.getParameter("url");

		// TODO input validation and
		// removing non alphanumeric characters from inputs.

		if (url == null) {
			throw new IOException(
					"missing parameter(s). Expecting url.");
		}

		
		System.out.println("Getting transcriptions from: "+url);
		
		String sourcePage = readContent(new URL(url));
		
		//String fullRequestURL = request.getRequestURL().append("?" + request.getQueryString()).toString();

		// This should be http://www.newtonproject.sussex.ac.uk/
		String baseURL = url.substring(0, url.indexOf('/', 7));
		System.out.println("baseURL: " +baseURL);
		
		String transcriptionPage = generateTranscriptionPage(sourcePage, baseURL);
		writePage(response, transcriptionPage);

	}
	
	private String readContent(URL url) throws IOException {
		
		URLConnection connection = (url).openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		
		String inputLine;
		StringBuffer input = new StringBuffer(); 
		
		while ((inputLine = in.readLine()) != null) {
			input.append(inputLine);
			input.append("\n");
		}		

		try {
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return input.toString();

	}		 
	
	private String generateTranscriptionPage(String sourcePage, String baseURL) {
		
		StringBuffer output = new StringBuffer();
				
		// replace any relative links, should all start with /mainui
		// FIXME - temporary until sussex has the appropriate feed setup. 
		sourcePage = sourcePage.replaceAll("\"/mainui", "\""+baseURL+"/mainui" );
			
		// replace any links to the view (diplomatic or normal) 
		//thisURL = thisURL.replaceAll("&view=\\w", "");
		//sourcePage = sourcePage.replaceAll("\"/view/extract/diplomatic/[\\w|/|\\d|&|=]*\"", thisURL+"&view=diplomatic" );
		//sourcePage = sourcePage.replaceAll("\"/view/extract/normalized/[\\w|/|\\d|&|=]*\"", thisURL+"&view=normal" );
		
		// Add HTML tag and DOCTYPE.
		output.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html>");
		
		// include the page head from the source 
		if (sourcePage.indexOf("<head>")!=-1 && sourcePage.indexOf("</head>")!=-1) {
			
			output.append(sourcePage.substring(sourcePage.indexOf("<head>"),sourcePage.indexOf("</head>")));
			output.append("<link href=\"styles/style-transcription.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
			output.append("</head>\n");
				
		} 
		
		// include the content //<!--start-text-container-->
		if (sourcePage.indexOf("<div id=\"tei\">")!=-1 && sourcePage.indexOf("<!--end-text-container-->")!=-1) {
			
			output.append(sourcePage.substring(sourcePage.indexOf("<div id=\"tei\">"),sourcePage.indexOf("<!--end-text-container-->")));
				
		} 
		
		// End Tag
		output.append("</HTML>");

		return output.toString();
	}
	
	/**
	 * Sets content type as html
	 * @param response
	 * @param page
	 * @throws IOException
	 */
	private void writePage(HttpServletResponse response, String page) throws IOException {
		response.setHeader("content-type","text/html");
		PrintWriter out = response.getWriter();		
		out.print(page);
		out.close();
	}
}
