package org.jboss.fuse.entities.travel;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "flightSegmentRecord")
public class FlightSegmentRecord {

	private String transactionId;
	private String personUUID;
	private String firstName;
	private String lastName;

	private String airline;
	private String airlineAccountId;
	private String airlineMemberStatus;

	private String departureCode;
	private String arrivalCode;

	private String departureDate;
	private String departureTime;

	private String arrivalDate;
	private String arrivalTime;

	private String flightNumber;
	private String ticketNumber;
	private String recordLocator;

	private String fareCode;

	private String associatedMonth;

	public static FlightSegmentRecord fromCSV(List<List<String>> csvData) {

		FlightSegmentRecord o = new FlightSegmentRecord();

		o.transactionId = csvData.get(0).get(0);
		o.personUUID = csvData.get(0).get(1);
		o.firstName = csvData.get(0).get(2);
		o.lastName = csvData.get(0).get(3);
		o.airline = csvData.get(0).get(4);
		o.airlineAccountId = csvData.get(0).get(5);
		o.airlineMemberStatus = csvData.get(0).get(6);

		o.departureCode = csvData.get(0).get(7);
		o.arrivalCode = csvData.get(0).get(8);

		o.departureDate = csvData.get(0).get(9);
		o.departureTime = csvData.get(0).get(10);

		o.arrivalDate = csvData.get(0).get(11);
		o.arrivalTime = csvData.get(0).get(12);

		o.flightNumber = csvData.get(0).get(13);
		o.ticketNumber = csvData.get(0).get(14);
		o.recordLocator = csvData.get(0).get(15);

		o.fareCode = csvData.get(0).get(16);

		o.associatedMonth = csvData.get(0).get(17);

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

	@XmlElement(name = "airline")
	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	@XmlElement(name = "airlineAccountId")
	public String getAirlineAccountId() {
		return airlineAccountId;
	}

	public void setAirlineAccountId(String airlineAccountId) {
		this.airlineAccountId = airlineAccountId;
	}

	@XmlElement(name = "airlineStatus")
	public String getAirlineMemberStatus() {
		return airlineMemberStatus;
	}

	public void setAirlineMemberStatus(String airlineMemberStatus) {
		this.airlineMemberStatus = airlineMemberStatus;
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

	@XmlElement(name = "departureDate")
	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	@XmlElement(name = "departureTime")
	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	@XmlElement(name = "arrivalDate")
	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	@XmlElement(name = "arrivalTime")
	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	@XmlElement(name = "flightNumber")
	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	@XmlElement(name = "ticketNumber")
	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	@XmlElement(name = "recordLocator")
	public String getRecordLocator() {
		return recordLocator;
	}

	public void setRecordLocator(String recordLocator) {
		this.recordLocator = recordLocator;
	}

	@XmlElement(name = "fareCode")
	public String getFareCode() {
		return fareCode;
	}

	public void setFareCode(String fareCode) {
		this.fareCode = fareCode;
	}

	@XmlElement(name = "associatedMonth")
	public String getAssociatedMonth() {
		return associatedMonth;
	}

	public void setAssociatedMonth(String associatedMonth) {
		this.associatedMonth = associatedMonth;
	}

}
