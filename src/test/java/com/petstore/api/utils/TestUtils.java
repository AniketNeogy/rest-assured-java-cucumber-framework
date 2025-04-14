package com.petstore.api.utils;

import com.github.javafaker.Faker;
import com.petstore.api.model.*;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Utility class for test helpers and verification methods
 */
@Slf4j
public class TestUtils {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000; // 2 seconds
    
    /**
     * Verify that a response has a successful status code (2xx)
     * 
     * @param response The response to verify
     */
    public static void verifySuccessStatusCode(Response response) {
        int statusCode = response.getStatusCode();
        log.info("Verifying successful status code. Actual: {}", statusCode);
        Assertions.assertThat(statusCode)
                .as("Response status code should be in the 2xx range")
                .isBetween(200, 299);
    }
    
    /**
     * Verify that a response has the expected status code
     * 
     * @param response The response to verify
     * @param expectedStatusCode The expected status code
     */
    public static void verifyStatusCode(Response response, int expectedStatusCode) {
        int statusCode = response.getStatusCode();
        log.info("Verifying status code. Expected: {}, Actual: {}", expectedStatusCode, statusCode);
        Assertions.assertThat(statusCode)
                .as("Response status code should be " + expectedStatusCode)
                .isEqualTo(expectedStatusCode);
    }
    
    /**
     * Execute an operation with retries if it returns a 404 response
     * 
     * @param operation The operation to execute
     * @return The successful response
     */
    public static Response executeWithRetry(Supplier<Response> operation) {
        Response response = null;
        boolean success = false;
        int attempts = 0;
        
        while (!success && attempts < MAX_RETRIES) {
            attempts++;
            response = operation.get();
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                success = true;
            } else if (response.getStatusCode() == 404) {
                log.info("Received 404 response, retrying... (Attempt {}/{})", attempts, MAX_RETRIES);
                if (attempts < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("Interrupted during retry delay", e);
                    }
                }
            } else {
                // If it's not a 404, don't retry
                break;
            }
        }
        
        return response;
    }
    
    /**
     * Generate a random pet for testing
     * 
     * @return A random pet
     */
    public static Pet generateRandomPet() {
        Category category = Category.builder()
                .id(random.nextLong(1000))
                .name(faker.animal().name())
                .build();
        
        List<Tag> tags = new ArrayList<>();
        tags.add(Tag.builder()
                .id(random.nextLong(1000))
                .name(faker.lorem().word())
                .build());
        
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("https://example.com/" + faker.lorem().word() + ".jpg");
        
        return Pet.builder()
                .id(random.nextLong(10000))
                .name(faker.animal().name())
                .category(category)
                .photoUrls(photoUrls)
                .tags(tags)
                .status("available")
                .build();
    }
    
    /**
     * Generate a random order for testing
     * 
     * @return A random order
     */
    public static Order generateRandomOrder() {
        return Order.builder()
                .id(random.nextLong(10000))
                .petId(random.nextLong(10000))
                .quantity(random.nextInt(10) + 1)
                .shipDate("2023-01-01T12:00:00.000Z")
                .status("placed")
                .complete(false)
                .build();
    }
    
    /**
     * Generate a random user for testing
     * 
     * @return A random user
     */
    public static User generateRandomUser() {
        return User.builder()
                .id(random.nextLong(10000))
                .username(faker.name().username())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .phone(faker.phoneNumber().phoneNumber())
                .userStatus(0)
                .build();
    }
} 