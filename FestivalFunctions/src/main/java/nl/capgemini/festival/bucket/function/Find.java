package nl.capgemini.festival.bucket.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

public class Find implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        String bucketName = "festival-data-2023";
        ListObjectsResponse listObjectsResponse = null;

        context.getLogger().log("Input: " + input);

        Region region = Region.EU_WEST_1;
        try (S3Client s3Client = S3Client.builder().region(region).build()) {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            listObjectsResponse = s3Client.listObjects(listObjects);
            List<S3Object> objects = listObjectsResponse.contents();
            for (S3Object myValue : objects) {
                System.out.printf("\n The name of the key is %s", myValue.key());
                System.out.printf("\n The object size is %s KBs", calKb(myValue.size()));
                System.out.printf("\n The owner is %s", myValue.owner());
                System.out.println();
            }

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return listObjectsResponse.sdkHttpResponse().toString();
    }
        private static long calKb(Long val) {
            return val/1024;
        }
}
