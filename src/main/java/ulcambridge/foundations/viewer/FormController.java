package ulcambridge.foundations.viewer;


import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.components.Captcha;
import ulcambridge.foundations.viewer.components.EmailHelper;
import ulcambridge.foundations.viewer.forms.FeedbackForm;
import ulcambridge.foundations.viewer.forms.MailingListForm;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class FormController {
    private final String feedbackEmail;
    private final String feedbackSender;
    private final String feedbackSubject;
    private final Captcha captcha;
    private final EmailHelper emailHelper;

    public FormController(
        @Value("${feedbackEmail}") String feedbackEmail,
        @Value("${feedbackSender:#{null}}") Optional<String> feedbackSender,
        @Value("${feedbackSubject}") String feedbackSubject,
        @Autowired Captcha captcha,
        @Autowired EmailHelper emailHelper
    ) {
        this.feedbackEmail = feedbackEmail;
        this.feedbackSender = feedbackSender.orElse(feedbackEmail);
        this.feedbackSubject = feedbackSubject;
        this.captcha = captcha;
        this.emailHelper = emailHelper;
    }

    private ModelAndView getFeedbackResponse(FeedbackForm form) {
        return this.getFeedbackResponse(form, null);
    }

    private ModelAndView getFeedbackResponse(
            FeedbackForm form, BindingResult errors) {
        return this.getFeedbackResponse(form, errors, false);
    }

    private ModelAndView getFeedbackResponse(
            FeedbackForm form, BindingResult errors, boolean submissionFailed) {

        return new ModelAndView("jsp/feedback")
                .addObject("feedbackForm", form)
                .addObject("captchaHtml", this.captcha.getHtml())
                .addObject("errors", errors)
                .addObject("submissionFailed", submissionFailed);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/feedbackform.html")
    public ModelAndView showForm(FeedbackForm feedbackForm) {
        return this.getFeedbackResponse(feedbackForm);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/feedbackform.html")
    public ModelAndView processSubmit(
        @Valid FeedbackForm feedbackForm,
        BindingResult result,
        HttpServletRequest request
    ) throws MessagingException {

        // validation for standard parameters
        if (result.hasErrors()) {
            return this.getFeedbackResponse(feedbackForm, result);
        }

        // validation for capcha
        String remoteAddr = request.getRemoteAddr();
        String responseToken = request.getParameter(this.captcha.getResponseParam());
        if (!this.captcha.verify(responseToken, remoteAddr)) {
            ObjectError error = new ObjectError("FeedbackForm",
                    "The reCAPTCHA input was not correct, please try again");
            result.addError(error);
            return this.getFeedbackResponse(feedbackForm, result);
        }

        // send email with comment in.
        boolean success = emailHelper.sendEmail(
                feedbackEmail,
                feedbackSender,
                feedbackSubject,
                "Feedback from the user '" + feedbackForm.getName() + "' ("
                        + feedbackForm.getEmail() + "):\n\n"
                        + feedbackForm.getComment());

        if (success) {
            return new ModelAndView("jsp/feedback-success");
        } else {
            // Handle failure to submit feedback email by re-showing the form
            // with an error. This way the user doesn't loose their comments.
            return this.getFeedbackResponse(feedbackForm, result, true);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/mailinglistform.html")
    public ModelAndView showForm(MailingListForm mailingListForm) {
        ModelAndView modelAndView = new ModelAndView("jsp/mailinglist");
        modelAndView.addObject("feedbackForm", mailingListForm);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/mailinglistform.html")
    public ModelAndView processSubmit(@Valid MailingListForm mailingListForm,
            BindingResult result) throws EmailException {

        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("jsp/mailinglist");
            modelAndView.addObject("errors", result);
            return modelAndView;
        }

        // TODO sign up to mailing list.

        return new ModelAndView("jsp/feedback-success");
    }

}
