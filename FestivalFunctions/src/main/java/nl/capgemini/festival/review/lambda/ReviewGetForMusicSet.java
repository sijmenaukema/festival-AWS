package nl.capgemini.festival.review.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nl.capgemini.festival.model.Review;
import nl.capgemini.festival.review.service.ReviewService;

public class ReviewGetForMusicSet extends ReviewService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        ArrayList<Review> reviewList = new ArrayList<>();
        String sortValue = request.getQueryStringParameters().get("id");
        try {
            Iterator<Review> responseItem = getKeyItemsFromDynamo(sortValue);
            responseItem.forEachRemaining(reviewList::add);
            if (reviewList.size() != 0) {
                return getApiGatewayProxyResponseEvent(reviewList, 200);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return getApiGatewayProxyResponseEvent(reviewList, 404);
    }

    private APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(ArrayList<Review> reviewList, Integer statusCode) {

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(new HashMap<String, String>() {{
                    put("Access-Control-Allow-Origin", "*");
                }})
                .withBody(gson.toJson(reviewList));
    }
}