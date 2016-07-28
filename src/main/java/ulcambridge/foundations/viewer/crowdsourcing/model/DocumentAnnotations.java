package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Lei
 *
 */
@XmlRootElement(name = "documentAnnotations")
public class DocumentAnnotations extends DocumentTerms {

    @SerializedName("annotations")
    private final List<Annotation> annotations;

    public DocumentAnnotations() {
        annotations = new ArrayList<Annotation>();
    }

    public List<Annotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    @XmlElementWrapper(name = "annotations")
    @XmlElement(name = "annotation")
    public void setAnnotations(Collection<Annotation> annotations) {
        this.annotations.clear();
        this.annotations.addAll(annotations);
    }

    public List<Term> getTerms() {
        List<Term> terms = new ArrayList<Term>();
        for (Annotation annotation : annotations) {
            terms.add(new Term(annotation.getName(), annotation.getRaw(), annotation.getValue()));
        }
        return terms;
    }

}
