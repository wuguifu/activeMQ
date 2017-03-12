package cn.com.active;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * http://localhost:8161/admin/
 * 一，建立connectionFactory对象 ，需要传入ip,用户名,密码,端口，使用默认即可。默认地址为"http://localhost:61616
 * 二，通过connectionFactory建立Connection连接，并用调用start()方法开启连接
 * 三。通过Connection对象创建session对话（上下文环境对象），用于接收消息，参数配置，启用事务
 * 四，通过session对象创建Destination对象。用于指定生产消息来源和消费消息目标
 * 五，通过session对象创建生产和消费消息对象（MessageProducer/MessageConsumer
 * 六.使用MessageProducer。setDeliveryMode方法设置持久化持久化特性和非持久化特性
 * 七，使用JMS规范的TextMessage形式创建数据，并用MessageProducer的send()方法发送数据）
 * @author WGF
 *五种不同的正文消息格式 ：
 *简单文本(TextMessage)、可序列化的对象 (ObjectMessage)、
 *属性集合 (MapMessage)、字节流 (BytesMessage)、
 *原始值流 (StreamMessage)，还有无有效负载的消息 (Message)。
 *
 *四种签收模式
 *AUTO_ACKNOWLEDGE = 1    自动确认
 *CLIENT_ACKNOWLEDGE = 2    客户端手动确认   consumer端必须调用签收的方法message.acknowledge()才能签收数据
 *DUPS_OK_ACKNOWLEDGE = 3    自动批量确认    consumer端会引起消息的重复，但降低了session的开销
 *SESSION_TRANSACTED = 0    事务提交并确认
 *
 */
public class ProducerSamples {
	public static void main(String[] args) throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER, 
				ActiveMQConnectionFactory.DEFAULT_PASSWORD, 
				"tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		//session不配置事务
		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);//配置事务
		//session配置事务
//		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("first");
		MessageProducer producer = session.createProducer(destination);
		//持久化DeliveryMode.PERSISTENT
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		for (int i = 0; i < 10; i++) {
			TextMessage message = session.createTextMessage();
			message.setText("我是第："+i+"个的内容 ！！");
			producer.send(message);
			System.out.println(message.toString());
		}
		//session.commit();//配置事务后需要使用commit方法才能提交到QUEUE
		if(null != connection){
			connection.close();
		}
	}
	
	

}
