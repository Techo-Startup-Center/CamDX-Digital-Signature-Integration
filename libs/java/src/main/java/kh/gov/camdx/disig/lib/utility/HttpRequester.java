package kh.gov.camdx.disig.lib.utility;

import java.util.HashMap;
import java.util.Map;

import kh.gov.camdx.disig.lib.exceptions.DisigException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class HttpRequester {
    public static Map<String,Object> requestToServer(String url, HttpMethod method, String requestContent) throws DisigException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json");
        ResponseEntity<HashMap> responseEntity = WebClient.builder().build()
                .method(method)
                .uri(url)
                .headers(x->x.addAll(httpHeaders))
                .body(Mono.just(requestContent),String.class)
                .retrieve()
                .toEntity(HashMap.class)
                .block();
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String,Object> responseBody = responseEntity.getBody();
            if (responseBody == null)
                throw new DisigException("Expected non null response, except got null", 500);
            if (responseBody.containsKey("error") && (int)responseBody.get("error") != 0)
                throw new DisigException(responseBody.get("message").toString(),(int)responseBody.get("error"));
            return responseEntity.getBody();
        }
        throw new DisigException("All response should have HTTP status 200 : " + responseEntity.getStatusCode() + " -> " + responseEntity.getBody(),responseEntity.getStatusCode().value());
    }
}
