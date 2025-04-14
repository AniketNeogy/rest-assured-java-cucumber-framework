@user
Feature: User API
  As a user of the Pet Store API
  I want to perform operations on user accounts
  So that I can manage user access

  Background: 
    Given the User API is available

  @create @smoke
  Scenario: Create a new user
    Given I have user details to be registered
    When I send a request to create a user
    Then the user should be created successfully
    And the response should contain the success message

  @read @smoke
  Scenario: Get a user by username
    Given a user exists in the system
    When I send a request to get the user by username
    Then the user should be retrieved successfully
    And the response should contain the correct user details

  @update
  Scenario Outline: Update a user with different information
    Given a user exists in the system
    When I update the user with the following details:
      | firstName | <firstName> |
      | lastName  | <lastName>  |
      | email     | <email>     |
      | phone     | <phone>     |
    Then the user should be updated successfully
    And the response should contain the success message

    Examples:
      | firstName | lastName | email                | phone          |
      | John      | Doe      | john.doe@example.com | 123-456-7890   |
      | Jane      | Smith    | jane.s@example.com   | 987-654-3210   |
      | Mike      | Johnson  | mike.j@example.com   | 555-123-4567   |

  @delete
  Scenario: Delete a user
    Given a user exists in the system
    When I send a request to delete the user
    Then the user should be deleted successfully
    And a subsequent request to get the user should return 404

  @auth @smoke
  Scenario: User login
    Given I have valid user credentials
    When I send a request to login
    Then the login should be successful
    And the response should contain a session token

  @auth
  Scenario: User logout
    Given a user is logged in
    When I send a request to logout
    Then the logout should be successful
    And the session should be terminated 