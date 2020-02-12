package ulcambridge.foundations.viewer.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class MailingListForm {

    @NotEmpty
    @Size(max = 100)
    private String name;

    @NotEmpty
    @Email
    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}