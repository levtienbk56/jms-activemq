package org.hedspi.gui;

import javax.jms.JMSException;

/**
 * All Actions of Controller. need to be implement
 * 
 * @author trungtran.vn
 *
 */
public interface Presenter {
	/**
	 * do login
	 * 
	 * @param username
	 * @param members
	 *            list member name, delimeter is comma ","
	 * @throws JMSException
	 */
	void login(String username, String members) throws JMSException;

	/**
	 * User type message from {@link MainFrame}, Controller handle to send it to
	 * Topic of ActiveMQ
	 * 
	 * @param msg
	 *            content
	 * @throws JMSException
	 */
	void sendMessage(String msg) throws JMSException;

	/**
	 * received new message from topic of ActiveMQ, Controller handle this
	 * message, then show it on view {@link MainFrame}
	 * 
	 * @param username
	 *            msg from who?
	 * @param msg
	 *            content
	 */
	void receiveMessage(String username, String msg);

	/**
	 * exception occured! Close connections and quit App
	 */
	void closeAndExit();

}
