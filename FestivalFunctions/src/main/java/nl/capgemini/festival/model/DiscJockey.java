package nl.capgemini.festival.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DiscJockey {

    private String id;
    private String name;
    private String genre;

    @DynamoDbPartitionKey
    public String getId() {return id; }
    public void setId(String id) {this.id = id; }

    @DynamoDbSortKey
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public DiscJockey(){}
}
