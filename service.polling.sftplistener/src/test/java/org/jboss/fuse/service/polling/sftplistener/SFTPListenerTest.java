package org.jboss.fuse.service.polling.sftplistener;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Arrays;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SFTPListenerTest extends CamelBlueprintTestSupport {

	protected static final String SFTP_ROOT_DIR = "/target/sftp/home/in";
	protected static final String TEST_DATA_LOC = "./src/test/resources/test-data/sftpPollRecords-basicTest.txt";
	protected static final String DEFAULT_LISTENER = "default";

	protected int sftpPort = 14501;

	protected static SshServer sshd;

	protected static BrokerService broker;

	protected boolean canTest;

	@Override
	@Before
	public void setUp() throws Exception {

		deleteDirectory("." + SFTP_ROOT_DIR);
		createDirectory("." + SFTP_ROOT_DIR);

		setUpSFTPServer();

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

		// Place the test-data into the data/in directory.
		String msgContent = new String(Files.readAllBytes(Paths.get(TEST_DATA_LOC)));
		template.sendBodyAndHeader(
				"sftp://admin@localhost:" + sftpPort + SFTP_ROOT_DIR
						+ "?password=admin&delay=10s&disconnect=true&stepwise=false",
				msgContent, Exchange.FILE_NAME, "msg.txt");

	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();

		broker.stop();
		broker = null;

		if (sshd != null) {
			try {
				// stop asap as we may hang forever
				sshd.stop(true);
				sshd = null;

			} catch (Exception e) {
				// ignore while shutting down as we could be
				// polling during shutdown and get errors when
				// the sftp server is stopping. This is only
				// an issue since we host the ftp server
				// embedded in the same jvm for unit testing
			}
		}

	}

	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/blueprint-polling-sftp.xml";
	}

	@Override
	protected String[] loadConfigAdminConfigurationFile() {
		return new String[] { "src/test/resources/etc/org.jboss.fuse.service.polling.sftplistener-testing.cfg",
				"org.jboss.fuse.service.polling.sftplistener" };
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {

				from("activemq:org.jboss.fuse.service.polling.processor.queue").id("sftplistenunittest")
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

	protected void setUpSFTPServer() throws Exception {
		canTest = true;

		try {
			sshd = SshServer.setUpDefaultServer();
			sshd.setPort(sftpPort);
			sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("target/key.ser"));

			sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>> asList(new SftpSubsystem.Factory()));

			sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
				public boolean authenticate(String username, String password, ServerSession session) {
					return true;
				}
			});

			sshd.setCommandFactory(new ScpCommandFactory());

			PublickeyAuthenticator publickeyAuthenticator = new PublickeyAuthenticator() {

				@Override
				public boolean authenticate(String username, PublicKey key, ServerSession session) {
					return true;
				}
			};

			sshd.setShellFactory(new ProcessShellFactory());

			sshd.setPublickeyAuthenticator(publickeyAuthenticator);
			sshd.start();

		} catch (Exception e) {

			canTest = false;
		}
	}
}
