package ulcambridge.foundations.viewer.crowdsourcing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author Lei
 *
 */
public class Term {

    @JsonProperty("name")
    private String name;

    @JsonProperty("raw")
    private int raw;

    @JsonProperty("value")
    private double value;

    public Term() {
    }

    public Term(String name, int raw, double value) {
        this.name = name;
        this.raw = raw;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Term))
            return false;
        if (obj == this)
            return true;

        Term rhs = (Term) obj;

        return new EqualsBuilder().append(name, rhs.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(99, 97).append(name).toHashCode();
    }

}
