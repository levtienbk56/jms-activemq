package org.hedspi.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
	// Địa chỉ máy cài ActiveMQ với cổng mặc định là 61616
	private static String url = "tcp://localhost:61616";
	// Tên Topic
	private static String subjectDefault = "HEDSPI";

	private String from = "clientA";
	private String to = "clientB";
	private String subject = String.valueOf(from.hashCode() + to.hashCode());
	private Destination destination;
	private MessageConsumer messageConsumer;

	public Consumer(Session session, String from, String to) {
		this.from = from.trim();
		this.to = to.trim();
		this.subject = String.valueOf(from.hashCode() + to.hashCode());

		try {
			this.destination = session.createTopic(this.subject);
			this.messageConsumer = session.createConsumer(destination);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MessageConsumer getMessageConsumer() {
		return this.messageConsumer;
	}

	public String getFrom() {
		return this.from;
	}

	public String getTo() {
		return this.to;
	}

	public static void main(String[] args) throws JMSException {

		// Getting JMS connection from the server
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

		Connection connection = connectionFactory.createConnection();

		connection.start();

		// Creating session for seding messages
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Getting the queue ‘TESTQUEUE’
		Destination destination = session.createQueue(subjectDefault);

		// MessageConsumer is used for receiving (consuming) messages
		MessageConsumer consumer = session.createConsumer(destination);

		while (true) {
			Message message = consumer.receive();

			// There are many types of Message and TextMessage
			// is just one of them. Producer sent us a TextMessage
			// so we must cast to it to get access to its .getText()
			// method.
			if (message instanceof TextMessage) {

				TextMessage textMessage = (TextMessage) message;
				String from = textMessage.getStringProperty("from");
				String to = textMessage.getStringProperty("to");

				System.out.println(from + " to " + to + ":" + textMessage.getText());

			}

		}
		// connection.close();
	}
}
