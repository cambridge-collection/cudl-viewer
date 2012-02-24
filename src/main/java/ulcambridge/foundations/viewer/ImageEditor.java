package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Properties;

@Controller
public class ImageEditor {

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
	// on path /imageeditor/
	@RequestMapping(value = "/**")
	public ModelAndView handleViewRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		float bgColourR = 0.05f;
		float bgColourG = 0.05f;
		float bgColourB = 0.05f;
		
		String bgColourStringR = (String) request.getSession().getAttribute("bgColourR");
		String bgColourStringG = (String) request.getSession().getAttribute("bgColourG");
		String bgColourStringB = (String) request.getSession().getAttribute("bgColourB");

		if (bgColourStringR!=null || bgColourStringG!=null || bgColourStringB!=null) {
			bgColourR = Float.parseFloat(bgColourStringR);
			bgColourG = Float.parseFloat(bgColourStringG);
			bgColourB = Float.parseFloat(bgColourStringB);
		}
		//System.out.println(replaceR1 + " " + replaceG1 + " " + replaceB1);
		//System.out.println(replaceR2 + " " + replaceG2 + " " + replaceB2);

		String imagePath = (String) request
				.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		if (imagePath.endsWith("png")| imagePath.endsWith("jpg")) {

			URL url = new URL(Properties.getString("imageServer") + imagePath);

			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());

			MBFImage image = ImageUtilities.readMBF(url);
			// DisplayUtilities.display(image);

			// image = ColourSpace.convert(image, ColourSpace.RGB);

			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {

					float R = image.getBand(0).pixels[y][x];
					float G = image.getBand(1).pixels[y][x];
					float B = image.getBand(2).pixels[y][x];

					// find blue pixels
					if (B>R && G>R) {
						image.getBand(0).pixels[y][x] = bgColourR;
						image.getBand(1).pixels[y][x] = bgColourG;
						image.getBand(2).pixels[y][x] = bgColourB;
					}
					// System.out.println(R+" "+G+" "+B);
				}
			}

			ImageUtilities.write(image, "png", out);

			if (out != null) {
				out.close();
			}

		} else {

			URL url = new URL(Properties.getString("imageServer") + imagePath);

			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());
			InputStream is = null;
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

		}

		return null;
	}
}
