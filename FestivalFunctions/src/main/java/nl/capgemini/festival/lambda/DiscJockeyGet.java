package nl.capgemini.festival.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Collections;

import nl.capgemini.festival.model.DiscJockey;
import nl.capgemini.festival.config.DiscJockeyDynamoDBClient;

public class DiscJockeyGet extends DiscJockeyDynamoDBClient implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public DiscJockeyGet() {
        super();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String keyValue = request.getQueryStringParameters().get("id");

        try {
            DiscJockey discJockey = getItemFromDynamo(keyValue);
            Gson gson = new Gson();
            if( discJockey!=null ) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(200)
                        .withHeaders(Collections.emptyMap())
                        .withBody(gson.toJson(discJockey));
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(404);
    }
}

