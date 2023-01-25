package nl.capgemini.festival.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class MusicSet {

    private String id;
    private String discJockeyId;
    private String title;
    private String genre;

    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @DynamoDbSortKey
    @DynamoDbAttribute("disc_jockey_id")
    public String getDiscJockeyId() { return discJockeyId; }
    public void setDiscJockeyId(String discJockeyId) { this.discJockeyId = discJockeyId;}

    public String getTitle() { return title; }
    public String getGenre() {return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setTitle(String title) { this.title = title; }

    public MusicSet(){}
}
