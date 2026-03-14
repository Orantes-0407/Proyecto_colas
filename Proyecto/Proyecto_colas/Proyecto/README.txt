LINK DEL VIDEO:https://drive.google.com/file/d/121FFHyWKaKtkjiwpa16KDJmy0e7UfgZY/view?usp=sharing



Procesamiento de Transacciones Bancarias con RabbitMQ y Java
Descripción del Proyecto
Este proyecto implementa un sistema distribuido para el procesamiento de transacciones bancarias utilizando RabbitMQ, Java y Maven siguiendo el patrón Producer–Consumer. El sistema obtiene un lote de transacciones desde una API externa, distribuye cada transacción a una cola según el banco destino y posteriormente un consumidor procesa cada mensaje enviándolo a otra API para su almacenamiento.
Arquitectura del Sistema
API GET /transacciones → Producer (Java) → RabbitMQ (colas por banco) → Consumer (Java) → API POST /guardarTransacciones
Tecnologías Utilizadas
Java 17, Maven, RabbitMQ, Docker, Jackson (JSON), Java HttpClient.
Componentes del Proyecto
1) Producer: consume el endpoint GET /transacciones, analiza el campo bancoDestino, crea una cola RabbitMQ por banco y publica cada transacción como JSON.
2) Consumer: escucha múltiples colas, recibe los mensajes, convierte el JSON a objeto Java, envía la transacción al endpoint POST y confirma el mensaje solo si el POST es exitoso.
API de Entrada (GET /transacciones)
Endpoint que devuelve un lote de transacciones con campos como idTransaccion, monto, moneda, cuentaOrigen, bancoDestino y detalle.
API de Salida (POST /guardarTransacciones)
Endpoint que recibe una transacción individual en formato JSON y la almacena en la base de datos.
Instalación de RabbitMQ
Ejecutar RabbitMQ con Docker:
docker run -d --name rabbitmq -p 5673:5672 -p 15672:15672 rabbitmq:3-management
Panel web disponible en http://localhost:15672
Creación de Usuario
docker exec -it rabbitmq rabbitmqctl add_user appuser apppass123
docker exec -it rabbitmq rabbitmqctl set_permissions -p / appuser ".*" ".*" ".*"
docker exec -it rabbitmq rabbitmqctl set_user_tags appuser administrator
Configuración de Conexión
HOST: 127.0.0.1
PORT: 5673
USER: appuser
PASSWORD: apppass123
VHOST: /
Ejecución del Proyecto
1. Iniciar RabbitMQ
2. Ejecutar ProducerMain para publicar transacciones
3. Verificar colas en RabbitMQ
4. Ejecutar ConsumerMain para consumir y enviar las transacciones al POST
Casos de Funcionamiento
• Si hay varios bancos se crean múltiples colas
• Cada banco procesa sus transacciones de forma independiente
• Si el POST falla el mensaje se reencola
• Si el POST es exitoso se hace ACK manual
• No se pierden transacciones
Flujo del Sistema
1. Producer consume la API GET
2. Se obtiene el lote de transacciones
3. Cada transacción se publica en RabbitMQ
4. RabbitMQ distribuye los mensajes por banco
5. Consumer escucha las colas
6. Consumer envía cada transacción al POST
7. Si el POST responde correctamente se hace ACK
8. Si falla se hace NACK y requeue
