package nl.capgemini.festival.discjockey.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nl.capgemini.festival.model.DiscJockey;
import nl.capgemini.festival.discjockey.service.DiscJockeyService;

public class DiscJockeyGetAll extends DiscJockeyService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public DiscJockeyGetAll() {
        super();
    }

    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        ArrayList<DiscJockey> discJockeysList = new ArrayList<>();
        try {
            Iterator<DiscJockey> responseItem = getAllItemsFromDynamo();
            responseItem.forEachRemaining(discJockeysList :: add);
            if( discJockeysList.size() != 0 ) {
                return getApiGatewayProxyResponseEvent(discJockeysList, 200);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return getApiGatewayProxyResponseEvent(discJockeysList, 404);
    }

    private APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(ArrayList<DiscJockey> discJockeysList, Integer statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(new HashMap<String, String>() {{
                    put("Access-Control-Allow-Origin", "*");
                }})
                .withBody(gson.toJson(discJockeysList));
    }
}

