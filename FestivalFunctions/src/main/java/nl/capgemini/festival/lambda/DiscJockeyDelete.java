package nl.capgemini.festival.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Collections;

import nl.capgemini.festival.config.DiscJockeyDynamoDBClient;
import nl.capgemini.festival.model.DiscJockey;

public class DiscJockeyDelete extends DiscJockeyDynamoDBClient implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public DiscJockeyDelete() {
        super();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String keyValue = request.getQueryStringParameters().get("id");
        try {
            DeleteItemEnhancedResponse<DiscJockey> response = deleteItemFromDynamo(keyValue, "id");
            if (response.attributes() != null) {
                Gson gson = new Gson();
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(200)
                        .withHeaders(Collections.emptyMap())
                        .withBody(gson.toJson(response.attributes()));
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    return new APIGatewayProxyResponseEvent()
            .withStatusCode(404)
            .withHeaders(Collections.emptyMap())
            .withBody(context.getAwsRequestId());
    }
}