package nl.capgemini.festival.bucket.function;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.capgemini.festival.model.DiscJockey;

public class DiscJockeyS3Client {
	String bucketName = "festival-data-2023";
	String key = "test.json";
	DiscJockey[] discJockeys = null;
	Region region = Region.EU_WEST_1;

	protected DiscJockey[] getAllDiscJockeys() {
		Region region = this.region;
        S3Client s3Client = S3Client.builder().region(region).build();
        ResponseInputStream<?> objectData = s3Client.getObject(GetObjectRequest.builder()
        		.bucket(bucketName)
        		.key(key)
        		.build());
        
        InputStreamReader isr = new InputStreamReader(objectData);
        BufferedReader br = new BufferedReader(isr);
		Gson gson = new Gson();

		discJockeys = gson.fromJson(br, DiscJockey[].class);
		return discJockeys;
	}
	
	protected ArrayList<DiscJockey> getAllDiscJockeysList(){
		return new ArrayList<DiscJockey>(Arrays.asList(getAllDiscJockeys()));
	}	
	
	protected boolean updateAllDiscJockeys(DiscJockey[] discJockeys) {
		Gson gson = new Gson(); 
        String jsonString = gson.toJson(discJockeys);
		Region region = this.region;
        S3Client s3Client = S3Client.builder().region(region).build();
        PutObjectResponse putResponse = s3Client.putObject(PutObjectRequest.builder()
        		.bucket(bucketName)
        		.key(key).build(),
        		RequestBody.fromString(jsonString));
        
        return putResponse.sdkHttpResponse().isSuccessful();
	}

	protected boolean updateAllDiscJockeys(List<DiscJockey> discJockeyList) {
		discJockeys = (DiscJockey[]) discJockeyList.toArray(new DiscJockey[discJockeyList.size()]);
		return updateAllDiscJockeys(discJockeys);
	}

}
