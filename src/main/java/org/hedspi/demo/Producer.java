package org.hedspi.demo;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
	// Địa chỉ máy cài ActiveMQ với cổng mặc định là 61616
	private static String url = "tcp://localhost:61616";

	private static String subjectDefault = "HEDSPI";
	private String from = "clientA";
	private String to = "clientB";
	private String subject = String.valueOf(from.hashCode() + to.hashCode());
	private Session session;
	private Destination destination;
	private MessageProducer messageProducer;

	public Producer(Session session, String from, String to) {
		this.from = from.trim();
		this.to = to.trim();
		this.subject = String.valueOf(from.hashCode() + to.hashCode());

		try {
			this.session = session;
			this.destination = session.createTopic(this.subject);
			this.messageProducer = session.createProducer(destination);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String msg) throws JMSException {
		TextMessage message = session.createTextMessage(msg);
		message.setStringProperty("from", this.from);
		message.setStringProperty("to", this.to);

		// Here we are sending the message!
		messageProducer.send(message);

	}

	public static void main(String[] args) throws JMSException {
		// Getting JMS connection from the server and starting it
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();

		// JMS messages are sent and received using a Session. We will
		// create here a non-transactional session object. If you want
		// to use transactions you should set the first parameter to ‘true’
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Destination represents here our queue ‘TESTQUEUE’ on the
		// JMS server. You don’t have to do anything special on the
		// server to create it, it will be created automatically.
		Destination destination = session.createQueue(subjectDefault);

		// MessageProducer is used for sending messages (as opposed
		// to MessageConsumer which is used for receiving them)
		MessageProducer producer = session.createProducer(destination);

		int msgTemp = 0;

		Scanner scanner = new Scanner(System.in);
		while (true) {
			msgTemp++;
			String msg = scanner.nextLine();
			if (msg.toLowerCase().equals("exit")) {
				System.out.println("You entered exit!");
				break;
			}

			System.out.println("Msg No " + msgTemp + ":\t" + msg);

			TextMessage message = session.createTextMessage(msg);
			message.setStringProperty("from", "clientA");
			message.setStringProperty("to", "clientB");

			// Here we are sending the message!
			producer.send(message);

			// Delay 1s
			// Thread.sleep(1000);
		}
		scanner.close();
	}
}
