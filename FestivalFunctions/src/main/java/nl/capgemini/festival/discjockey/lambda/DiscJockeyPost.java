package nl.capgemini.festival.discjockey.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.UUID;

import nl.capgemini.festival.discjockey.service.DiscJockeyService;
import nl.capgemini.festival.model.DiscJockey;

public class DiscJockeyPost extends DiscJockeyService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public DiscJockeyPost() {
        super();
    }

    Gson gson = new Gson();
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String body = request.getBody();
        DiscJockey discJockey = gson.fromJson(body, DiscJockey.class);
        UUID uuid = UUID.randomUUID();
        discJockey.setId(String.valueOf(uuid));
        try {
            putItemInDynamo(discJockey);
            return getApiGatewayProxyResponseEvent(discJockey, 200);
        } catch (DynamoDbException e) {
            System.err.format(e.getMessage());
            System.exit(1);
        }
        return getApiGatewayProxyResponseEvent(null, 404);
    }

    private APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(DiscJockey discJockey, Integer statusCode) {

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(new HashMap<String, String>() {{
                    put("Access-Control-Allow-Origin", "*");
                }})
                .withBody(gson.toJson(discJockey));
    }
}

