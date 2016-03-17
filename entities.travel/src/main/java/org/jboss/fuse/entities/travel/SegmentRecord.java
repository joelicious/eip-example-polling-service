package org.jboss.fuse.entities.travel;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "segmentRecord")
public class SegmentRecord {
	
	private String departureCode;
	private String arrivalCode;
	private String distance;
	
	public static SegmentRecord fromCSV(List<List<String>> csvData) {

		SegmentRecord o = new SegmentRecord();
		o.departureCode = csvData.get(0).get(1);
		o.arrivalCode = csvData.get(0).get(2);
		o.distance = csvData.get(0).get(3);
		
		return o;
		
	}

	@XmlElement(name = "departureCode")
	public String getDepartureCode() {
		return departureCode;
	}

	public void setDepartureCode(String departureCode) {
		this.departureCode = departureCode;
	}

	@XmlElement(name = "arrivalCode")
	public String getArrivalCode() {
		return arrivalCode;
	}

	public void setArrivalCode(String arrivalCode) {
		this.arrivalCode = arrivalCode;
	}

	@XmlElement(name = "distance")
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}
