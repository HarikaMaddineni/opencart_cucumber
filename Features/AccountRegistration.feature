Feature: Account Registration

Scenario: New User Registration
		Given User Launch browser
    And opens URL "https://tutorialsninja.com/demo/index.php"
    When User navigate to MyAccount menu
    And click on Register
    And user enters following details 
    		|FirstName|saketha|
    		|LastName |ch|
    		|E-mail   |saketha3.ch@gmail.com|
    		|Telephone|12345678|
    		|password|123456|
    		|cnfPassword|123456|
   And check the privacy policy
   And click continue
   Then user Navigate to sucessfull account  created page
    		
    		

	
