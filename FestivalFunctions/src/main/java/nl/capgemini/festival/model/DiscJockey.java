package nl.capgemini.festival.model;

public class DiscJockey {

    private long id;
    private String name;
    private String genre;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public DiscJockey(){}

    public DiscJockey(long id, String name, String genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "DiscJockey{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
