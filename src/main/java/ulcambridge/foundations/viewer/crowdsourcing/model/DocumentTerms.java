package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Lei
 *
 */
@XmlRootElement(name = "documentTerms")
public class DocumentTerms {

    @JsonProperty("oid")
    private String userId;

    @JsonProperty("docId")
    private String documentId;

    @JsonProperty("total")
    private int total;

    @JsonProperty("terms")
    private List<Term> terms;

    public DocumentTerms() {
    }

    public DocumentTerms(String documentId, int total, List<Term> terms) {
        this.documentId = documentId;
        this.total = total;
        this.terms = terms;
    }

    public String getUserId() {
        return userId;
    }

    @XmlElement(name = "oid")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    @XmlElement(name = "docId")
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getTotal() {
        return total;
    }

    @XmlElement(name = "total")
    public void setTotal(int total) {
        this.total = total;
    }

    public List<Term> getTerms() {
        return terms;
    }

    @XmlElementWrapper(name = "terms")
    @XmlElement(name = "term")
    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    /**
     * it is actually convert the object into xml string
     */
    public String toJAXBString(DocumentTerms docTerms) {
        String output = "";

        try {
            // create JAXB context and instantiate marshaller
            JAXBContext context = JAXBContext.newInstance(DocumentTerms.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            m.marshal(docTerms, baos);

            output = baos.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return output;
    }

}
