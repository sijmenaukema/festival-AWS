package nl.capgemini.festival;

import nl.capgemini.festival.model.DiscJockey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rdsdata.RdsDataClient;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementRequest;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementResponse;
import software.amazon.awssdk.services.rdsdata.model.Field;
import software.amazon.awssdk.services.rdsdata.model.SqlParameter;

import java.util.List;
public class DiscJockeyRDSClient {
	static Region region = Region.EU_WEST_1;

	protected DiscJockeyRDSClient() {}

	public static RdsDataClient rdsClient() {
		return RdsDataClient.builder()
				.region(region)
				.build();
	}

	protected DiscJockey getDiscJockeyById(String discJockeyId) {
		final String GET_BY_ID_SQL_STATEMENT = "select id, name, genre from %s.discjockeys where id=:id";

		ExecuteStatementResponse executeStatementResponse;
		try (RdsDataClient rdsDataClient = DiscJockeyRDSClient.rdsClient()) {

			ExecuteStatementRequest executeStatementRequest = ExecuteStatementRequest.builder()
					.database("database-festival")
					.resourceArn("arn:aws:rds:eu-west-1:496035563056:db:database-festival")
					.secretArn("arn:aws:kms:eu-west-1:496035563056:key/781261cf-a067-4137-a7d9-e4e2f315bf34")
					.sql(String.format(GET_BY_ID_SQL_STATEMENT, "database-festival"))
					.parameters(SqlParameter.builder().name(discJockeyId).value(Field.builder().stringValue(discJockeyId).build()).build())
					.continueAfterTimeout(true)
					.build();

			executeStatementResponse = rdsDataClient.executeStatement(executeStatementRequest);
			if (executeStatementResponse.hasRecords()) {
				List<List<Field>> records = executeStatementResponse.records();
				for (List<Field> record : records) {
					return transformToDiscJockey(record);
				}
			}
			return null;
		}
	}

	private DiscJockey transformToDiscJockey(List<Field> record) {
		Long id = record.get(0).longValue();
		DiscJockey discJockey = new DiscJockey(id);
		discJockey.setName(record.get(1).stringValue());
		discJockey.setGenre(record.get(2).stringValue());
		return discJockey;
	}
}

