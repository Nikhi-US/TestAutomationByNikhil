@IncubyteAssessment
Feature:Incubyte Assessment Test

  Background: Launch URL to login
    When User initilize the driver and incognitomode with URL "ENV_test"
  @Test1
  Scenario Outline:Create an Account- Luma
    Then User load the data from <filename> in <sheetname> from <rowNumber>
    When User verify the Logo
    Then User Creates an Account

    Examples:
      | filename      | sheetname | rowNumber |
      | TestData.xlsx | TestCreds | 0         |

  @Test2
  Scenario Outline:Sign In Luma Account
    Then User load the data from <filename> in <sheetname> from <rowNumber>
    When User verify the Logo
    Then User Logins to Luma Account

    Examples:
      | filename      | sheetname | rowNumber |
      | TestData.xlsx | TestCreds | 0         |