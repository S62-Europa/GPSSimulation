package Simulation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Gateway {
    private ConnectionFactory factory;
    public Connection connection;
    public Channel channel;

    public Gateway() throws Exception {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }
}
