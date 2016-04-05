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
	// Địa chỉ máy cài ActiveMQ với cổng mặc định là 61616
	static String url = "tcp://localhost:61616";
	private static Scanner scanner;

	public static void main(String[] args) throws JMSException {
		// Getting JMS connection from the server and starting it
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
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
					while (true) {
						String msg = scanner.nextLine();
						if (msg.toLowerCase().equals("exit")) {
							System.out.println("You entered exit!");
							scanner.close();
							break;
						}

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
					while (true) {
						Message message = consumer.getMessageConsumer().receive();

						if (message instanceof TextMessage) {
							TextMessage textMessage = (TextMessage) message;
							String from = textMessage.getStringProperty("from");
							String to = textMessage.getStringProperty("to");
							
							if(from.equals(consumer.getFrom())){
								System.out.println(from + ":\t" + textMessage.getText());
							}else if(to.equals(consumer.getFrom())){
								System.out.println(from + ":\t" + textMessage.getText());
							}
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
