package nl.capgemini.festival.review.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.UUID;

import nl.capgemini.festival.model.Review;
import nl.capgemini.festival.review.service.ReviewService;

public class ReviewPost extends ReviewService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    Gson gson = new Gson();

        @Override
        public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
            String body = request.getBody();
            Review review = gson.fromJson(body, Review.class);
            UUID uuid = UUID.randomUUID();
            review.setId(String.valueOf(uuid));
            try {
                putItemInDynamo(review);
                return getApiGatewayProxyResponseEvent(review, 200);
            } catch (DynamoDbException e) {
                System.err.format(e.getMessage());
                System.exit(1);
            }
        return getApiGatewayProxyResponseEvent(null, 404);
        }

        private APIGatewayProxyResponseEvent getApiGatewayProxyResponseEvent(Object review, Integer statusCode) {

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(new HashMap<String, String>() {{
                        put("Access-Control-Allow-Origin", "*");
                    }})
                    .withBody(gson.toJson(review));
        }
}
