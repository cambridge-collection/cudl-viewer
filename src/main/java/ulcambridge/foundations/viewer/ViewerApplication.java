package ulcambridge.foundations.viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ViewerApplication {

    public static void main(String[] args) {
        new SpringApplication(ViewerApplication.class)
            .run(args);
    }
}
