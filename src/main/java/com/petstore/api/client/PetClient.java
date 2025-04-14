package com.petstore.api.client;

import com.petstore.api.constants.Endpoints;
import com.petstore.api.model.Pet;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Client for interacting with the Pet API endpoints
 */
@Slf4j
public class PetClient extends RestClient {

    /**
     * Create a new pet in the store
     * 
     * @param pet Pet object to create
     * @return Response containing the created pet
     */
    public Response createPet(Pet pet) {
        log.info("Creating new pet: {}", pet);
        return post(Endpoints.PET, pet);
    }
    
    /**
     * Update an existing pet
     * 
     * @param pet Pet object to update
     * @return Response containing the updated pet
     */
    public Response updatePet(Pet pet) {
        log.info("Updating pet with ID: {}", pet.getId());
        return put(Endpoints.PET, pet);
    }
    
    /**
     * Find pets by status
     * 
     * @param status Status values that need to be considered for filter
     * @return Response containing a list of pets
     */
    public Response findPetsByStatus(String status) {
        log.info("Finding pets by status: {}", status);
        return RestAssured.given()
                .spec(spec)
                .queryParam("status", status)
                .when()
                .get(Endpoints.FIND_PETS_BY_STATUS);
    }
    
    /**
     * Find pet by ID
     * 
     * @param petId ID of pet to return
     * @return Response containing the pet
     */
    public Response getPetById(Long petId) {
        log.info("Getting pet by ID: {}", petId);
        return RestAssured.given()
                .spec(spec)
                .pathParam("petId", petId)
                .when()
                .get(Endpoints.PET_BY_ID);
    }
    
    /**
     * Delete a pet
     * 
     * @param petId Pet ID to delete
     * @return Response containing the result of the deletion
     */
    public Response deletePet(Long petId) {
        log.info("Deleting pet with ID: {}", petId);
        return RestAssured.given()
                .spec(spec)
                .pathParam("petId", petId)
                .when()
                .delete(Endpoints.PET_BY_ID);
    }
    
    /**
     * Upload an image to a pet
     * 
     * @param petId ID of pet to update
     * @param additionalMetadata Additional metadata
     * @param file The file to upload
     * @return Response containing the result of the operation
     */
    public Response uploadImage(Long petId, String additionalMetadata, File file) {
        log.info("Uploading image for pet with ID: {}", petId);
        return RestAssured.given()
                .spec(spec)
                .contentType("multipart/form-data")
                .pathParam("petId", petId)
                .multiPart("additionalMetadata", additionalMetadata)
                .multiPart("file", file)
                .when()
                .post(Endpoints.UPLOAD_PET_IMAGE);
    }
} 