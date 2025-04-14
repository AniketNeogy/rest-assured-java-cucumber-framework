package com.petstore.api.client;

import com.petstore.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * Base REST client that provides common functionality for API interactions
 */
@Slf4j
public class RestClient {
    protected final ApiConfig config;
    protected final RequestSpecification spec;

    public RestClient() {
        config = ApiConfig.getInstance();
        RestAssured.baseURI = config.getBaseUrl();
        
        // Configure REST Assured with proper connection management
        // Using simpler configuration to avoid HTTP client incompatibility issues
        RestAssuredConfig restAssuredConfig = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", config.getRequestTimeout())
                        .setParam("http.socket.timeout", config.getRequestTimeout())
                        .setParam("http.connection-manager.timeout", config.getRequestTimeout()));
        
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(restAssuredConfig);
        
        if (config.isLogAllRequests()) {
            builder.addFilter(new RequestLoggingFilter());
        }
        
        if (config.isLogAllResponses()) {
            builder.addFilter(new ResponseLoggingFilter());
        }
        
        spec = RestAssured.given().spec(builder.build());
    }
    
    /**
     * Performs a GET request
     *
     * @param endpoint The API endpoint
     * @return Response object
     */
    protected Response get(String endpoint) {
        log.debug("Sending GET request to: {}", endpoint);
        return spec.when().get(endpoint);
    }
    
    /**
     * Performs a GET request with path parameters
     *
     * @param endpoint The API endpoint
     * @param pathParams Path parameters
     * @return Response object
     */
    protected Response get(String endpoint, Object... pathParams) {
        log.debug("Sending GET request to: {} with path params: {}", endpoint, pathParams);
        return spec.when().get(endpoint, pathParams);
    }
    
    /**
     * Performs a POST request with a request body
     *
     * @param endpoint The API endpoint
     * @param requestBody The request body
     * @return Response object
     */
    protected Response post(String endpoint, Object requestBody) {
        log.debug("Sending POST request to: {}", endpoint);
        return spec.body(requestBody).when().post(endpoint);
    }
    
    /**
     * Performs a PUT request with a request body
     *
     * @param endpoint The API endpoint
     * @param requestBody The request body
     * @return Response object
     */
    protected Response put(String endpoint, Object requestBody) {
        log.debug("Sending PUT request to: {}", endpoint);
        return spec.body(requestBody).when().put(endpoint);
    }
    
    /**
     * Performs a DELETE request
     *
     * @param endpoint The API endpoint
     * @return Response object
     */
    protected Response delete(String endpoint) {
        log.debug("Sending DELETE request to: {}", endpoint);
        return spec.when().delete(endpoint);
    }
    
    /**
     * Performs a DELETE request with path parameters
     *
     * @param endpoint The API endpoint
     * @param pathParams Path parameters
     * @return Response object
     */
    protected Response delete(String endpoint, Object... pathParams) {
        log.debug("Sending DELETE request to: {} with path params: {}", endpoint, pathParams);
        return spec.when().delete(endpoint, pathParams);
    }
} 