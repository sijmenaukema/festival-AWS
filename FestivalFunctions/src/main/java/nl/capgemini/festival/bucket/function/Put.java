package nl.capgemini.festival.bucket.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.sql.Timestamp;
import java.util.Date;

public class Put implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        PutObjectResponse putObjectResponse;
        String bucketName = "festival-data-2023";
        Region region = Region.EU_WEST_1;
        Date date = new Date();
        Timestamp ts =new Timestamp(date.getTime());

        context.getLogger().log("Input: " + input);

        try (S3Client s3Client = S3Client.builder().region(region).build()) {
            putObjectResponse = s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(ts.toString())
                    .build(),
                RequestBody.fromString((String) input));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return putObjectResponse.sdkHttpResponse().toString();
    }
}