package nl.capgemini.festival.musicset.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import nl.capgemini.festival.model.MusicSet;
import nl.capgemini.festival.musicset.service.MusicSetService;

import java.util.HashMap;

public class MusicSetDelete extends MusicSetService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public MusicSetDelete() {
        super();
    }

    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String keyValue = request.getQueryStringParameters().get("id");
        try {
            MusicSet response = deleteItemFromDynamo(keyValue).attributes();
            if (response != null) {
                return getApiGatewayProxyResponseEvent(response, 200);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return getApiGatewayProxyResponseEvent(null, 404);
    }

    private APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(MusicSet response, Integer statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(new HashMap<String, String>() {{
                    put("Access-Control-Allow-Origin", "*");
                }})
                .withBody(gson.toJson(response));
    }
}