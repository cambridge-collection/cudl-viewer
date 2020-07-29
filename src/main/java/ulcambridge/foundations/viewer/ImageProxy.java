package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Properties;

@Controller
@RequestMapping("/imageproxy/")
public class ImageProxy {

    /**
     * This is used when we want to display images as if they were appearing
     * locally, but actually stream them from a remote location.
     *
     * This is needed to view images from the SeaDragon Ajax which are stored on
     * a remote server. This servers location is specified in the cudl-global
     * properties file in the imageServer option.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    // on path /imageproxy/
    @RequestMapping(value = "/**")
    public ModelAndView handleViewRequest(HttpServletRequest request,
            HttpServletResponse response, @Value("${imageServer}") URI imageServer) throws Exception {

        String imagePath = (String) request
                .getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        // do nothing if image proxy is off in the properties file.
        if (Properties.getString("proxyURL").isEmpty()) {
            return null;
        }

        URL url = new URL(imageServer + imagePath);

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        response.setContentType(connection.getContentType());
        InputStream is = connection.getInputStream();

        try {
            is = url.openStream();
            byte[] byteChunk = new byte[4096];

            int n;

            while ((n = is.read(byteChunk)) > 0) {
                out.write(byteChunk, 0, n);
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (is != null) {
                is.close();
            }
            if (out != null) {
                out.close();
            }
        }

        return null;
    }
}
