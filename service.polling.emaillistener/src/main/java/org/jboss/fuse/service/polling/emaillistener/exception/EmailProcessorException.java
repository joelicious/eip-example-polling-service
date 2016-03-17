package org.jboss.fuse.service.polling.emaillistener.exception;

/***
 * EmailProcessorException is a generic Exception that can be raised from
 * EmailProcessor.
 * 
 * @author jbutler
 * 
 */
public class EmailProcessorException extends Exception {

	private static final long serialVersionUID = 2016031620151350L;

	public EmailProcessorException(String message) {
		super(message);
	}

	public EmailProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

}