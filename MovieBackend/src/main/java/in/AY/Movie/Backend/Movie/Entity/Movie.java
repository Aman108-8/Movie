package in.AY.Movie.Backend.Movie.Entity;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Movie {
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column
    private String genre;

    @Column
    private Double rating;

    @Column
    private Integer releaseYear;

    @Column
    private String thumbnail;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieQuality> quality = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<MovieScreenShot> screenShot = new ArrayList<>();

	public List<MovieScreenShot> getScreenShot() {
		return screenShot;
	}

	public void setScreenShot(List<MovieScreenShot> screenShot) {
		this.screenShot = screenShot;
	}

	public List<MovieQuality> getQuality() {
		return quality;
	}

	public void setQuality(List<MovieQuality> quality) {
		this.quality = quality;
	}

	 // ---------- Helper Methods ----------
	
	public void addQuality(MovieQuality q){
        this.quality.add(q);
        q.setMovie(this);
    }

    public void addScreenShot(MovieScreenShot s){
        this.screenShot.add(s);
        s.setMovie(this);
    }

    public void clearQualities(){
        this.quality.clear();
    }

    public void clearScreenshots(){
        this.screenShot.clear();
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

}
