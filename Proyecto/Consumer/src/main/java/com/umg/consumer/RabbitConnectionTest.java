package com.umg.consumer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitConnectionTest {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5673);
            factory.setUsername("appuser");
            factory.setPassword("apppass123");
            factory.setVirtualHost("/");

            System.out.println("Probando conexión AMQP a 127.0.0.1:5673 con appuser...");

            try (Connection connection = factory.newConnection()) {
                System.out.println("✅ Conexión AMQP exitosa");
            }

        } catch (Exception e) {
            System.out.println("❌ Error de conexión AMQP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}