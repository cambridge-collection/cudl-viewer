package ulcambridge.foundations.viewer;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.forms.FeedbackForm;
import ulcambridge.foundations.viewer.forms.MailingListForm;
import ulcambridge.foundations.viewer.model.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class FormController {

    private static final String
            RECAPTCHA_PUBLIC_KEY = "6Lfp19cSAAAAACCDpTi_8O1znKcR8boRacNb0NjC",
            RECAPTCHA_PRIVATE_KEY = "6Lfp19cSAAAAACgXgRVTbk1m11OdFS8sttohEMDv";

    protected final Log logger = LogFactory.getLog(getClass());

    protected final static String feedbackEmail = Properties
            .getString("feedbackEmail");
    protected final static String feedbackSubject = Properties
            .getString("feedbackSubject");

    private ReCaptcha getReCaptcha() {
        return ReCaptchaFactory.newSecureReCaptcha(
                RECAPTCHA_PUBLIC_KEY, RECAPTCHA_PRIVATE_KEY, false);
    }

    private String getReCaptchaHtml() {
        return this.getReCaptcha().createRecaptchaHtml(null, null);
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
                .addObject("captchaHtml", this.getReCaptchaHtml())
                .addObject("errors", errors)
                .addObject("submissionFailed", submissionFailed);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/feedbackform.html")
    public ModelAndView showForm(FeedbackForm feedbackForm) {
        return this.getFeedbackResponse(feedbackForm);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/feedbackform.html")
    public ModelAndView processSubmit(@Valid FeedbackForm feedbackForm,
            BindingResult result, HttpServletRequest request)
            throws AddressException, MessagingException {

        // validation for standard parameters
        if (result.hasErrors()) {
            return this.getFeedbackResponse(feedbackForm, result);
        }

        // validation for capcha
        String remoteAddr = request.getRemoteAddr();

        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaResponse reCaptchaResponse = this.getReCaptcha()
                .checkAnswer(remoteAddr, challenge, uresponse);

        if (!reCaptchaResponse.isValid()) {
            ObjectError error = new ObjectError("FeedbackForm",
                    "The reCAPTCHA input was not correct, please try again");
            result.addError(error);

            return this.getFeedbackResponse(feedbackForm, result);
        }

        // send email with comment in.
        boolean success = EmailHelper.sendEmail(
                feedbackEmail,
                feedbackEmail,
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


        ModelAndView modelAndView = new ModelAndView("jsp/feedback-success");
        return modelAndView;

    }

}
