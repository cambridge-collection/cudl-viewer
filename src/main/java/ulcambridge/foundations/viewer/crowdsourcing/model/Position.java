package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Lei
 * 
 */
public class Position {

	@SerializedName("type")
	private String type;

	@SerializedName("coordinates")
	private List<Coordinates> coordinates = new ArrayList<Coordinates>();

	public Position() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Coordinates> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Format coordinates list to oa:FragmentSelector
	 * 
	 * @see <a href="http://www.openannotation.org/spec/core/specific.html#FragmentSelector">Fragment Selector</a>
	 * 
	 * @return
	 */
	public String formatCoordinatesToFragmentSelector() {
		if (coordinates.size() < 1) {
			return "0,0,0,0";
		} else if (coordinates.size() == 1) {
			return (int) coordinates.get(0).getX() + "," + (int) coordinates.get(0).getY() + ",1,1";
		} else { // size <= 5
			int w = (int) Math.abs(coordinates.get(2).getX() - coordinates.get(1).getX());
			int h = (int) Math.abs(coordinates.get(4).getY() - coordinates.get(1).getY());
			return (int) coordinates.get(0).getX() + "," + (int) coordinates.get(0).getY() + "," + w + "," + h;
		}
	}

}
