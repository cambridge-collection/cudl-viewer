package ulcambridge.foundations.viewer.crowdsourcing;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.riot.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ulcambridge.foundations.viewer.crowdsourcing.dao.CrowdsourcingDao;
import ulcambridge.foundations.viewer.crowdsourcing.model.Annotation;
import ulcambridge.foundations.viewer.crowdsourcing.model.DocumentAnnotations;
import ulcambridge.foundations.viewer.crowdsourcing.model.DocumentTags;
import ulcambridge.foundations.viewer.crowdsourcing.model.DocumentTerms;
import ulcambridge.foundations.viewer.crowdsourcing.model.JsonResponse;
import ulcambridge.foundations.viewer.crowdsourcing.model.Tag;
import ulcambridge.foundations.viewer.crowdsourcing.model.TermCombiner;
import ulcambridge.foundations.viewer.crowdsourcing.model.UserAnnotations;
import ulcambridge.foundations.viewer.rdf.RDFReader;
import ulcambridge.foundations.viewer.utils.Utils;

/**
 * Handles requests for the crowdsourcing platform.
 * 
 * @author Lei
 * 
 */
@RestController
public class CrowdsourcingController {

	private static final Logger logger = LoggerFactory.getLogger(CrowdsourcingController.class);

	private CrowdsourcingDao dataSource;

	public CrowdsourcingController(CrowdsourcingDao dataSource) {
		this.dataSource = dataSource;
	}

	private boolean isUserAuthed() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return !auth.getName().equals("anonymousUser");
	}

	private JsonResponse redirect() {
		JsonResponse resp = new JsonResponse("401", "User unauthenticated");
		resp.setRedirect(true);
		resp.setRedirectURL("/auth/login");
		return resp;
	}

	// on path /anno/get
	@RequestMapping(value = "/anno/get/{docId}/{docPage}", method = RequestMethod.GET, produces = { "application/json" })
	public JsonResponse handleAnnotationsFetch(@PathVariable("docId") String documentId, @PathVariable("docPage") int documentPageNo)
			throws IOException {

		// check user autentication
		if (!isUserAuthed())
			return redirect();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DocumentAnnotations docAnnotations = dataSource.getAnnotations(auth.getName(), documentId, documentPageNo);
		return new JsonResponse("200", "Annotations fetched", docAnnotations);
	}

	// on path /anno/update
	@RequestMapping(value = "/anno/update/{docId}/{docPage}", method = RequestMethod.POST, headers = { "Content-type=application/json" }, consumes = {
			"application/json" }, produces = { "application/json" })
	public JsonResponse handleAnnotationAddOrUpdate(@PathVariable("docId") String documentId, @PathVariable("docPage") int documentPageNo,
			@RequestBody Annotation annotation) throws SQLException, IOException {

		// check user autentication
		if (!isUserAuthed())
			return redirect();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int result = dataSource.addAnnotation(auth.getName(), documentId, annotation);
		return new JsonResponse("200", "Annotation added/updated");
	}

	// on path /anno/remove
	@RequestMapping(value = "/anno/remove/{docId}/{uuid}", method = RequestMethod.POST, produces = { "application/json" })
	public JsonResponse handleAnnotationRemove(@PathVariable("docId") String documentId, @PathVariable("uuid") String annotationUuid)
			throws SQLException, IOException {

		// check user autentication
		if (!isUserAuthed())
			return redirect();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int result = dataSource.removeAnnotation(auth.getName(), documentId, UUID.fromString(annotationUuid));
		return new JsonResponse("200", "Annotation removed");
	}

	// on path /tag/get
	@RequestMapping(value = "/tag/get/{docId}", method = RequestMethod.GET, produces = { "application/json" })
	public JsonResponse handleTagsFetch(@PathVariable("docId") String documentId) throws IOException {

		// combine tags with annotations and removed tags
		DocumentTags docTags = dataSource.getTagsByDocument(documentId);
		DocumentTags docRemovedTags = dataSource.getRemovedTagsByDocument(documentId);
		DocumentAnnotations docAnnotations = dataSource.getAnnotationsByDocument(documentId);

		DocumentTerms docARTTerms = new TermCombiner().combine_Anno_Tag_RemovedTag(docAnnotations, docTags, docRemovedTags);

		return new JsonResponse("200", "Tags fetched", docARTTerms);
	}

	// on path /rmvtag/get
	@RequestMapping(value = "/rmvtag/get/{docId}", method = RequestMethod.GET, produces = { "application/json" })
	public JsonResponse handleRemovedTagsFetch(@PathVariable("docId") String documentId) throws IOException {

		// check user autentication
		if (!isUserAuthed())
			return redirect();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DocumentTags docTags = dataSource.getRemovedTags(auth.getName(), documentId);
		return new JsonResponse("200", "Removed tags fetched", docTags);
	}

	// on path /rmvtag/update
	@RequestMapping(value = "/rmvtag/update/{docId}", method = RequestMethod.POST, headers = { "Content-type=application/json" }, consumes = {
			"application/json" }, produces = { "application/json" })
	public JsonResponse handleRemovedTagAddOrUpdate(@PathVariable("docId") String documentId, @RequestBody Tag removedTag)
			throws SQLException, IOException {

		// check user autentication
		if (!isUserAuthed())
			return redirect();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int result = dataSource.addRemovedTag(auth.getName(), documentId, removedTag);
		return new JsonResponse("200", "Removed tag added/updated");
	}

	// on path /export
	@RequestMapping(value = "/export", method = RequestMethod.GET, produces = { "application/rdf+xml" })
	public void handleUserContributionsExport(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// check user autentication
		if (!isUserAuthed())
			return;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserAnnotations userAnnotations = dataSource.getAnnotationsByUser(auth.getName());

		String baseUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());

		RDFReader rr = new RDFReader(auth.getName(), baseUrl);

		for (DocumentAnnotations docAnnotations : userAnnotations.getDocumentAnnotations()) {
			String documentId = docAnnotations.getDocumentId();
			for (Annotation annotation : docAnnotations.getAnnotations()) {
				rr.addElement(annotation, documentId);
			}
		}

		response.setHeader("Content-Disposition", "attachment; filename=" + ("USER" + "_" + Utils.getCurrentDateTime().toString()) + ".rdf");

		OutputStream os = response.getOutputStream();
		rr.getModel().write(os, RDFFormat.RDFXML.getLang().getName());
		response.flushBuffer();
		os.close();
	}

	// on path /export
	@RequestMapping(value = "/export/{docId}", method = RequestMethod.GET, produces = { "application/rdf+xml" })
	public void handleUserDocumentContributionsExport(@PathVariable("docId") String documentId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (!isUserAuthed())
			return;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		DocumentAnnotations docAnnotations = dataSource.getAnnotations(auth.getName(), documentId, 0);

		String baseUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());

		RDFReader rr = new RDFReader(auth.getName(), baseUrl);

		for (Annotation annotation : docAnnotations.getAnnotations()) {
			rr.addElement(annotation, documentId);
		}

		// prepareResp(response, "application/rdf+xml; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + (documentId + "_" + Utils.getCurrentDateTime().toString()) + ".rdf");

		OutputStream os = response.getOutputStream();
		rr.getModel().write(os, RDFFormat.RDFXML.getLang().getName());
		response.flushBuffer();
		os.close();
	}

}
