package nl.capgemini.festival.bucket.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Get implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        String bucketName = "festival-data-2023";
        String outputString;
        String key = (input == null) ? "\\*.txt" : input.toString();

        context.getLogger().log("Input: " + input);

        Region region = Region.EU_WEST_1;
        try (S3Client s3Client = S3Client.builder().region(region).build()) {
            ResponseInputStream<?> objectData = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());

            InputStreamReader inputStreamReader = new InputStreamReader(objectData);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            outputString = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputString;
    }
}