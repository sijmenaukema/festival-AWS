package nl.capgemini.festival.musicset.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import nl.capgemini.festival.model.MusicSet;
import nl.capgemini.festival.musicset.service.MusicSetService;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MusicSetGetForDiscJockey extends MusicSetService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public MusicSetGetForDiscJockey() {
        super();
    }

    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        ArrayList<MusicSet> musicSetList = new ArrayList<>();
        String sortValue = request.getQueryStringParameters().get("discJockeyId");
        try {
            Iterator<MusicSet> responseItem = getKeyItemsFromDynamo(sortValue);
            responseItem.forEachRemaining(musicSetList::add);
            if (musicSetList.size() != 0) {
                return getApiGatewayProxyResponseEvent(musicSetList, 200);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return getApiGatewayProxyResponseEvent(musicSetList, 404);
    }

    private APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(ArrayList<MusicSet> musicSetList, Integer statusCode) {

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(new HashMap<String, String>() {{
                    put("Access-Control-Allow-Origin", "*");
                }})
                .withBody(gson.toJson(musicSetList));
    }
}

