package Simulation.Listener;

import Dtos.TransLocationDto;
import Simulation.MessageProducer;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

public class SimulationListener {


    public SimulationListener(String serverAddress, String countryCode, String queue, ISimulationListener listener) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(serverAddress);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);

            Consumer simulationConsumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) {
                    String messageAsString = new String(body, MessageProducer.UTF8);
                    TransLocationDto dto = new Gson().fromJson(messageAsString, TransLocationDto.class);

                    if (countryCode.equals(dto.getCountryCode())) {
                        listener.onReceiveFromSimulation(dto);
                    }
                }
            };
            channel.basicConsume(queue, simulationConsumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
