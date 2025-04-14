package com.petstore.api.client;

import com.petstore.api.constants.Endpoints;
import com.petstore.api.model.Order;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;


/**
 * Client for interacting with the Store API endpoints
 */
@Slf4j
public class StoreClient extends RestClient {
    
    /**
     * Returns pet inventories by status
     * 
     * @return Response containing inventory data
     */
    public Response getInventory() {
        log.info("Getting pet inventories by status");
        return get(Endpoints.STORE_INVENTORY);
    }
    
    /**
     * Place an order for a pet
     * 
     * @param order Order to place
     * @return Response containing the placed order
     */
    public Response placeOrder(Order order) {
        log.info("Placing new order: {}", order);
        return post(Endpoints.STORE_ORDER, order);
    }
    
    /**
     * Find purchase order by ID
     * 
     * @param orderId ID of order to fetch
     * @return Response containing the order
     */
    public Response getOrderById(Long orderId) {
        log.info("Getting order by ID: {}", orderId);
        return RestAssured.given()
                .spec(spec)
                .pathParam("orderId", orderId)
                .when()
                .get(Endpoints.STORE_ORDER_BY_ID);
    }
    
    /**
     * Delete purchase order by ID
     * 
     * @param orderId ID of order to delete
     * @return Response containing the result of the deletion
     */
    public Response deleteOrder(Long orderId) {
        log.info("Deleting order with ID: {}", orderId);
        return RestAssured.given()
                .spec(spec)
                .pathParam("orderId", orderId)
                .when()
                .delete(Endpoints.STORE_ORDER_BY_ID);
    }
} 