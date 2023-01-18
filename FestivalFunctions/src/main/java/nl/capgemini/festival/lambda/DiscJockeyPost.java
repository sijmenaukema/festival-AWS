package nl.capgemini.festival.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import nl.capgemini.festival.config.DiscJockeyDynamoDBClient;
import nl.capgemini.festival.model.DiscJockey;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Collections;
import java.util.UUID;

public class DiscJockeyPost extends DiscJockeyDynamoDBClient implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public DiscJockeyPost() {
        super();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Gson gson = new Gson();
        String body = request.getBody();
        DiscJockey discJockey = gson.fromJson(body, DiscJockey.class);
        UUID uuid = UUID.randomUUID();
        discJockey.setId(String.valueOf(uuid));
        try {
            PutItemEnhancedResponse response = putItemInDynamo(discJockey);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Collections.emptyMap())
                    .withBody(gson.toJson(discJockey));
        } catch (DynamoDbException e) {
            System.err.format(e.getMessage());
            System.exit(1);
        }

        return new APIGatewayProxyResponseEvent()
                .withHeaders(Collections.emptyMap())
                .withBody(context.getAwsRequestId());
    }
}

