package nl.capgemini.festival.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDBClient {

    public static DynamoDbClient getDynamoDBClient() {
        Region region = Region.EU_WEST_1;
        return DynamoDbClient.builder()
                .region(region)
                .build();
    }
}
