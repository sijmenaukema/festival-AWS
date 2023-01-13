package nl.capgemini.festival.model;

import com.google.gson.annotations.SerializedName;

public class MusicSet {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("genre")
    private String genre;

    public MusicSet(){}

    public MusicSet(String title, DiscJockey discJockey, String genre){
        this.title = title;
        this.genre = genre;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() {return genre; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
}
