# Microservicio Consumidor de Mensajes

Este microservicio se encarga exclusivamente del consumo de mensajes desde RabbitMQ.

## Tecnologías
- Spring Boot 3.3.13
- Java 21
- Spring AMQP (RabbitMQ)
- Lombok
- Maven

## Endpoints

### GET /api/mensaje/ultimo
Obtiene el último mensaje disponible en la cola de RabbitMQ.

## Características
- Listener con reconocimiento manual (ACK manual)
- Procesamiento de mensajes con delay de 10 segundos
- Manejo de errores con NACK

## Puerto
El microservicio corre en el puerto 8082.
