package nl.capgemini.festival.bucket.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import nl.capgemini.festival.model.DiscJockey;
import nl.capgemini.festival.request.HttpRequest;
import nl.capgemini.festival.response.HttpDiscJockeyResponse;

import java.util.List;

public class DiscJockeyDelete extends DiscJockeyS3Client implements RequestHandler<HttpRequest, HttpDiscJockeyResponse> {

    @Override
    public HttpDiscJockeyResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Request: " + request);

        long discJockeyToBeRemovedId = Long.parseLong(request.getQueryStringParameters().get("id"));

        List<DiscJockey> discJockeyList = getAllDiscJockeysList();

        boolean isSuccessful = discJockeyList.removeIf(dj -> dj.getId() == discJockeyToBeRemovedId);
        boolean isUpdated = updateAllDiscJockeys(discJockeyList);

        if(isSuccessful && isUpdated){
            return new HttpDiscJockeyResponse();
        }

        HttpDiscJockeyResponse response = new HttpDiscJockeyResponse();
        response.setStatusCode("400");
        return response;
    }
}
