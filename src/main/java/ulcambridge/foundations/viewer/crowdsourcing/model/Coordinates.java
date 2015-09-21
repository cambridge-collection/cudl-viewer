package ulcambridge.foundations.viewer.crowdsourcing.model;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Lei
 * 
 */
public class Coordinates {

	@SerializedName("x")
	private double x;

	@SerializedName("y")
	private double y;

	public Coordinates() {
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
