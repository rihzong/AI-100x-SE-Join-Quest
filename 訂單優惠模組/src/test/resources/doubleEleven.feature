@doubleEleven_order_pricing
Feature: Double Eleven Order Pricing Promotions
  As a shopper
  I want the system to calculate my order total with Double Eleven promotions
  So that I can understand how much to pay and what items I will receive

Scenario: Same product quantity reaches 10 without promotions
    Given no promotions are applied
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子     | 10        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000         |
    And the customer should receive:
      | productName | quantity |
      | 襪子     | 10        |

  Scenario: Threshold discount applies when same product quantity reaches 10
    Given Double Eleven promotions is active And the threshold discount promotion is configured:
      | threshold | discount |
      | 10      | 0.2      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子     | 10        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 800         |
    And the customer should receive:
      | productName | quantity |
      | 襪子     | 10        |

  Scenario: customer buy same product when quantity reaches 12
    Given the threshold discount promotion is configured:
      | threshold | discount |
      | 10      | 0.2      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子     | 12        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 襪子     | 12        |

  Scenario: customer buy same product when quantity reaches 27
    Given the threshold discount promotion is configured:
      | threshold | discount |
      | 10      | 0.2      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子     | 27        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 2300        |
    And the customer should receive:
      | productName | quantity |
      | 襪子     | 27        |

 Scenario: customer buy multiple products when total quantity reaches 10
    Given the threshold discount promotion is configured:
      | threshold | discount |
      | 10      | 0.2      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子     | 1        | 100       |
      | T-shirt     | 1        | 100       |
      | 口紅     | 1        | 100       |
      | 鞋子     | 1        | 100       |
      | 褲子     | 1        | 100       |
      | 裙子     | 1        | 100       |
      | 糖果     | 1        | 100       |
      | 水     | 1        | 100       |
      | 果汁     | 1        | 100       |
      | 可樂     | 1        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 襪子     | 1        |
      | T-shirt     | 1        |
      | 口紅     | 1        |
      | 鞋子     | 1        |
      | 褲子     | 1        |
      | 裙子     | 1        |
      | 糖果     | 1        |
      | 水     | 1        |
      | 果汁     | 1        |
      | 可樂     | 1        | 