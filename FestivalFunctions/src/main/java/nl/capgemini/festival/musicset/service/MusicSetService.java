package nl.capgemini.festival.musicset.service;

import nl.capgemini.festival.config.DynamoDBClient;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.capgemini.festival.model.MusicSet;

public class MusicSetService extends DynamoDBClient {

    public static DynamoDbTable<MusicSet> getDynamoDbTable() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDBClient())
                .build();

        return enhancedClient.table("music_set", TableSchema.fromBean(MusicSet.class));
    }

    public MusicSet getItemFromDynamo(String keyValue) {
        DynamoDbTable<MusicSet> msTable = getDynamoDbTable();
        Key key = Key.builder()
                .partitionValue(keyValue)
                .build();

        return  msTable.getItem(key);
    }

    public Iterator<MusicSet> getKeyItemsFromDynamo(String keyValue) {
        try {
            DynamoDbTable<MusicSet> msTable = getDynamoDbTable();
            AttributeValue attVal = AttributeValue.fromS(keyValue);
            Map<String, AttributeValue> expressionValues = new HashMap<String, AttributeValue>(){{
                put(":dj_id", attVal);
            }};
            Map<String, String> expressionNames = new HashMap<>(){{
                put("#i", "disc_jockey_id");
            }};

            Expression filterExpression =  Expression.builder()
                    .expressionValues(expressionValues)
                    .expressionNames(expressionNames)
                    .expression("#i = :dj_id")
                    .build();

            ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                    .filterExpression(filterExpression)
                    .build();

            return msTable.scan(scanRequest).items().iterator();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public Iterator<MusicSet> getAllItemsFromDynamo() {
        try {
            DynamoDbTable<MusicSet> msTable = getDynamoDbTable();
            return msTable.scan().items().iterator();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public PutItemEnhancedResponse putItemInDynamo(MusicSet musicSet) {
        DynamoDbTable<MusicSet> msTable = getDynamoDbTable();
        PutItemEnhancedRequest request = PutItemEnhancedRequest.builder(MusicSet.class)
                .item(musicSet)
                .build();

        return msTable.putItemWithResponse(request);
    }

    public DeleteItemEnhancedResponse<MusicSet> deleteItemFromDynamo(String keyValue) {
        DynamoDbTable<MusicSet> msTable = getDynamoDbTable();
        Key key = Key.builder()
                .partitionValue(keyValue)
                .build();

        DeleteItemEnhancedRequest request= DeleteItemEnhancedRequest.builder()
                .key(key)
                .build();

        return msTable.deleteItemWithResponse(request);
    }
}
