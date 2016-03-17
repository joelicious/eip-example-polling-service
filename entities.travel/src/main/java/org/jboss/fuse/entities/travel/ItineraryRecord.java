package org.jboss.fuse.entities.travel;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itineraryRecord")
public class ItineraryRecord {
	
	private String ticketNumber;
	private String recordLocator;
	
	private String cost;
	private String costCategory;

	public static ItineraryRecord fromCSV(List<List<String>> csvData) {

		ItineraryRecord o = new ItineraryRecord();

		o.ticketNumber = csvData.get(0).get(0);
		o.recordLocator = csvData.get(0).get(1);
		o.cost = csvData.get(0).get(2);
		o.costCategory = csvData.get(0).get(3);

		return o;
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

	@XmlElement(name = "cost")
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	@XmlElement(name = "costCategory")
	public String getCostCategory() {
		return costCategory;
	}

	public void setCostCategory(String costCategory) {
		this.costCategory = costCategory;
	}
	
}
