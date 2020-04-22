Feature: Plivo Slack API Assignment.

  Scenario Outline: Create a new Channel
    Given api URL "<URI>", ChannelNameRequired "<ChannelNameRequired>", NewChannelNameRequired "<NewChannelNameRequired>", ChannelIDRequired "<ChannelIDRequired>"
    When User hits the API
    Then Verify the Success Response Status-Code 200
    And Verify the Schema of the Response for "<URI>"
    And Verify the property Channel-name in the response JSON
    Examples:
      |URI| ChannelNameRequired | NewChannelNameRequired |ChannelIDRequired|
      |create|true | false| false|

  Scenario Outline: Join the newly created Channel
    Given api URL "<URI>", ChannelNameRequired "<ChannelNameRequired>", NewChannelNameRequired "<NewChannelNameRequired>", ChannelIDRequired "<ChannelIDRequired>"
    When User hits the API
    Then Verify the Success Response Status-Code 200
    And Verify the Schema of the Response for "<URI>"
    And Verify the property Channel-id, Channel-name, Channel-Creator in the response JSON for join
    And Verify the property Already-In-Channel should be true
    Examples:
      |URI| ChannelNameRequired | NewChannelNameRequired |ChannelIDRequired|
      |join|true | false| false|

  Scenario Outline: Rename the Channel
    Given api URL "<URI>", ChannelNameRequired "<ChannelNameRequired>", NewChannelNameRequired "<NewChannelNameRequired>", ChannelIDRequired "<ChannelIDRequired>"
    When User hits the API
    Then Verify the Success Response Status-Code 200
    And Verify the Schema of the Response for "<URI>"
    And Verify the property Channel-id, Channel-name, Channel-Creator in the response JSON for rename
    Examples:
      |URI| ChannelNameRequired | NewChannelNameRequired |ChannelIDRequired|
      |rename|false | true| true|

  Scenario Outline:
  List all Channels and Validate if the Channel name has changed successfully
    Given api URL "<URI>", ChannelNameRequired "<ChannelNameRequired>", NewChannelNameRequired "<NewChannelNameRequired>", ChannelIDRequired "<ChannelIDRequired>"
    When User hits the API
    Then Verify the Success Response Status-Code 200
    And Verify the Schema of the Response for "<URI>"
    And Verify the property Channel-id, Channel-Name, Channel-Creator in the response JSON
    And Verify the new channel which is been created and check the Archived Status which should be false
    Examples:
      |URI| ChannelNameRequired | NewChannelNameRequired |ChannelIDRequired|
      |list|false | false| false|

  Scenario Outline: Archive the Channel
    Given api URL "<URI>", ChannelNameRequired "<ChannelNameRequired>", NewChannelNameRequired "<NewChannelNameRequired>", ChannelIDRequired "<ChannelIDRequired>"
    When User hits the API
    Then Verify the Success Response Status-Code 200
    And Verify the Schema of the Response for "<URI>"
    And verify the success response of archive
    Examples:
      |URI| ChannelNameRequired | NewChannelNameRequired |ChannelIDRequired|
      |archive|false| false| true|


  Scenario Outline: Validate if the Channel is archived successfully
    Given api URL "<URI>", ChannelNameRequired "<ChannelNameRequired>", NewChannelNameRequired "<NewChannelNameRequired>", ChannelIDRequired "<ChannelIDRequired>"
    When User hits the API
    Then Verify the Success Response Status-Code 200
    And Verify the Schema of the Response for "<URI>"
    And Verify the property Channel-id, Channel-Name, Channel-Creator in the response JSON
    And Verify the new channel which is been created and check the Archived Status which should be true
    Examples:
      |URI| ChannelNameRequired | NewChannelNameRequired |ChannelIDRequired|
      |list|false | false| false|
