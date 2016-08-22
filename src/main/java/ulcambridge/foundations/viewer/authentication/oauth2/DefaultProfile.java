package ulcambridge.foundations.viewer.authentication.oauth2;

import org.springframework.util.Assert;

public class DefaultProfile implements Profile {
    private final String typeName, id, email;

    public DefaultProfile(String typeName, String id, String email) {
        Assert.hasText(typeName);
        Assert.hasText(id);
        Assert.hasText(email);

        this.typeName = typeName;
        this.id = id;
        this.email = email;
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getEmailAddress() {
        return this.email;
    }
}
