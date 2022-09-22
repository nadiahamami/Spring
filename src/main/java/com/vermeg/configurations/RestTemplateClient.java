package com.vermeg.configurations;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;

@Component
public class RestTemplateClient {

    @Value("${jira.username}")
    private String username;

    @Value("${jira.password}")
    private String password;

    @Value("${jira.uri}")
    private String uri;

    private JsonNodeFactory jnf;
    private HttpHeaders headers ;


    private final RestTemplate restTemplate;

    public RestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.jnf = JsonNodeFactory.instance;
        this.headers = new HttpHeaders();
    }


    public JsonObject getResponseRestTemplate(String resourceUrl, HttpMethod httpMethod ) {
        headers.setBasicAuth(username, password);
        HttpEntity<String> request = new HttpEntity(headers);
        return  responseRestTemplate(resourceUrl , httpMethod , request);

    }

   public JsonObject  postResponseRestTemplate(String resourceUrl, HttpMethod httpMethod , List<String> searchBy){
       headers.setBasicAuth(username, password);
       headers.setContentType(MediaType.APPLICATION_JSON);
       ObjectNode payload = jnf.objectNode();
       payload(payload , searchBy) ;
       HttpEntity<String> request = new HttpEntity(payload ,headers);

       return  responseRestTemplate(resourceUrl , httpMethod , request);

   }

   private JsonObject responseRestTemplate(String resourceUrl , HttpMethod httpMethod , HttpEntity<?> request) {

       try {
           ResponseEntity<String> response = restTemplate.exchange(uri + resourceUrl,
                   httpMethod, request, String.class);

           if (response.hasBody()) {
               JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
               return jsonObject;
           }
           return null;
       }catch (Exception ex){
           throw new RuntimeException(ex.getMessage());
       }
   }
    private void payload(ObjectNode payload, List<String> searchBy) {
        ArrayNode fields = payload.putArray("fields");
        payload.put("startAt", 0);
        payload.put("maxResults", 10000);
        for (String field : searchBy)
            fields.add(field);
    }


}
