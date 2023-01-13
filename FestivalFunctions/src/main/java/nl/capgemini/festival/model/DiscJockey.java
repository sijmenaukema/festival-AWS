package nl.capgemini.festival.model;

import com.google.gson.annotations.SerializedName;

public class DiscJockey {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("genre")
    private String genre;

    public DiscJockey(){}

    public DiscJockey(long id){
        this.id = id;
    }

    public Long getId() {return id; }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public void setGenre(String genre) { this.genre = genre; }
}
