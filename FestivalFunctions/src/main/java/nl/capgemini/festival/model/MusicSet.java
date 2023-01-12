package nl.capgemini.festival.model;

public class MusicSet {

    private long id;
    private String title;
    private String genre;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public MusicSet(){}

    public MusicSet(long id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "MusicSet{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
