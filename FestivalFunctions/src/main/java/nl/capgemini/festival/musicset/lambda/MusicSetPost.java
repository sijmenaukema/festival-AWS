package nl.capgemini.festival.musicset.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.UUID;

import nl.capgemini.festival.model.MusicSet;
import nl.capgemini.festival.musicset.service.MusicSetService;

public class MusicSetPost extends MusicSetService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public MusicSetPost() {
        super();
    }

    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String body = request.getBody();
        MusicSet musicSet = gson.fromJson(body, MusicSet.class);
        if (musicSet.getId() == null) {
            UUID uuid = UUID.randomUUID();
            musicSet.setId(String.valueOf(uuid));
        }
        try {
            putItemInDynamo(musicSet);
            return getApiGatewayProxyResponseEvent(musicSet, 200);
        } catch (DynamoDbException e) {
            System.err.format(e.getMessage());
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