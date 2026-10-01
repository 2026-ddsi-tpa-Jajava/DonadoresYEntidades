package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncentivosApiClient {
    @Value("${incentivos.api.base-url}")
    private String BASE_URL;
    private RestClientBuilder restClientBuilder;

    @PostConstruct
    void init() {
        restClientBuilder = new RestClientBuilder(BASE_URL == null ? "" : BASE_URL);
    }

    public MisionDTO obtenerMisionActualDeDonador(String donadorID) {
            if (BASE_URL == null || BASE_URL.isBlank()) return null;

            String url = "/misiones/" + donadorID;
            return restClientBuilder.get(url, MisionDTO.class);
    }

    public List<InsigniaDTO> obtenerInsigniasDeDonador(String donadorID) {
        if (BASE_URL == null || BASE_URL.isBlank()) return List.of();

        String url = "/insignias/" + donadorID;
        return restClientBuilder.get(url, new ParameterizedTypeReference<>() {});
    }
}
