package ulcambridge.foundations.viewer.transcriptions;

public class NewtonTranscriptionFormatter implements TranscriptionFormatter {

    // The url for the transcription specified should be something like the
    // following:.
    // http://www.newtonproject.sussex.ac.uk/view/extract/normalized/THEM00009/start=p001r&end=p001r
    // onload should be the function to call onpage load.
    public String format(String sourceURL, String sourcePage) {

        // Get basURL for transcription, e.g. http://www.newtonproject.sussex.ac.uk/
        // (reads up to first / after http://)
        String baseURL = sourceURL.substring(0, sourceURL.indexOf('/', 7));

        StringBuffer output = new StringBuffer();

        // replace any relative links, should all start with /mainui
        // FIXME - temporary until sussex has the appropriate feed setup.
        sourcePage = sourcePage.replaceAll("\"/mainui", "\"" + baseURL
                + "/mainui");

        // Bit hacky - but we read it in in UTF-8.
        sourcePage = sourcePage.replaceAll("charset\\=ISO-8859-1", "charset\\=UTF-8");

        // replace any links to the view (diplomatic or normal)
        // thisURL = thisURL.replaceAll("&view=\\w", "");
        // sourcePage =
        // sourcePage.replaceAll("\"/view/extract/diplomatic/[\\w|/|\\d|&|=]*\"",
        // thisURL+"&view=diplomatic" );
        // sourcePage =
        // sourcePage.replaceAll("\"/view/extract/normalized/[\\w|/|\\d|&|=]*\"",
        // thisURL+"&view=normal" );

        // Add HTML tag and DOCTYPE.
        output.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html>");

        // include the page head from the source
        if (sourcePage.indexOf("<head>") != -1
                && sourcePage.indexOf("</head>") != -1) {
            // output.append("<script type='text/javascript'>window.onerror = function() { alert (\"error\") }</script>\n");
            output.append(sourcePage.substring(sourcePage.indexOf("<head>"),
                    sourcePage.indexOf("</head>")));
            output.append("<link href=\"styles/style-transcription.css\" rel=\"stylesheet\" type=\"text/css\" />\n");

            output.append("</head><body><div class=\"transcription\">\n");
            // Add link to Newton Project
            output.append("<div class=\"transcription-credit\">Transcription by the <a target='_blank' href='"+
            "http://www.newtonproject.sussex.ac.uk'>Newton Project</a></div>");
            // output.append("<div class=\"transcription-credit\">Transcription by the <a target='_blank' href='http://www.newtonproject.sussex.ac.uk/'>Newton Project</a></div>");

        }

        // include the content //<!--start-text-container-->
        if (sourcePage.indexOf("<div id=\"tei\">") != -1
                && sourcePage.indexOf("<!--end-text-container-->") != -1) {

            output.append(sourcePage.substring(
                    sourcePage.indexOf("<div id=\"tei\">"),
                    sourcePage.indexOf("<!--end-text-container-->")));

        }

        // End Tag (inc end of transcription div.)
        output.append("</div><div id=\"navigation\"></div></body></HTML>");

        return output.toString();
    }

}
