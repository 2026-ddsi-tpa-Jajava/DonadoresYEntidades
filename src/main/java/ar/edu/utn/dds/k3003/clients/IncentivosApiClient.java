package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncentivosApiClient {
    private final String BASE_URL = "https://incentivos-yuse.onrender.com";
    private final RestClientBuilder restClientBuilder = new RestClientBuilder(BASE_URL);

    public MisionDTO obtenerMisionActualDeDonador(String donadorID) {
            String url = BASE_URL + "/misiones/" + donadorID;
            return restClientBuilder.get(url, MisionDTO.class);
    }

    public List<InsigniaDTO> obtenerInsigniasDeDonador(String donadorID) {
        String url = BASE_URL + "/insignias/" + donadorID;
        return restClientBuilder.get(url, new ParameterizedTypeReference<>() {});
    }
}
