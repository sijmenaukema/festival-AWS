package nl.capgemini.festival.musicset.service;

import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.capgemini.festival.model.MusicSet;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import static nl.capgemini.festival.config.DynamoDBClient.getDynamoDBClient;

public class MusicSetService {

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
                put(":keyValue", attVal);
            }};
            Map<String, String> expressionNames = new HashMap<>(){{
                put("#disc_jockey_id", "disc_jockey_id");
            }};

            Expression filterExpression =  Expression.builder()
                    .expressionValues(expressionValues)
                    .expressionNames(expressionNames)
                    .expression("equals(#disc_jockey_id, :keyValue)")
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
