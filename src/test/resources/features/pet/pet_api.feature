@pet
Feature: Pet API
  As a user of the Pet Store API
  I want to perform CRUD operations on pets
  So that I can manage the pets in the store

  Background: 
    Given the Pet API is available

  @create @smoke
  Scenario: Create a new pet
    Given I have pet details to be added to the store
    When I send a request to create a pet
    Then the pet should be created successfully
    And the response should contain the pet details

  @read @smoke
  Scenario: Get a pet by ID
    Given a pet exists in the store
    When I send a request to get the pet by ID
    Then the pet should be retrieved successfully
    And the response should contain the correct pet details

  @update
  Scenario: Update an existing pet
    Given a pet exists in the store
    When I update the pet with new name and status
    Then the pet should be updated successfully
    And the response should contain the updated pet details

  @read
  Scenario Outline: Find pets by different statuses
    Given I want to find pets by status
    When I send a request to find pets with status "<status>"
    Then the pets should be retrieved successfully
    And the response should contain a list of pets

    Examples:
      | status    |
      | available |
      | pending   |
      | sold      |

  @delete
  Scenario: Delete a pet
    Given a pet exists in the store
    When I send a request to delete the pet
    Then the pet should be deleted successfully
    And a subsequent request to get the pet should return 404 