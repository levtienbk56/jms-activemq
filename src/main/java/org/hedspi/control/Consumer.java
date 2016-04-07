package org.hedspi.control;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.hedspi.model.Group;
import org.hedspi.model.User;

public class Consumer {
	private User from;
	private Group to;
	private String subject;
	private Destination destination;
	private MessageConsumer messageConsumer;

	public Consumer(Session session, String from, String to) {
		this.from = new User(from.trim(), "");
		this.to = new Group(to.trim());
		
		// is sum of hashcode of all username.
		this.subject = String.valueOf(this.from.getHashCode() + this.to.getHashCode());

		try {
			this.destination = session.createTopic(this.subject);
			this.messageConsumer = session.createConsumer(destination);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public MessageConsumer getMessageConsumer() {
		return this.messageConsumer;
	}

	public User getFrom() {
		return this.from;
	}

	public Group getTo() {
		return this.to;
	}
}
