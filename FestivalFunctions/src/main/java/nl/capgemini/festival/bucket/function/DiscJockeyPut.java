package nl.capgemini.festival.bucket.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import nl.capgemini.festival.model.DiscJockey;
import nl.capgemini.festival.request.HttpRequest;
import nl.capgemini.festival.response.HttpDiscJockeyResponse;

import java.util.List;

public class DiscJockeyPut extends DiscJockeyS3Client  implements RequestHandler<HttpRequest, HttpDiscJockeyResponse> {

    @Override
    public HttpDiscJockeyResponse handleRequest(HttpRequest request, Context context) {

        context.getLogger().log("Request: " + request);

        String body = request.getBody();
        Gson gson = new Gson();
        DiscJockey discJockeyToAdd = gson.fromJson(body, DiscJockey.class);

        List<DiscJockey> discJockeyList = getAllDiscJockeysList();
        discJockeyList.add(discJockeyToAdd);

        if(updateAllDiscJockeys(discJockeyList)) {
            return new HttpDiscJockeyResponse();
        }

        HttpDiscJockeyResponse response = new HttpDiscJockeyResponse();
        response.setStatusCode("500");
        return response;
    }
}