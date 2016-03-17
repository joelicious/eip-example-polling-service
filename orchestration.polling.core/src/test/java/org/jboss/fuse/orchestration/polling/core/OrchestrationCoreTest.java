package org.jboss.fuse.orchestration.polling.core;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrchestrationCoreTest extends CamelBlueprintTestSupport {

	protected static final String TEST_DATA_LOC = "./src/test/resources/test-data/flight-segment-record-1.xml";

	protected static BrokerService broker;

	protected static JdbcTemplate jdbc;

	protected boolean canTest;

	@Before
	@Override
	public void setUp() throws Exception {

		// Creates the Blueprint Context
		super.setUp();

		// Embed a JMS Broker for testing
		broker = new BrokerService();

		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:61615"));
		broker.setBrokerName("activemq");
		broker.addConnector(connector);
		broker.setPersistent(false);

		broker.start();

		org.apache.derby.jdbc.EmbeddedDataSource dataSource = context.getRegistry()
				.lookupByNameAndType("derbyDataSource", org.apache.derby.jdbc.EmbeddedDataSource.class);

		jdbc = new JdbcTemplate(dataSource);

		try {
			System.out.println("Creating Tables");
			jdbc.execute(
					"CREATE TABLE flight_segment_record (flight_segment_record_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY, transactionId VARCHAR (50), airline VARCHAR (50), airlineAccountId VARCHAR (50), airlineStatus VARCHAR (50), arrivalCode VARCHAR (50), arrivalDate VARCHAR (50), arrivalTime VARCHAR (50), associatedMonth VARCHAR (50))");
		} catch (Throwable e) {
			System.out.println("Unable to create table: " + e);
		}

		// Send the message
		String msgContent = new String(Files.readAllBytes(Paths.get(TEST_DATA_LOC)));
		template.sendBody("activemq:org.jboss.fuse.service.polling.processor.queue", msgContent);

	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/blueprint-orchestration-polling.xml,/OSGI-INF/blueprint/blueprint-orchestration-polling-testing.xml";
	}

	@Override
	protected String[] loadConfigAdminConfigurationFile() {
		return new String[] { "src/test/resources/etc/org.jboss.fuse.orchestration.polling.core-testing.cfg",
				"org.jboss.fuse.orchestration.polling.core" };
	}

	@Test
	public void testRoute() throws Exception {

		// wait 1 second for Route to start and execute
		Thread.sleep(10000);

		try {
			System.out.println("Query Tables");
			int total = jdbc.queryForObject("SELECT COUNT(*) FROM flight_segment_record", Integer.class);
			System.out.println("Count in Table: " + total);
		} catch (Throwable e) {
			System.out.println("Unable to create table: " + e);
		}

	}

}
