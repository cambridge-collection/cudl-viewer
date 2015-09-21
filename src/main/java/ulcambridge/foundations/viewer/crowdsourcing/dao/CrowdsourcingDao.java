package ulcambridge.foundations.viewer.crowdsourcing.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import ulcambridge.foundations.viewer.crowdsourcing.model.Annotation;
import ulcambridge.foundations.viewer.crowdsourcing.model.DocumentAnnotations;
import ulcambridge.foundations.viewer.crowdsourcing.model.DocumentTags;
import ulcambridge.foundations.viewer.crowdsourcing.model.Tag;
import ulcambridge.foundations.viewer.crowdsourcing.model.UserAnnotations;

/**
 * 
 * @author Lei
 *
 */
public interface CrowdsourcingDao {

	public DocumentAnnotations getAnnotations(String userId, String documentId, int documentPageNo);

	public DocumentAnnotations getAnnotationsByDocument(String documentId);

	public UserAnnotations getAnnotationsByUser(String userId);

	public DocumentTags getTagsByDocument(String documentId);

	public DocumentTags getRemovedTags(String userId, String documentId);

	public DocumentTags getRemovedTagsByDocument(String documentId);

	public int addAnnotation(String userId, String documentId, Annotation annotation) throws SQLException;

	public int addTag(String documentId, DocumentTags documentTags) throws SQLException;

	public int addRemovedTag(String userId, String documentId, Tag removedTag) throws SQLException;

	public int removeAnnotation(String userId, String documentId, UUID annotationUuid) throws SQLException;

	public List<String> getAnnotatedDocuments();

	public List<String> getTaggedDocuments();

}
