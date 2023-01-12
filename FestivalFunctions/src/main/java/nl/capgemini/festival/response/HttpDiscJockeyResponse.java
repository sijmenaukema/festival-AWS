package nl.capgemini.festival.response;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import nl.capgemini.festival.model.DiscJockey;

public class HttpDiscJockeyResponse {

    private String body;
    private String statusCode = "200";
    private Map<String, String> headers = new HashMap<String, String>();

    public HttpDiscJockeyResponse() {
        super();
        this.headers.put("Content-Type","application/json");
    }

    public HttpDiscJockeyResponse(DiscJockey discJockey) {
        this();
        Gson gson = new Gson();
        this.body = gson.toJson(discJockey);
    }

    public HttpDiscJockeyResponse(DiscJockey[] discJockeys) {
        this();
        Gson gson = new Gson();
        this.body = gson.toJson(discJockeys);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
