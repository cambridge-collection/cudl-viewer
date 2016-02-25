package ulcambridge.foundations.viewer.crowdsourcing;

import org.apache.jena.riot.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Handles requests for the crowdsourcing platform.
 * 
 * @author Lei
 * 
 */
@RestController
public class CrowdsourcingController {

	private static final Set<String> USER_ROLES = Collections.unmodifiableSet(
			new HashSet<String>(Arrays.asList("ROLE_USER", "ROLE_ADMIN")));

	private static final Logger logger = LoggerFactory.getLogger(CrowdsourcingController.class);

	private CrowdsourcingDao dataSource;

	private final Set<String> allowedRoles;

	public CrowdsourcingController() {
		this.allowedRoles = USER_ROLES;
	}

	public CrowdsourcingController(CrowdsourcingDao dataSource) {
		this();
		this.dataSource = dataSource;
	}

	private static final CacheControl CACHE_PRIVATE = CacheControl.noCache();
	private static final CacheControl CACHE_PUBLIC_INFREQUENTLY_CHANGING =
			CacheControl.empty()
				.cachePublic()
				.sMaxAge(30, TimeUnit.MINUTES);

	private boolean isUserAuthenticated() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth == null)
			return false;

		for(GrantedAuthority a : auth.getAuthorities()) {
			if(this.allowedRoles.contains(a.getAuthority()))
				return true;
		}

		return false;
	}

	private void ensureAuthenticated() {
		if(!isUserAuthenticated())
			throw new PermissionDeniedException();
	}

	private static class PermissionDeniedException extends RuntimeException { }

	@ExceptionHandler(PermissionDeniedException.class)
	private ResponseEntity<Void> handlePermissionDenied(PermissionDeniedException e ) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@ExceptionHandler(SQLException.class)
	private ResponseEntity<Void> handleSqlException(SQLException e) {
		logger.error("Database operation failed", e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.build();
	}

	private JsonResponse redirect() {
		JsonResponse resp = new JsonResponse("401", "User unauthenticated");
		resp.setRedirect(true);
		resp.setRedirectURL("/auth/login");
		return resp;
	}

	private String getCurrentUserId() {
		ensureAuthenticated();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	// on path /anno/get
	@RequestMapping(value = "/anno/get/{docId}/{docPage}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<JsonResponse> handleAnnotationsFetch(@PathVariable("docId") String documentId, @PathVariable("docPage") int documentPageNo,
											   HttpServletRequest req)
			throws IOException {

		ensureAuthenticated();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DocumentAnnotations docAnnotations = dataSource.getAnnotations(auth.getName(), documentId, documentPageNo);

		return ResponseEntity.status(HttpStatus.OK)
				.cacheControl(CACHE_PRIVATE)
				.body(new JsonResponse("200", "Annotations fetched", docAnnotations));
	}

	// on path /anno/update
	@RequestMapping(value = "/anno/update/{docId}/{docPage}", method = RequestMethod.POST, headers = { "Content-type=application/json" }, consumes = {
			"application/json" }, produces = { "application/json" })
	public JsonResponse handleAnnotationAddOrUpdate(@PathVariable("docId") String documentId, @PathVariable("docPage") int documentPageNo,
			@RequestBody Annotation annotation) throws SQLException, IOException {

		ensureAuthenticated();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int result = dataSource.addAnnotation(auth.getName(), documentId, annotation);
		return new JsonResponse("200", "Annotation added/updated");
	}

	@RequestMapping(value = "/anno/remove/{docId}/{uuid}",
					method = RequestMethod.DELETE)
	public ResponseEntity<Void> handleAnnotationRemove(
			@PathVariable("docId") String documentId,
			@PathVariable("uuid") UUID annotationId)
			throws SQLException, IOException {

		boolean removed = dataSource.removeAnnotation(getCurrentUserId(), documentId, annotationId);
		return ResponseEntity.status(removed ? HttpStatus.NO_CONTENT
											 : HttpStatus.NOT_FOUND)
				.build();
	}

	/**
	 * Delete the annotations created by the logged-in user with the specified
	 * IDs from a document.
     */
	@RequestMapping(value = "/anno/remove/{docId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<Set<UUID>> removeAnnotations(
			@PathVariable("docId") String documentId,
			@RequestParam("uuid") List<UUID> annotationIds)
			throws SQLException, IOException {

		Set<UUID> removed = dataSource.removeAnnotations(
				getCurrentUserId(), documentId, annotationIds);

		return ResponseEntity.ok().body(removed);
	}

	// on path /tag/get
	@RequestMapping(value = "/tag/get/{docId}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<JsonResponse> handleTagsFetch(@PathVariable("docId") String documentId) throws IOException {

		// combine tags with annotations and removed tags
		DocumentTags docTags = dataSource.getTagsByDocument(documentId);
		DocumentTags docRemovedTags = dataSource.getRemovedTagsByDocument(documentId);
		DocumentAnnotations docAnnotations = dataSource.getAnnotationsByDocument(documentId);

		DocumentTerms docARTTerms = new TermCombiner().combine_Anno_Tag_RemovedTag(docAnnotations, docTags, docRemovedTags);

		return ResponseEntity.ok()
				.cacheControl(CACHE_PUBLIC_INFREQUENTLY_CHANGING)
				.body(new JsonResponse("200", "Tags fetched", docARTTerms));
	}

	// on path /rmvtag/get
	@RequestMapping(value = "/rmvtag/get/{docId}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<JsonResponse> handleRemovedTagsFetch(@PathVariable("docId") String documentId) throws IOException {

		ensureAuthenticated();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DocumentTags docTags = dataSource.getRemovedTags(auth.getName(), documentId);

		return ResponseEntity.ok()
				.cacheControl(CACHE_PRIVATE)
				.body(new JsonResponse("200", "Removed tags fetched", docTags));
	}

	// on path /rmvtag/update
	@RequestMapping(value = "/rmvtag/update/{docId}", method = RequestMethod.POST, headers = { "Content-type=application/json" }, consumes = {
			"application/json" }, produces = { "application/json" })
	public JsonResponse handleRemovedTagAddOrUpdate(@PathVariable("docId") String documentId, @RequestBody Tag removedTag)
			throws SQLException, IOException {

		ensureAuthenticated();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int result = dataSource.addRemovedTag(auth.getName(), documentId, removedTag);
		return new JsonResponse("200", "Removed tag added/updated");
	}

	// on path /export
	@RequestMapping(value = "/export", method = RequestMethod.GET, produces = { "application/rdf+xml" })
	public void handleUserContributionsExport(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ensureAuthenticated();

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
		response.setHeader("Cache-Control", CACHE_PRIVATE.getHeaderValue());

		OutputStream os = response.getOutputStream();
		rr.getModel().write(os, RDFFormat.RDFXML.getLang().getName());
		response.flushBuffer();
		os.close();
	}

	// on path /export
	@RequestMapping(value = "/export/{docId}", method = RequestMethod.GET, produces = { "application/rdf+xml" })
	public void handleUserDocumentContributionsExport(@PathVariable("docId") String documentId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		ensureAuthenticated();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		DocumentAnnotations docAnnotations = dataSource.getAnnotations(auth.getName(), documentId, 0);

		String baseUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());

		RDFReader rr = new RDFReader(auth.getName(), baseUrl);

		for (Annotation annotation : docAnnotations.getAnnotations()) {
			rr.addElement(annotation, documentId);
		}

		// prepareResp(response, "application/rdf+xml; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + (documentId + "_" + Utils.getCurrentDateTime().toString()) + ".rdf");
		response.setHeader("Cache-Control", CACHE_PRIVATE.getHeaderValue());

		OutputStream os = response.getOutputStream();
		rr.getModel().write(os, RDFFormat.RDFXML.getLang().getName());
		response.flushBuffer();
		os.close();
	}
}
