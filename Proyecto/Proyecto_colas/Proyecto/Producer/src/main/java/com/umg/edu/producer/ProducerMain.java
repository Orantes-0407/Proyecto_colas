package com.umg.edu.producer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.umg.producer.model.LoteTransacciones;
import com.umg.producer.model.Transaccion;

public class ProducerMain {

   
    private static final String API_URL =
            "https://hly784ig9d.execute-api.us-east-1.amazonaws.com/default/transacciones";

    private static final String RABBIT_HOST = "127.0.0.1";
    private static final int RABBIT_PORT = 5673;
    private static final String RABBIT_USER = "appuser";
    private static final String RABBIT_PASS = "apppass123";
    private static final String RABBIT_VHOST = "/";

    public static void main(String[] args) {

        try {

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("❌ Error al consumir API: " + response.statusCode());
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            LoteTransacciones lote =
                    mapper.readValue(response.body(), LoteTransacciones.class);

            System.out.println("✅ Lote recibido: " + lote.getLoteId());
            System.out.println("📦 Cantidad de transacciones: " + lote.getTransacciones().size());

    
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RABBIT_HOST);
            factory.setPort(RABBIT_PORT);
            factory.setUsername(RABBIT_USER);
            factory.setPassword(RABBIT_PASS);
            factory.setVirtualHost(RABBIT_VHOST);

            System.out.println("HOST: " + RABBIT_HOST);
            System.out.println("PORT: " + RABBIT_PORT);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                for (Transaccion tx : lote.getTransacciones()) {

                    String queueName = tx.getBancoDestino();

                    channel.queueDeclare(queueName, true, false, false, null);

                    String jsonTx = mapper.writeValueAsString(tx);

                    channel.basicPublish(
                            "",
                            queueName,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            jsonTx.getBytes(StandardCharsets.UTF_8)
                    );

                    System.out.println("📤 Enviada " + tx.getIdTransaccion() + " -> cola " + queueName);
                }

                System.out.println("✅ Todas las transacciones fueron enviadas");

            }

        } catch (Exception e) {
            System.out.println("❌ Error en Producer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}