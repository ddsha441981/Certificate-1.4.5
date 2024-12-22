# Created by deendayal kumawat at 17-06-2024
Feature: User Login with JWT Authentication

  As a user
  I want to log in to the application
  So that I can access protected resources using JWT tokens

  Scenario: Successful login with valid credentials
    Given I have the valid login credentials
    When I send a POST request to "/auth/login" with my credentials
    Then I should receive a 200 status code
    And I should receive a JWT token in the response

  Scenario: Failed login with invalid credentials
    Given I have invalid login credentials
    When I send a POST request to "/auth/login" with my credentials
    Then I should receive a 401 status code
    And I should not receive a JWT token

  Scenario: Accessing protected resource with valid JWT token
    Given I have a valid JWT token
    When I send a GET request to "/protected/resource" with my token
    Then I should receive a 200 status code
    And I should receive the requested data

  Scenario: Accessing protected resource with invalid JWT token
    Given I have an invalid JWT token
    When I send a GET request to "/protected/resource" with my token
    Then I should receive a 403 status code
    And I should not receive the requested data

  Scenario: Accessing protected resource without JWT token
    Given I have no JWT token
    When I send a GET request to "/protected/resource" without a token
    Then I should receive a 401 status code
    And I should not receive the requested data


