package com.scheideggergroup.core.model;

/**
 * Class responsible to store Google Encoded Polylines.
 * @author Vini
 *
 */
public class Polyline {

	private String encodedPolyline;

	/**
	 * Creates a new instance of Polyline.
	 * @param encodedPolyline the encoded polyline.
	 */
	public Polyline() {
		super();
	}

	/**
	 * Gets the encoded polyline.
	 * @return a string containing the encoded polyline.
	 */
	public String getEncodedPolyline() {
		return encodedPolyline;
	}

	/**
	 * Sets the encoded polyline.
	 * @param encodedPolyline a string containing the encoded polyline.
	 */
	public void setEncodedPolyline(String encodedPolyline) {
		this.encodedPolyline = encodedPolyline;
	}
}
