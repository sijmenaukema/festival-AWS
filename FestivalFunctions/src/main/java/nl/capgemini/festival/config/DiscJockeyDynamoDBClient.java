package nl.capgemini.festival.config;

import nl.capgemini.festival.model.DiscJockey;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedResponse;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Iterator;

public class DiscJockeyDynamoDBClient {

    String tableName = "disc_jockey";

    public DynamoDbClient getDynamoDB() {
        Region region = Region.EU_WEST_1;
        return DynamoDbClient.builder()
                .region(region)
                .build();
    }

    public DynamoDbTable<DiscJockey> getDynamoDbTable() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDB())
                .build();

        return enhancedClient.table(tableName, TableSchema.fromBean(DiscJockey.class));
    }

    public DiscJockey getItemFromDynamo(String keyValue) {
        DynamoDbTable<DiscJockey> djTable = getDynamoDbTable();
        Key key = Key.builder()
                .partitionValue(keyValue)
                .build();

        return  djTable.getItem(key);
    }

    public Iterator<DiscJockey> getAllItemsFromDynamo() {
        try {
            DynamoDbTable<DiscJockey> djTable = getDynamoDbTable();
            return djTable.scan().items().iterator();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public PutItemEnhancedResponse putItemInDynamo(DiscJockey discJockey) {
        DynamoDbTable<DiscJockey> djTable = getDynamoDbTable();
        PutItemEnhancedRequest request = PutItemEnhancedRequest.builder(DiscJockey.class)
                .item(discJockey)
                .build();

        return djTable.putItemWithResponse(request);
    }

    public DeleteItemEnhancedResponse<DiscJockey> deleteItemFromDynamo(String keyValue, String partitionKeyName) {
        DynamoDbTable<DiscJockey> djTable = getDynamoDbTable();
        Key key = Key.builder()
                .partitionValue(keyValue)
                .build();

        DeleteItemEnhancedRequest request= DeleteItemEnhancedRequest.builder()
                .key(key)
                .build();

        return djTable.deleteItemWithResponse(request);
    }
}
