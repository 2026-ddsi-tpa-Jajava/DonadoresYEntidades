package ar.edu.utn.dds.k3003.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class Publisher {
    public static final String DONADOR_ACTUALIZADO = "DONADOR_ACTUALIZADO";
    public static final String ENTIDAD_ACTUALIZADA = "ENTIDAD_ACTUALIZADA";
    public static final String NECESIDAD_ACTUALIZADA = "NECESIDAD_ACTUALIZADA";
    public static final String NECESIDAD_ELIMINADA = "NECESIDAD_ELIMINADA";
    public static final String QUEJA_REGISTRADA = "QUEJA_REGISTRADA";

    private final RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(String tipo, Object payload) {
        Map<String, Object> event = Map.of("id", UUID.randomUUID().toString(), "tipo", tipo, "version", 1L, "ocurridoEn", Instant.now().toString(), "payload", payload);
        rabbitTemplate.convertAndSend("consultas.queue", event);
    }
}
