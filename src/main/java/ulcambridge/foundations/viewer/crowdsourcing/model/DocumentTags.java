package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Lei
 *
 */
@XmlRootElement(name = "documentTags")
@JsonIgnoreProperties({"terms"})
public class DocumentTags extends DocumentTerms {

    @JsonProperty("tags")
    private List<Tag> tags;

    public DocumentTags() {
    }

    public List<Tag> getTags() {
        return tags;
    }

    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Term> getTerms() {
        List<Term> terms = new ArrayList<Term>();
        for (Tag tag : tags) {
            terms.add(new Term(tag.getName(), tag.getRaw(), tag.getValue()));
        }
        return terms;
    }

}
