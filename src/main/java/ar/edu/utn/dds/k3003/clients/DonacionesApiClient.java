package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DonacionesApiClient {

    @Value("${donaciones.api.base-url}")
    private String BASE_URL;
    private RestClientBuilder restClientBuilder;

    @PostConstruct
    void init() {
        restClientBuilder = new RestClientBuilder(BASE_URL == null ? "" : BASE_URL);
    }

    public boolean esProductoValido(String productoID) {
        if (BASE_URL == null || BASE_URL.isBlank()) return true;

        String url = "/productos/" + productoID + "/existencia";
        return restClientBuilder.get(url, Boolean.class);
    }
}
