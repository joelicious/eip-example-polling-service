package org.jboss.fuse.service.polling.emaillistener;

import com.consol.citrus.Citrus;
import com.consol.citrus.container.IteratingConditionExpression;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.design.DefaultTestDesigner;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.message.MessageType;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.*;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

public class EmailListenerTest extends CamelBlueprintTestSupport {

	private static final String USER_NAME = "accountone";
	private static final String USER_PASSWORD = "accountone";
	private static final String EMAIL_USER_ADDRESS = "accountone@localhost";

	private GreenMail mailServer;

	private Citrus citrus;
	private TestDesigner designer;

	@Override
	@Before
	public void setUp() throws Exception {
		// GreenMail sets Test Server up on Port 3110
		mailServer = new GreenMail(ServerSetupTest.SMTP_POP3);
		mailServer.start();

		mailServer.setUser(EMAIL_USER_ADDRESS, USER_NAME, USER_PASSWORD);

		citrus = Citrus.newInstance();
		designer = new DefaultTestDesigner(citrus.getApplicationContext(), citrus.createTestContext());

		// Creates the Blueprint Context
		super.setUp();

	}

	@Override
	@After
	public void tearDown() throws Exception {
		mailServer.stop();
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

	@Test
	public void testEmailRoute() throws Exception {
		designer.echo("testEmailRoute::start");

		// Show routes for information
		List<Route> routes = this.context().getRoutes();
		for (Route r : routes) {
			designer.echo("+Route: " + r.getId());
		}

		designer.camel()
				.context((ModelCamelContext) this.context())
				.controlBus()
				.route("poll.email.receive", "status")
				.result(ServiceStatus.Started);

		designer.echo("Delivering Email w/ Attachment");

		designer.send("mailClient")
				.payload(new ClassPathResource("test-data/send-mail.xml"));

		designer.iterate().condition(new IteratingConditionExpression() {
			@Override
			public boolean evaluate(int i, TestContext context) {
				return i <= 5;
			}
		}).actions(
			designer.receive("servicePollingJmsEndpoint")
					.messageType(MessageType.XML)
					.payload("citrus:readFile('test-data/flightSegmentRecord_${i}.xml')")
		);

		designer.echo("testDynamicEmailRoute::end");

		citrus.run(designer.getTestCase());
	}

}