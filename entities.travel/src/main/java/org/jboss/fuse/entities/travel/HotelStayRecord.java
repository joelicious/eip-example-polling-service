package org.jboss.fuse.entities.travel;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hotelStayRecord")
public class HotelStayRecord {
	
	private String transactionId;
	private String personUUID;
	private String firstName;
	private String lastName;
	
	private String hotelBrand;
	private String hotelName;
	private String hotelGroup;
	private String hotelUUID;

	private String hotelMemberStatus;

	private String arrivalDate;
	private String departureDate;
	
	private String basePoints;
	private String welcomeBonus;
	private String hotelBonus;
	private String eliteBonus;
	
	private String hotelCost;
	private String hotelCostType;
	
	public static HotelStayRecord fromCSV(List<List<String>> csvData) {
		
		HotelStayRecord o = new HotelStayRecord();

		o.transactionId = csvData.get(0).get(0);
		o.personUUID = csvData.get(0).get(1);
		o.firstName = csvData.get(0).get(2);
		o.lastName = csvData.get(0).get(3);
		
		o.hotelBrand = csvData.get(0).get(4);
		o.hotelName = csvData.get(0).get(5);
		o.hotelGroup = csvData.get(0).get(6);
		o.hotelUUID = csvData.get(0).get(7);
		
		o.hotelMemberStatus = csvData.get(0).get(8);
		
		o.arrivalDate = csvData.get(0).get(9);
		o.departureDate = csvData.get(0).get(10);

		o.basePoints = csvData.get(0).get(11);
		o.welcomeBonus = csvData.get(0).get(12);
		o.hotelBonus = csvData.get(0).get(13);
		o.eliteBonus = csvData.get(0).get(14);

		o.hotelCost = csvData.get(0).get(15);
		o.hotelCostType = csvData.get(0).get(16);

		return o;
		
	}

	@XmlElement(name = "transactionId")
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@XmlElement(name = "personUUID")
	public String getPersonUUID() {
		return personUUID;
	}

	public void setPersonUUID(String personUUID) {
		this.personUUID = personUUID;
	}

	@XmlElement(name = "firstName")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement(name = "lastName")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	@XmlElement(name = "hotelMemberStatus")
	public String getHotelMemberStatus() {
		return hotelMemberStatus;
	}

	public void setHotelMemberStatus(String hotelMemberStatus) {
		this.hotelMemberStatus = hotelMemberStatus;
	}

	@XmlElement(name = "arrivalDate")
	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	@XmlElement(name = "departureDate")
	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	@XmlElement(name = "basePoints")
	public String getBasePoints() {
		return basePoints;
	}

	public void setBasePoints(String basePoints) {
		this.basePoints = basePoints;
	}

	@XmlElement(name = "welcomeBonus")
	public String getWelcomeBonus() {
		return welcomeBonus;
	}

	public void setWelcomeBonus(String welcomeBonus) {
		this.welcomeBonus = welcomeBonus;
	}

	@XmlElement(name = "hotelBonus")
	public String getHotelBonus() {
		return hotelBonus;
	}

	public void setHotelBonus(String hotelBonus) {
		this.hotelBonus = hotelBonus;
	}

	@XmlElement(name = "eliteBonus")
	public String getEliteBonus() {
		return eliteBonus;
	}

	public void setEliteBonus(String eliteBonus) {
		this.eliteBonus = eliteBonus;
	}

	@XmlElement(name = "hotelCost")
	public String getHotelCost() {
		return hotelCost;
	}

	public void setHotelCost(String hotelCost) {
		this.hotelCost = hotelCost;
	}

	@XmlElement(name = "hotelCostType")
	public String getHotelCostType() {
		return hotelCostType;
	}

	public void setHotelCostType(String hotelCostType) {
		this.hotelCostType = hotelCostType;
	}

}
