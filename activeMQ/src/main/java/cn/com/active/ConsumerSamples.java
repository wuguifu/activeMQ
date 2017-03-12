package cn.com.active;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumerSamples {
	public static void main(String[] args) throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER, 
				ActiveMQConnectionFactory.DEFAULT_PASSWORD, 
				"tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("first");
		MessageConsumer consumer = session.createConsumer(destination);
		while(true){
			TextMessage message = (TextMessage) consumer.receive();
			if(message == null){
				break;
			}
			System.out.println(message.getText());
		}
	}

}
