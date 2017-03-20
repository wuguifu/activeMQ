package cn.com.active;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
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
		//MessageConsumer consumer = session.createConsumer(destination);
		String selector = "name = a or age < 18";
		//增加过滤器而且生产端 需要添加producer.setStringProperty();才能正常接收消息
		MessageConsumer consumer = session.createConsumer(destination, selector);
		//消息格式分为：TextMessage和MapMessage
		while(true){
			Message message = consumer.receive();
			if(message instanceof TextMessage){
				System.out.println(((TextMessage) message).getText());
			}else if(message instanceof MapMessage){
				MapMessage mapMessage = (MapMessage) message;
				System.out.println(mapMessage.getString("name"));
			}
		}
	}
}
