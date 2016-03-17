package org.jboss.fuse.service.polling.filelistener;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Before;
import org.junit.Test;

public class FileListenerTest extends CamelBlueprintTestSupport {

	protected static final String TEST_DATA_LOC = "./src/test/resources/test-data/filePollRecords-basicTest.txt";

	protected static BrokerService broker;

	@Override
	@Before
	public void setUp() throws Exception {

		// Embed a JMS Broker for testing
		broker = new BrokerService();
		final TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:61615"));
		broker.setBrokerName("activemq");
		broker.addConnector(connector);
		broker.setPersistent(false);
		broker.start();

		// Creates the Blueprint Context
		super.setUp();

		// Place the test-data into the data/in directory.
		final String msgContent = new String(Files.readAllBytes(Paths.get(TEST_DATA_LOC)));
		template.sendBodyAndHeader("file:data/in/flightSegment", msgContent, Exchange.FILE_NAME, "msg.txt");

	}

	@Override
	protected String getBlueprintDescriptor() {
		return "OSGI-INF/blueprint/blueprint-polling-file.xml";
	}

	@Override
	protected String[] loadConfigAdminConfigurationFile() {
		return new String[] { "src/test/resources/etc/org.jboss.fuse.service.polling.filelistener-testing.cfg",
				"org.jboss.fuse.service.polling.filelistener" };
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {

				from("activemq:org.jboss.fuse.service.polling.processor.queue").id("filelistenunittest")
						.to("file:data/out?fileName=record-${date:now:yyyyMMdd-hhmmssSSSS}").to("mock:result");

			}
		};
	}

	@Test
	public void testRoute() throws Exception {

		getMockEndpoint("mock:result").expectedMessageCount(6);

		// assert expectations
		assertMockEndpointsSatisfied();
	}

}
