package org.hedspi.demo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.hedspi.model.Group;
import org.hedspi.model.User;

public class Producer {
	private User from;
	private Group to;
	private String subject;
	private Session session;
	private Destination destination;
	private MessageProducer messageProducer;

	public Producer(Session session, String from, String to) {
		this.from = new User(from.trim(), "");
		this.to = new Group(to);
		
		// is sum of hashcode of all username.
		this.subject = String.valueOf(this.from.getHashCode() + this.to.getHashCode());

		try {
			this.session = session;
			this.destination = session.createTopic(this.subject);
			this.messageProducer = session.createProducer(destination);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg) throws JMSException {
		TextMessage message = session.createTextMessage(msg);
		message.setStringProperty("from", this.from.getUsername());

		// Here we are sending the message!
		messageProducer.send(message);
	}

	public User getFrom() {
		return this.from;
	}

	public Group getTo() {
		return this.to;
	}

}
