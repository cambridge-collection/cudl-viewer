package ulcambridge.foundations.viewer.utils;

import ulcambridge.foundations.viewer.model.Properties;

import java.util.Random;

public class ImageServerHelper {

    private final String[] imageServers;
    private final String imageServersProperty;

    public ImageServerHelper() {
        imageServersProperty = Properties.getString("imageServer");

        // If it's a comma separated list of image servers pick a random one.
        if (imageServersProperty!=null) {
            if (imageServersProperty.contains(",")) {
                imageServers = imageServersProperty.split(",");
            } else {
                imageServers = new String[1];
                imageServers[0] = imageServersProperty;
            }
        } else {
            imageServers = new String[0];
        }
    }

    public String[] getImageServers() {
        return imageServers;
    }

    public String getRandomImageServer() {

        Random r = new Random();
        final int random = r.nextInt(imageServers.length);
        return imageServers[random].trim();

    }

    public String getImageServersCSV() {
        return imageServersProperty;
    }

}
