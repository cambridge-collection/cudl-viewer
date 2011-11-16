package ulcambridge.foundations.viewer.forms;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeedbackFormValidator implements Validator {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return FeedbackForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        FeedbackForm feedbackForm = (FeedbackForm) obj;
        if (feedbackForm.getName() == null || feedbackForm.getName().trim().isEmpty()) {
            errors.rejectValue("name", "error.feedback.name.not-specified", null, "Name required.");
        }
        if (feedbackForm.getEmail() == null || feedbackForm.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "error.feedback.email.not-specified", null, "Email Address required.");
        }
        if (feedbackForm.getComment() == null || feedbackForm.getComment().trim().isEmpty()) {
            errors.rejectValue("comment", "error.feedback.comment.not-specified", null, "Comment required.");
        }
    }

}