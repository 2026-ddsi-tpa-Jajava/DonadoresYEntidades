package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncentivosApiClient {
    private final String BASE_URL = "https://incentivos-yuse.onrender.com";

    public MisionDTO obtenerMisionPorDonador(String donadorID) {
        try {
            String url = BASE_URL + "/misiones/" + donadorID;
            return HttpClientBuilder.get(url, MisionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener misión para el donador con ID: " + donadorID, e);
        }
    }

    public List<InsigniaDTO> obtenerInsigniaPorDonador(String donadorID) {
        try {
            String url = BASE_URL + "/insignias/" + donadorID;
            return HttpClientBuilder.get(url, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener insignia para el donador con ID: " + donadorID, e);
        }
    }
}
