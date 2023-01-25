package nl.capgemini.festival.discjockey.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import nl.capgemini.festival.discjockey.service.DiscJockeyService;
import nl.capgemini.festival.model.DiscJockey;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;

public class DiscJockeyDelete extends DiscJockeyService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public DiscJockeyDelete() {
        super();
    }

    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String keyValue = request.getQueryStringParameters().get("id");
        try {
            DiscJockey response = deleteItemFromDynamo(keyValue).attributes();
            //TODO remove all musicset
            if (response != null) {
                return getApiGatewayProxyResponseEvent(response, 200);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
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