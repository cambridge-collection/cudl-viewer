package ulcambridge.foundations.viewer;

import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.forms.FeedbackForm;

@Controller
public class FormController {

	protected final Log logger = LogFactory.getLog(getClass());
	protected final static ResourceBundle config = ResourceBundle
			.getBundle("cudl-global");
	protected final static String feedbackEmail = config
			.getString("feedbackEmail");
	protected final static String feedbackSubject = config
			.getString("feedbackSubject");
	protected final static String feedbackHost = config
			.getString("feedbackHost");

	@RequestMapping(method = RequestMethod.GET, value = "/feedbackform.html")
	public ModelAndView showForm(FeedbackForm feedbackForm) {
		ModelAndView modelAndView = new ModelAndView("jsp/feedback");
		modelAndView.addObject("feedbackForm", feedbackForm);
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/feedbackform.html")
	public ModelAndView processSubmit(@Valid FeedbackForm feedbackForm,
			BindingResult result, Map model) throws EmailException {

		if (result.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("jsp/feedback");
			modelAndView.addObject("errors", result);
			return modelAndView;
		}

		// send email with comment in.
		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(feedbackHost);
		email.addTo(feedbackEmail);
		email.setFrom(feedbackEmail);
		email.setSubject(feedbackSubject);
		email.setMsg("Feedback from the user '" + feedbackForm.getName()
				+ "' (" + feedbackForm.getEmail() + "):\n\n"
				+ feedbackForm.getComment());

		// send the email
		email.send();

		ModelAndView modelAndView = new ModelAndView("jsp/feedback-success");
		return modelAndView;

	}
}