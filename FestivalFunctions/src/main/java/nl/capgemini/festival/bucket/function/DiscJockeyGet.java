package nl.capgemini.festival.bucket.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import nl.capgemini.festival.model.DiscJockey;
import nl.capgemini.festival.request.HttpRequest;
import nl.capgemini.festival.response.HttpDiscJockeyResponse;

import java.util.Objects;

public class DiscJockeyGet extends DiscJockeyS3Client implements RequestHandler<HttpRequest, HttpDiscJockeyResponse> {

    @Override
    public HttpDiscJockeyResponse handleRequest(HttpRequest request, Context context) {
       context.getLogger().log("Request: " + request);

       if ( Objects.equals(request.getQueryStringParameters().get("id"), "all")) {
           DiscJockey[] discJockeys = getAllDiscJockeys();
           return new HttpDiscJockeyResponse(discJockeys);
       }

       long discJockeyId = Long.parseLong(request.getQueryStringParameters().get("id"));

       DiscJockey discJockey = getDiscJockeyById(discJockeyId);
       assert discJockey != null;
       return new HttpDiscJockeyResponse(discJockey);
    }

    private DiscJockey getDiscJockeyById(long discJockeyId) {

        DiscJockey[] discJockeys = getAllDiscJockeys();
            for(DiscJockey dj : discJockeys){
            if(dj.getId()==discJockeyId){
                return dj;
            }
        }
        return null;
    }
}
