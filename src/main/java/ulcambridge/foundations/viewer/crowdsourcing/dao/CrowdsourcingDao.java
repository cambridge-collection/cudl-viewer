package ulcambridge.foundations.viewer.crowdsourcing.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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

    public DocumentAnnotations getAnnotations(String userId, String documentId);

    public DocumentAnnotations getAnnotationsByDocument(String documentId);

    public UserAnnotations getAnnotationsByUser(String userId);

    public DocumentTags getTagsByDocument(String documentId);

    public DocumentTags getRemovedTags(String userId, String documentId);

    public DocumentTags getRemovedTagsByDocument(String documentId);

    public int addAnnotation(String userId, String documentId, Annotation annotation) throws SQLException;

    public int addTag(String documentId, DocumentTags documentTags) throws SQLException;

    public int addRemovedTag(String userId, String documentId, Tag removedTag) throws SQLException;

    public boolean removeAnnotation(String userId, String documentId, UUID annotationUuid) throws SQLException;

    /**
     * Remove multiple annotations owned by a single user from a document.
     *
     * @param userId The ID of the user whose annotations are to be removed
     * @param documentId The ID of the document from which annotations will be removed
     * @param annotationIds The IDs of the annotations to be removed
     * @return The set of annotation IDs that were removed
     * @throws SQLException
     */
    Set<UUID> removeAnnotations(String userId, String documentId, Collection<UUID> annotationIds) throws SQLException;

    public List<String> getAnnotatedDocuments();

    public List<String> getTaggedDocuments();

}
