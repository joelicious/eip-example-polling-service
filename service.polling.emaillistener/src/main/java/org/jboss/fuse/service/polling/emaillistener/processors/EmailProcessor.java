package org.jboss.fuse.service.polling.emaillistener.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.jboss.fuse.service.polling.emaillistener.entities.FilenameAndContents;
import org.jboss.fuse.service.polling.emaillistener.exception.EmailProcessorException;

public class EmailProcessor implements Processor {

	private static final Logger LOG = Logger.getLogger(EmailProcessor.class);

	public void process(Exchange exchange) throws Exception {

		LOG.info("Entering EmailProcessor...");

		Map<String, DataHandler> attachments = exchange.getIn().getAttachments();

		if ((null == attachments) || (attachments.size() < 1)) {
			throw new EmailProcessorException("Null or 0 attachements");
		} else {
			LOG.info("attachments.size = " + attachments.size());
		}

		List<String> attachmentFilenames = new ArrayList<String>();

		try {

			List<FilenameAndContents> attachmentArray = new ArrayList<FilenameAndContents>();

			for (String name : attachments.keySet()) {

				DataHandler dh = attachments.get(name);

				// get the file name
				String filename = dh.getName();

				// convert the input string to a string
				String contents = exchange.getContext().getTypeConverter().convertTo(String.class, dh.getInputStream());

				LOG.info("filename: " + filename);
				LOG.info("contents: " + contents);

				attachmentFilenames.add(filename);

				FilenameAndContents attachmentFile = new FilenameAndContents();
				attachmentFile.setFileContents(contents);
				attachmentFile.setFileName(filename);
				attachmentArray.add(attachmentFile);

			}

			exchange.getIn().setBody(attachmentArray);

		} catch (org.apache.camel.TypeConversionException tce) {
			throw new EmailProcessorException("Unable to type convert from file to string", tce);
		} catch (java.io.IOException ioe) {
			throw new EmailProcessorException("IOException while obtaining Input Stream", ioe);
		} catch (java.lang.UnsupportedOperationException uoe) {
			throw new EmailProcessorException("UnsupportedOperationException add operation is not supported by list",
					uoe);
		} catch (java.lang.ClassCastException cce) {
			throw new EmailProcessorException("ClassCastException element prevents it from being added to list", cce);
		} catch (java.lang.NullPointerException npe) {
			throw new EmailProcessorException("NullPointerException element is null", npe);
		} catch (java.lang.IllegalArgumentException iae) {
			throw new EmailProcessorException(
					"IllegalArgumentException property of element prevents it from being added to list", iae);
		}

		LOG.info("Exiting EmailProcessor.");

	}

}