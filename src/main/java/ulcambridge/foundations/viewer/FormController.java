package ulcambridge.foundations.viewer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptchaImpl;
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

@Controller
public class FormController {

	protected final Log logger = LogFactory.getLog(getClass());

	protected final static String feedbackEmail = Properties
			.getString("feedbackEmail");
	protected final static String feedbackSubject = Properties
			.getString("feedbackSubject");

	@RequestMapping(method = RequestMethod.GET, value = "/feedbackform.html")
	public ModelAndView showForm(FeedbackForm feedbackForm) {
		ModelAndView modelAndView = new ModelAndView("jsp/feedback");
		modelAndView.addObject("feedbackForm", feedbackForm);
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/feedbackform.html")
	public ModelAndView processSubmit(@Valid FeedbackForm feedbackForm,
			BindingResult result, HttpServletRequest request)
			throws AddressException, MessagingException {

		// validation for standard parameters
		if (result.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("jsp/feedback");
			modelAndView.addObject("errors", result);
			return modelAndView;
		}

		// validation for capcha
		String remoteAddr = request.getRemoteAddr();
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey("6Lfp19cSAAAAACgXgRVTbk1m11OdFS8sttohEMDv");

		String challenge = request.getParameter("recaptcha_challenge_field");
		String uresponse = request.getParameter("recaptcha_response_field");
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
				challenge, uresponse);

		if (!reCaptchaResponse.isValid()) {
			ObjectError error = new ObjectError("FeedbackForm",
					"The recaptcha input was not correct, please try again");
			result.addError(error);
			ModelAndView modelAndView = new ModelAndView("jsp/feedback");
			modelAndView.addObject("errors", result);
			return modelAndView;
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
			ModelAndView modelAndView = new ModelAndView("jsp/feedback-success");
			return modelAndView;
		} else {
			ModelAndView modelAndView = new ModelAndView("jsp/feedback-failure");
			return modelAndView;
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