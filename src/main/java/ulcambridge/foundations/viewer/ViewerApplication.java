package ulcambridge.foundations.viewer;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ViewerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ViewerApplication.class);

        // TODO: Move this to default configuration file
        app.setDefaultProperties(ImmutableMap.of(
                "server.servlet.session.timeout", 240));

        app.run(args);
    }
}
