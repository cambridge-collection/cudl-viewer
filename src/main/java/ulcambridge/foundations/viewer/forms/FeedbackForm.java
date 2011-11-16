package ulcambridge.foundations.viewer.forms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeedbackForm {

    protected final Log logger = LogFactory.getLog(getClass());

    private String name;
    private String email;
    private String comment;

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
    
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }    
}