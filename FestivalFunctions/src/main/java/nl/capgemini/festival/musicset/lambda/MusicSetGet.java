package nl.capgemini.festival.musicset.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;

import nl.capgemini.festival.model.MusicSet;
import nl.capgemini.festival.musicset.service.MusicSetService;

public class MusicSetGet extends MusicSetService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public MusicSetGet() {
        super();
    }

    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String keyValue = request.getQueryStringParameters().get("id");
        try {
            MusicSet musicSet = getItemFromDynamo(keyValue);
            if( musicSet != null ) {
                return getApiGatewayProxyResponseEvent(musicSet, 200);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return getApiGatewayProxyResponseEvent(null, 404);
    }

    private APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(MusicSet musicSet, Integer statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(new HashMap<String, String>() {{
                    put("Access-Control-Allow-Origin", "*");
                }})
                .withBody(gson.toJson(musicSet));
    }
}