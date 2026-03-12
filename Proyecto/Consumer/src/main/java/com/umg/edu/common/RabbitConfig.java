package com.umg.edu.common;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitConfig {

   
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 5672;         // <- si tu producer usa 5672, aquí igual
    public static final String USER = "appuser";
    public static final String PASS = "apppass123";
    public static final String VHOST = "/";

    public static ConnectionFactory factory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USER);
        factory.setPassword(PASS);
        factory.setVirtualHost(VHOST);
        return factory;
    }
}