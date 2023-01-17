package nl.capgemini.festival.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import nl.capgemini.festival.config.DiscJockeyDynamoDBClient;
import nl.capgemini.festival.model.DiscJockey;

public class DiscJockeyGetAll extends DiscJockeyDynamoDBClient implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public DiscJockeyGetAll() {
        super();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        ArrayList<DiscJockey> discJockeysList = new ArrayList<>();
        try {
            Iterator<DiscJockey> responseItem = getAllItemsFromDynamo();
            responseItem.forEachRemaining(discJockeysList :: add);
            Gson gson = new Gson();
            if( discJockeysList.size() != 0 ) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(200)
                        .withHeaders(Collections.emptyMap())
                        .withBody(gson.toJson(discJockeysList));
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(404);
    }
}

