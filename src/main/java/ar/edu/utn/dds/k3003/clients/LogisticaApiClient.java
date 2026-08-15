package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.logistica.AsignacionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LogisticaApiClient {

    @Value("${logistica.api.base-url}")
    private String BASE_URL;
    private final RestClientBuilder restClientBuilder = new RestClientBuilder(BASE_URL);

    public int cuantoStockHayDe(String productoID) {
        if (BASE_URL == null || BASE_URL.isBlank()) return 0;

        String url = BASE_URL + "/depositos/stock/" + productoID;
        return restClientBuilder.get(url, Integer.class);
    }

    public AsignacionDTO crearAsignacionStock(String necesidadID, String productoID, int cantidad) {
        if (BASE_URL == null || BASE_URL.isBlank()) return null;

        String url = BASE_URL + "/asignaciones/stock";

        Map<String, String> request = new HashMap<>();
        request.put("necesidadID", necesidadID);
        request.put("productoID", productoID);
        request.put("cantidadAsignada", String.valueOf(cantidad));

        return restClientBuilder.post(url, request, AsignacionDTO.class);
    }
}
