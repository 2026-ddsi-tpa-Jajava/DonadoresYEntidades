package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncentivosApiClient {
    @Value("${incentivos.api.base-url}")
    private String BASE_URL;
    private final RestClientBuilder restClientBuilder = new RestClientBuilder(BASE_URL);

    public MisionDTO obtenerMisionActualDeDonador(String donadorID) {
            if (BASE_URL == null || BASE_URL.isBlank()) return null;

            String url = BASE_URL + "/misiones/" + donadorID;
            return restClientBuilder.get(url, MisionDTO.class);
    }

    public List<InsigniaDTO> obtenerInsigniasDeDonador(String donadorID) {
        if (BASE_URL == null || BASE_URL.isBlank()) return List.of();

        String url = BASE_URL + "/insignias/" + donadorID;
        return restClientBuilder.get(url, new ParameterizedTypeReference<>() {});
    }
}
