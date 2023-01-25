package nl.capgemini.festival.discjockey.service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedResponse;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Iterator;

import nl.capgemini.festival.model.DiscJockey;
import nl.capgemini.festival.config.DynamoDBClient;

public class DiscJockeyService extends DynamoDBClient {

    public static DynamoDbTable<DiscJockey> getDynamoDbTable() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDBClient())
                .build();

        return enhancedClient.table("disc_jockey", TableSchema.fromBean(DiscJockey.class));
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

    public DeleteItemEnhancedResponse<DiscJockey> deleteItemFromDynamo(String keyValue) {
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
