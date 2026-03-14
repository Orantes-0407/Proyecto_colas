package com.umg.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import com.umg.edu.consumer.model.Transaccion;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsumerMain {

    private static final String RABBIT_HOST = "127.0.0.1";
    private static final int RABBIT_PORT = 5673;
    private static final String RABBIT_USER = "appuser";
    private static final String RABBIT_PASS = "apppass123";
    private static final String RABBIT_VHOST = "/";

    private static final String POST_URL =
            "https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones";

    // Colas a escuchar
    private static final String[] QUEUES = {"BAC", "BANRURAL", "BI", "GYT"};

    public static void main(String[] args) throws Exception {

        System.out.println("Conectando a " + RABBIT_HOST + ":" + RABBIT_PORT + " user=" + RABBIT_USER);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBIT_HOST);
        factory.setPort(RABBIT_PORT);
        factory.setUsername(RABBIT_USER);
        factory.setPassword(RABBIT_PASS);
        factory.setVirtualHost(RABBIT_VHOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Procesar un mensaje a la vez
        channel.basicQos(1);

        ObjectMapper mapper = new ObjectMapper();
        HttpClient httpClient = HttpClient.newHttpClient();

        System.out.println("✅ Consumer conectado a RabbitMQ");
        System.out.println("👂 Escuchando colas...");

        for (String queueName : QUEUES) {

            channel.queueDeclare(queueName, true, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                long deliveryTag = delivery.getEnvelope().getDeliveryTag();
                String body = new String(delivery.getBody(), StandardCharsets.UTF_8);

                try {
                    System.out.println("\n📥 Mensaje recibido de cola: " + queueName);

                    // JSON -> objeto
                    Transaccion tx = mapper.readValue(body, Transaccion.class);

                    // Enviar al POST con reintento mínimo 1
                    boolean ok = enviarPostConReintento(httpClient, mapper, tx);

                    if (ok) {
                        channel.basicAck(deliveryTag, false);
                        System.out.println("✅ POST OK + ACK: " + tx.getIdTransaccion());
                    } else {
                        channel.basicNack(deliveryTag, false, true);
                        System.out.println("❌ POST falló 2 veces -> NACK con requeue: " + tx.getIdTransaccion());
                    }

                } catch (Exception e) {
                    channel.basicNack(deliveryTag, false, true);
                    System.out.println("❌ Error procesando mensaje -> NACK con requeue");
                    e.printStackTrace();
                }
            };

            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
            System.out.println("👂 Escuchando cola: " + queueName);
        }
    }

    private static boolean enviarPostConReintento(HttpClient httpClient, ObjectMapper mapper, Transaccion tx) {
        if (enviarPost(httpClient, mapper, tx)) {
            return true;
        }

        System.out.println("🔁 Reintentando POST para: " + tx.getIdTransaccion());
        return enviarPost(httpClient, mapper, tx);
    }

    private static boolean enviarPost(HttpClient httpClient, ObjectMapper mapper, Transaccion tx) {
        try {
            String jsonBody = mapper.writeValueAsString(tx);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(POST_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status == 200 || status == 201) {
                return true;
            } else {
                System.out.println("❌ POST HTTP " + status + " -> " + response.body());
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ Error POST: " + e.getMessage());
            return false;
        }
    }
}