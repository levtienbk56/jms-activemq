package org.hedspi.demo;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {
	static String url = "tcp://localhost:61616";
	private static Scanner scanner;
	private static boolean exit = false;
	private static Connection connection;

	public static void main(String[] args) throws JMSException {
		// Getting JMS connection from the server and starting it
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		System.out.print("enter your name: ");
		scanner = new Scanner(System.in);
		String from = scanner.nextLine().trim();
		System.out.print("enter friend's name: ");
		String to = scanner.nextLine().trim();

		final Producer producer = new Producer(session, from, to);
		final Consumer consumer = new Consumer(session, from, to);

		// producer thread
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				try {
					while (!exit) {
						// scan message content
						String msg = scanner.nextLine();
						if (msg.toLowerCase().equals("exit")) {
							System.out.println("You entered exit!");
							exit = true;
							scanner.close();
							producer.send("left conversation!");
							break;
						}

						// send message
						producer.send(msg);
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		// consumer thread
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				try {
					while (!exit) {
						Message message = consumer.getMessageConsumer().receive();
						if (exit) {
							connection.close();
							break;
						}

						// there are so much messageType, need to be sure this
						// is a TextMessage
						if (message instanceof TextMessage) {
							TextMessage textMessage = (TextMessage) message;
							String from = textMessage.getStringProperty("from");

							System.out.println(from + ":\t" + textMessage.getText());
						}
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		// start thread
		thread1.start();
		thread2.start();
	}

}
