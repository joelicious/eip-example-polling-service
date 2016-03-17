package org.jboss.fuse.service.polling.ftplistener;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FTPListenerTest extends CamelBlueprintTestSupport {

	protected static final String FTP_ROOT_DIR = "./target/ftp/home";
	protected static final String TEST_DATA_LOC = "./src/test/resources/test-data/ftpPollRecords-basicTest.txt";
	protected static final String DEFAULT_LISTENER = "default";

	protected static final File USERS_FILE = new File("./src/test/resources/users.properties");

	protected static FtpServer ftpServer;

	protected static BrokerService broker;

	protected boolean canTest;

	@Override
	@Before
	public void setUp() throws Exception {

		// Clean out FTP file system
		deleteDirectory(FTP_ROOT_DIR);

		canTest = false;

		// Call helper method that configures a FTP Server Factory
		FtpServerFactory factory = createFtpServerFactory();

		if (factory != null) {

			// Create a factory
			ftpServer = factory.createServer();

			if (ftpServer != null) {

				// Start the server
				ftpServer.start();
				canTest = true;

			}

		}

		// Embed a JMS Broker for testing
		broker = new BrokerService();
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:61615"));
		broker.setBrokerName("activemq");
		broker.addConnector(connector);
		broker.setPersistent(false);
		broker.start();

		// Creates the Blueprint Context
		super.setUp();

		// Place the test-data into the data/home/in directory.
		String msgContent = new String(Files.readAllBytes(Paths.get(TEST_DATA_LOC)));
		template.sendBodyAndHeader("file:target/ftp/home/in", msgContent, Exchange.FILE_NAME, "msg.txt");

	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		if (ftpServer != null) {
			try {

				ftpServer.stop();
				ftpServer = null;

			} catch (Exception e) {

				// ignore while shutting down as we could be
				// polling during shutdown and get errors
				// when the ftp server is stopping. This is only
				// an issue since we host the ftp server
				// embedded in the same jvm for unit testing.

			}
		}

		broker.stop();
		broker = null;
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/blueprint-polling-ftp.xml";
	}

	@Override
	protected String[] loadConfigAdminConfigurationFile() {
		return new String[] { "src/test/resources/etc/org.jboss.fuse.service.polling.ftplistener-testing.cfg",
				"org.jboss.fuse.service.polling.ftplistener" };
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {

				from("activemq:org.jboss.fuse.service.polling.processor.queue").id("ftplistenunittest")
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

	protected FtpServerFactory createFtpServerFactory() throws Exception {

		NativeFileSystemFactory fsf = new NativeFileSystemFactory();
		fsf.setCreateHome(true);

		PropertiesUserManagerFactory pumf = new PropertiesUserManagerFactory();
		pumf.setAdminName("admin");
		pumf.setPasswordEncryptor(new ClearTextPasswordEncryptor());
		pumf.setFile(USERS_FILE);
		UserManager userManager = pumf.createUserManager();

		ListenerFactory factory = new ListenerFactory();
		factory.setPort(14500);

		FtpServerFactory serverFactory = new FtpServerFactory();
		serverFactory.setUserManager(userManager);
		serverFactory.setFileSystem(fsf);
		serverFactory.setConnectionConfig(new ConnectionConfigFactory().createConnectionConfig());
		serverFactory.addListener(DEFAULT_LISTENER, factory.createListener());

		return serverFactory;

	}

}
