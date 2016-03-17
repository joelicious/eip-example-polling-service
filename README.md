# Polling Service
A project that demonstrates the file/ftp/sftp/jms/jdbc/email Camel Endpoints.  It also includes unit testing that provisions embedded ftp/sftp/jms/jdbc/mail servers.

1. entities.travel : a module that contains Java Objects with XML Bind Annotations. These objects are used to marshal and unmarshal data between Java and XML.
2. services.polling.emaillistener : a module that listens to an Email Account and will pass the attachments of an email to the Orchestration layer. It contains a unit test that embeds a GreenMail server and a JMS broker in order to test the route.
3. services.polling.filelistener : a module that watches a file system location for CSV files. It contains a unit test that embeds a JMS broker in order to test the route’s functionality.
4. services.polling.ftplistener : a module that monitors a FTP location for CSV files. It contains a unit test that embeds a FTP server and a JMS broker in order to test the route’s functionality.
5. services.polling.sftplistener : a module that monitors a SFTP location for CSV files. It contains a unit test that embeds a SFTP server in order to test the route’s functionality.
6. orchestration.polling.core : a module that monitors a JMS location for XML Documents and then persists the data to a database. It contains a unit test that embeds a JMS broker and a Derby Database in order to test the route’s functionality.
7. features.polling : a module that creates a Karaf Feature file for easier deployments to JBoss Fuse.


To install the application, perform the following:

karaf@root> features:addurl mvn:org.jboss.fuse/features.polling/0.0.1-SNAPSHOT/xml/features

karaf@root> features:install app-polling
