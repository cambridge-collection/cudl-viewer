package ulcambridge.foundations.viewer.authentication;

import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.model.Properties;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = 985462454355609846L;

    private String password;
    private String username;
    private String email;
    private boolean enabled;
    private List<String> userRoles;

    public User(String username, String password, String email, boolean enabled, List<String> userRoles) {
        Assert.notNull(username);

        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.userRoles = userRoles;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public List<String> getUserRoles() {

        return this.userRoles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
