package org.jboss.fuse.entities.travel;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hotelRecord")
public class HotelRecord {

	private String hotelBrand;
	private String hotelName;
	private String hotelGroup;
	private String hotelUUID;

	private String hotelCity;
	private String hotelProvinceOrState;
	private String hotelCountry;

	public static HotelRecord fromCSV(List<List<String>> csvData) {

		HotelRecord o = new HotelRecord();
		o.hotelBrand = csvData.get(0).get(1);
		o.hotelName = csvData.get(0).get(2);
		o.hotelGroup = csvData.get(0).get(3);
		o.hotelUUID = csvData.get(0).get(4);

		o.hotelCity = csvData.get(0).get(5);
		o.hotelProvinceOrState = csvData.get(0).get(6);
		o.hotelCountry = csvData.get(0).get(7);

		return o;

	}

	@XmlElement(name = "hotelBrand")
	public String getHotelBrand() {
		return hotelBrand;
	}

	public void setHotelBrand(String hotelBrand) {
		this.hotelBrand = hotelBrand;
	}

	@XmlElement(name = "hotelName")
	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	@XmlElement(name = "hotelGroup")
	public String getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	@XmlElement(name = "hotelUUID")
	public String getHotelUUID() {
		return hotelUUID;
	}

	public void setHotelUUID(String hotelUUID) {
		this.hotelUUID = hotelUUID;
	}

	@XmlElement(name = "hotelCity")
	public String getHotelCity() {
		return hotelCity;
	}

	public void setHotelCity(String hotelCity) {
		this.hotelCity = hotelCity;
	}

	@XmlElement(name = "hotelProvinceOrState")
	public String getHotelProvinceOrState() {
		return hotelProvinceOrState;
	}

	public void setHotelProvinceOrState(String hotelProvinceOrState) {
		this.hotelProvinceOrState = hotelProvinceOrState;
	}
	
	@XmlElement(name = "hotelCountry")
	public String getHotelCountry() {
		return hotelCountry;
	}

	public void setHotelCountry(String hotelCountry) {
		this.hotelCountry = hotelCountry;
	}

}
