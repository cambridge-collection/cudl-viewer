package ulcambridge.foundations.viewer.authentication.oauth2;

import org.springframework.util.Assert;

import java.util.Optional;

public class DefaultProfile implements Profile {
    private final String typeName, id;
    private final Optional<String> email;

    public DefaultProfile(String typeName, String id, String email) {
        Assert.hasText(typeName);
        Assert.hasText(id);
        Assert.hasText(email);

        this.typeName = typeName;
        this.id = id;
        this.email = Optional.ofNullable(email);
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
    public Optional<String> getEmailAddress() {
        return this.email;
    }
}
