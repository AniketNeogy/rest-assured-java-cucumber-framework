package com.petstore.api.client;

import com.petstore.api.constants.Endpoints;
import com.petstore.api.model.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Client for interacting with the User API endpoints
 */
@Slf4j
public class UserClient extends RestClient {
    
    /**
     * Create a new user
     * 
     * @param user User to create
     * @return Response containing the result of the operation
     */
    public Response createUser(User user) {
        log.info("Creating new user: {}", user);
        return post(Endpoints.USER, user);
    }
    
    /**
     * Create a list of users with given input array
     * 
     * @param users List of users to create
     * @return Response containing the result of the operation
     */
    public Response createUsersWithArray(User[] users) {
        log.info("Creating users with array. Count: {}", users.length);
        return post(Endpoints.CREATE_USERS_WITH_ARRAY, users);
    }
    
    /**
     * Create a list of users with given input list
     * 
     * @param users List of users to create
     * @return Response containing the result of the operation
     */
    public Response createUsersWithList(List<User> users) {
        log.info("Creating users with list. Count: {}", users.size());
        return post(Endpoints.CREATE_USERS_WITH_LIST, users);
    }
    
    /**
     * Get user by username
     * 
     * @param username The name that needs to be fetched
     * @return Response containing the user
     */
    public Response getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return RestAssured.given()
                .spec(spec)
                .pathParam("username", username)
                .when()
                .get(Endpoints.USER_BY_USERNAME);
    }
    
    /**
     * Update user
     * 
     * @param username Name that needs to be updated
     * @param user Updated user object
     * @return Response containing the result of the operation
     */
    public Response updateUser(String username, User user) {
        log.info("Updating user: {}", username);
        return RestAssured.given()
                .spec(spec)
                .pathParam("username", username)
                .body(user)
                .when()
                .put(Endpoints.USER_BY_USERNAME);
    }
    
    /**
     * Delete user
     * 
     * @param username The name that needs to be deleted
     * @return Response containing the result of the operation
     */
    public Response deleteUser(String username) {
        log.info("Deleting user: {}", username);
        return RestAssured.given()
                .spec(spec)
                .pathParam("username", username)
                .when()
                .delete(Endpoints.USER_BY_USERNAME);
    }
    
    /**
     * Logs user into the system
     * 
     * @param username The user name for login
     * @param password The password for login in clear text
     * @return Response containing the result of the operation
     */
    public Response loginUser(String username, String password) {
        log.info("Logging in user: {}", username);
        return RestAssured.given()
                .spec(spec)
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get(Endpoints.USER_LOGIN);
    }
    
    /**
     * Logs out current logged in user session
     * 
     * @return Response containing the result of the operation
     */
    public Response logoutUser() {
        log.info("Logging out current user");
        return get(Endpoints.USER_LOGOUT);
    }
} 