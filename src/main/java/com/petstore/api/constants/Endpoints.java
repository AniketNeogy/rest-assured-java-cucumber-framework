package com.petstore.api.constants;

/**
 * Constants for Petstore API endpoints
 */
public class Endpoints {
    
    // Pet endpoints
    public static final String PET = "/pet";
    public static final String PET_BY_ID = "/pet/{petId}";
    public static final String FIND_PETS_BY_STATUS = "/pet/findByStatus";
    public static final String UPLOAD_PET_IMAGE = "/pet/{petId}/uploadImage";
    
    // Store endpoints
    public static final String STORE_INVENTORY = "/store/inventory";
    public static final String STORE_ORDER = "/store/order";
    public static final String STORE_ORDER_BY_ID = "/store/order/{orderId}";
    
    // User endpoints
    public static final String USER = "/user";
    public static final String USER_BY_USERNAME = "/user/{username}";
    public static final String USER_LOGIN = "/user/login";
    public static final String USER_LOGOUT = "/user/logout";
    public static final String CREATE_USERS_WITH_ARRAY = "/user/createWithArray";
    public static final String CREATE_USERS_WITH_LIST = "/user/createWithList";
} 