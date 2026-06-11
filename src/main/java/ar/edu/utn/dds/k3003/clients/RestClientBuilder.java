package ar.edu.utn.dds.k3003.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;


public class RestClientBuilder {

    public RestClient restClient;

    public RestClientBuilder(String baseURL) {
        restClient = RestClient.builder()
                .baseUrl(baseURL)
                .build();
    }

    public <T> T get(String uri, Class<T> clazz) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(clazz);
    }

    public <T> T get(String uri, ParameterizedTypeReference<T> typeReference) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(typeReference); // RestClient ya soporta esto nativamente
    }
}
