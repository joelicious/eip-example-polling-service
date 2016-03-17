package org.jboss.fuse.service.polling.emaillistener;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

public class EmailListenerTest extends CamelBlueprintTestSupport {

	private static final Logger LOG = LoggerFactory.getLogger(EmailListenerTest.class);
	
	protected static BrokerService broker;

	protected static final String ATTACHMENT_DIR_STRING = "./src/test/resources/test-data/";
	protected static final File ATTACHMENT_FILE = new File(ATTACHMENT_DIR_STRING + "emailPollRecords-basicTest.txt");

	private static final String USER_NAME = "accountone";
	private static final String USER_PASSWORD = "accountone";
	private static final String EMAIL_USER_ADDRESS = "accountone@localhost";
	private static final String EMAIL_TO = "accountone@127.0.0.1";
	private static final String EMAIL_SUBJECT = "Test E-Mail";

	private GreenMail mailServer;

	private GreenMailUser user;

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

		// GreenMail sets Test Server up on Port 3110
		mailServer = new GreenMail(ServerSetupTest.POP3);
		mailServer.start();

		user = mailServer.setUser(EMAIL_USER_ADDRESS, USER_NAME, USER_PASSWORD);

		// Creates the Blueprint Context
		super.setUp();

	}

	@Override
	@After
	public void tearDown() throws Exception {
		mailServer.stop();
		
		broker.stop();
		broker = null;
		
		super.tearDown();
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/blueprint-polling-email.xml";
	}

	@Override
	protected String[] loadConfigAdminConfigurationFile() {
		return new String[] { "src/test/resources/etc/org.jboss.fuse.service.polling.emaillistener-testing.cfg",
				"org.jboss.fuse.service.polling.emaillistener" };
	}

	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {

			public void configure() {
				from("activemq:org.jboss.fuse.service.polling.processor.queue").id("emaillistenunittest")
						.to("file:data/out?fileName=record-${date:now:yyyyMMdd-hhmmssSSSS}").to("mock:result");
			}

		};
	}

	@Test
	public void testEmailRoute() throws Exception {

		LOG.info("testEmailRoute::start");

		// wait 1 second for Listener Route to start
		Thread.sleep(1000);

		ServiceStatus routeStatus = this.context.getRouteStatus("poll.email.receive");

		// Show routes for information
		List<Route> routes = this.context().getRoutes();
		for (Route r : routes) {
			LOG.info("+Route: " + r.getId());
		}

		if (null != routeStatus) {

			assertTrue("Route was not created", routeStatus.isStarted());

			MimeMessage message = new MimeMessage((Session) null);
			message.setFrom(new InternetAddress(EMAIL_TO));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(EMAIL_USER_ADDRESS));
			message.setSubject(EMAIL_SUBJECT);
			message.setSentDate(Calendar.getInstance().getTime());

			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setText("This is the email body text");

			MimeBodyPart attachmentPart = new MimeBodyPart();
			DataSource source = new FileDataSource(ATTACHMENT_FILE);
			attachmentPart.setDataHandler(new DataHandler(source));
			attachmentPart.setFileName("emailPollRecords-basicTest.txt");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPart);
			multipart.addBodyPart(attachmentPart);

			message.setContent(multipart);

			LOG.info("Delivering Email w/ Attachment");

			user.deliver(message);

			// wait 2 seconds for route to start and process
			Thread.sleep(2000);

			getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

			// assert expectations
			assertMockEndpointsSatisfied();

		} else {
			assertFalse("RouteStatus is null", true);
		}

		LOG.info("testDynamicEmailRoute::end");

	}

}