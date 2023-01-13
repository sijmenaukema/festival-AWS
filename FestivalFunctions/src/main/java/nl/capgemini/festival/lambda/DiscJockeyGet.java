package nl.capgemini.festival.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import nl.capgemini.festival.DiscJockeyRDSClient;
import nl.capgemini.festival.model.DiscJockey;

import java.util.Collections;

public class DiscJockeyGet extends DiscJockeyRDSClient implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    String responseBody = "";

    public DiscJockeyGet() {
        super();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String discJockeyId = request.getQueryStringParameters().get("id");
        DiscJockey discJockey = getDiscJockeyById(discJockeyId);
        responseBody += discJockey.toString();
        if( !responseBody.isEmpty() ) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Collections.emptyMap())
                    .withBody(responseBody);
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(404);
    }
}

