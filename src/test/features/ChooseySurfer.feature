#language:en 

Feature: Pick best suitable spot out of top two spots
    Scenario: As a choosey surfer
        Given I like to surf in any of 2 beaches out of top 10 of Sydney
#            | Lat    | Long      | Postcode |
            | -33.89 | 151.27     | 2026     |
            | -33.79 | 151.28     | 2095     |
            | -33.37 | 151.48     | 2261     |
            | -33.59 | 151.32     | 2108     |
            | -33.82 | 151.25     | 2088     |
            | -33.90 | 151.26     | 2024     |
            | -33.64 | 151.32     | 2107     |
            | -33.85 | 151.26     | 2030     |
            | -33.92 | 151.25     | 2034     |
            | -33.78 | 151.29     | 2096     |

        And I only like to surf on "Monday" & "Friday" in next 16 days
        When I look up the the weather forecast for the next 16 days with postcodes
        Then I check to if see the temperature is between <12°C and 30°C>
        And I check wind speed to be between 3 and 9
        And I check to see if UV index is <= 12
        And I Pick best suitable spot out of top two spots, based upon suitable weather forecast for the day

