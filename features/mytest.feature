Feature: Login


  Scenario: Successful login with valid credentials


    Given User launch the chrome browser

    When User opens URL "http://admin-demo.nopcommerce.com/login"

    And User enters email as "admin@yourstore.com" and password as "admin"

    And Click on login button

    Then Page title should be "Dashboard / nopCommerce administration"

    When User click on logout link

    Then Page title should be "Your store. Login"

    And Close browser