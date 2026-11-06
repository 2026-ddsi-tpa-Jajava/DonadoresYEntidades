package ar.edu.utn.dds.k3003.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.JacksonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue consultasQueue() {
        return new Queue("consultas.queue", true);
    }

    @Bean
    public MessageConverter messageConverter() {
        // Fechas como ISO-8601 ("2026-11-05") y no como arreglo ([2026,11,5])
        ObjectMapper mapper = JacksonUtils.enhancedObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return new Jackson2JsonMessageConverter(mapper);
    }
}