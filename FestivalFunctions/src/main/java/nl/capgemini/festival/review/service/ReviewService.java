package nl.capgemini.festival.review.service;

import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.capgemini.festival.config.DynamoDBClient;
import nl.capgemini.festival.model.Review;

public class ReviewService extends DynamoDBClient {

    public static DynamoDbTable<Review> getDynamoDbTable() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDBClient())
                .build();

        return enhancedClient.table("review", TableSchema.fromBean(Review.class));
    }

    public Iterator<Review> getKeyItemsFromDynamo(String keyValue) {
        try {
            DynamoDbTable<Review> msTable = getDynamoDbTable();
            AttributeValue attVal = AttributeValue.fromS(keyValue);
            Map<String, AttributeValue> expressionValues = new HashMap<String, AttributeValue>(){{
                put(":ms_id", attVal);
            }};
            Map<String, String> expressionNames = new HashMap<>(){{
                put("#i", "music_set_id");
            }};

            Expression filterExpression =  Expression.builder()
                    .expressionValues(expressionValues)
                    .expressionNames(expressionNames)
                    .expression("#i = :ms_id")
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


    public PutItemEnhancedResponse putItemInDynamo(Review review) {
        DynamoDbTable<Review> msTable = getDynamoDbTable();
        PutItemEnhancedRequest request = PutItemEnhancedRequest.builder(Review.class)
                .item(review)
                .build();

        return msTable.putItemWithResponse(request);
    }

    public DeleteItemEnhancedResponse<Review> deleteItemFromDynamo(String keyValue) {
        DynamoDbTable<Review> msTable = getDynamoDbTable();
        Key key = Key.builder()
                .partitionValue(keyValue)
                .build();

        DeleteItemEnhancedRequest request= DeleteItemEnhancedRequest.builder()
                .key(key)
                .build();

        return msTable.deleteItemWithResponse(request);
    }
}
