@store
Feature: Store API
  As a user of the Pet Store API
  I want to perform operations on store orders
  So that I can manage pet orders

  Background: 
    Given the Store API is available

  @create @smoke
  Scenario: Create a new order
    Given I have order details to be placed
    When I send a request to create an order
    Then the order should be created successfully
    And the response should contain the order details

  @read @smoke
  Scenario: Get an order by ID
    Given an order exists in the store
    When I send a request to get the order by ID
    Then the order should be retrieved successfully
    And the response should contain the correct order details

  @delete
  Scenario: Delete an order
    Given an order exists in the store
    When I send a request to delete the order
    Then the order should be deleted successfully
    And a subsequent request to get the order should return 404

  @read
  Scenario: Get inventory by status
    When I send a request to get the inventory
    Then the inventory should be retrieved successfully
    And the response should contain the inventory counts by status

  @create
  Scenario Outline: Create orders with different quantities
    Given I have the following order details:
      | petId    | <petId>    |
      | quantity | <quantity> |
      | status   | <status>   |
    When I send a request to create an order
    Then the order should be created successfully
    And the response should contain the order details with quantity <quantity>

    Examples:
      | petId | quantity | status    |
      | 1     | 1        | placed    |
      | 2     | 5        | approved  |
      | 3     | 10       | delivered | 