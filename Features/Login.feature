Feature: Login with Valid Credentials

@sanity @regression	
Scenario: Successful Login with Valid Credentials
  	Given User Launch browser
    And opens URL "https://tutorialsninja.com/demo/index.php"
    When User navigate to MyAccount menu
    And click on Login
    And User enters Email as "maddineniharika@gmail.com" and Password as "123456"
    And Click on Login button
    Then User navigates to MyAccount Page