package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Lei
 * 
 */
public class Annotation extends Term {

	@SerializedName("target")
	private String target;
	
	@SerializedName("type")
	private String type;
	
	@SerializedName("page")
	private int page;
	
	@SerializedName("uuid")
	private UUID uuid;
	
	@SerializedName("date")
	private Date date;
	
	@SerializedName("position")
	private Position position;
	
	private Annotation() {}
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Annotation))
            return false;
        if (obj == this)
            return true;

        Annotation rhs = (Annotation) obj;
        
        // compare target, type and page number if it is 'doc'
        //
        if (rhs.getTarget().equals(this.getTarget()) && rhs.getTarget().equals("doc")) {
        	return new EqualsBuilder().
                append(page, rhs.page).
                append(target, rhs.target).
                append(type, rhs.type).
                isEquals();
        } 
        // otherwise (it is 'tag') compare name, target, type, name and position
        //
        else {
        	return new EqualsBuilder().
	            // if deriving: appendSuper(super.equals(obj)).
	            append(getName(), rhs.getName()).
	            append(page, rhs.getPage()).
	            append(target, rhs.getTarget()).
	            append(type, rhs.getType()).
	            append(position.getType(), rhs.getPosition().getType()).
	            append(position.getCoordinates(), rhs.getPosition().getCoordinates()).
	            isEquals();
        }
    }
    
    @Override
    public int hashCode() {
    	if (this.getTarget().equals("doc")) {
    		return new HashCodeBuilder(99, 101).
                    append(page).
                    append(target).
                    append(type).
                    toHashCode();
    	} else {
    		return new HashCodeBuilder(99, 101).
                    append(getName()).
                    append(page).
                    append(target).
                    append(type).
                    append(position.getType()).
                    toHashCode();
    	}  
	}

}
