package org.hedspi.gui;

import java.awt.EventQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.hedspi.control.Consumer;
import org.hedspi.control.Producer;

public class Controller implements Presenter, Runnable {
	public static final String URL = "tcp://localhost:61616";
	private MainFrame mainFrame;
	private LoginFrame loginFrame;
	private Producer producer;
	private Consumer consumer;

	public static void main(String args[]) {

		final Controller controller = new Controller();

		EventQueue.invokeLater(controller);
	}

	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			loginFrame = new LoginFrame(Controller.this);
			mainFrame = new MainFrame(Controller.this);
			mainFrame.setVisible(false);
			loginFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login(String username, String members) throws JMSException {
		if (username.equals("") || members.equals("")) {
			JOptionPane.showMessageDialog(loginFrame, "input empty");
		} else {
			// setting MainFrame
			mainFrame.setUserNameLabel("Welcome " + username);
			mainFrame.setGroupMember(members);
			mainFrame.setVisible(true);
			loginFrame.setVisible(false);

			// Getting JMS connection from the server and starting it
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// create producer
			producer = new Producer(session, username, members);

			// create consumer and looping to listening
			consumer = new Consumer(session, username, members);
			activeMessageListener();
		}
	}

	// let MessageListener run in single thread
	private void activeMessageListener() {
		new Thread(new Runnable() {

			public void run() {
				try {
					while (true) {
						Message message = consumer.getMessageConsumer().receive();

						// there are so much messageType, need to be sure this
						// is a TextMessage
						if (message instanceof TextMessage) {
							TextMessage textMessage = (TextMessage) message;
							String from = textMessage.getStringProperty("from");
							receiveMessage(from, textMessage.getText());
						}
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// request from view. to server
	public void sendMessage(String msg) throws JMSException {
		producer.send(msg);
		System.out.println("send: " + msg);
	}

	// received message from server, now append on view
	public void receiveMessage(String username, String msg) {
		mainFrame.appendReceivedMessage(username, msg);
		System.out.println("received: " + username + "," + msg);
	}

	public void closeAndExit() {
		// System.exit(0);
	}

}
