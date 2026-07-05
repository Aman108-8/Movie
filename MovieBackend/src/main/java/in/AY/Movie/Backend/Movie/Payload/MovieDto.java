package in.AY.Movie.Backend.Movie.Payload;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;

public class MovieDto {

	private UUID id;
    private String title;
    private String description;
    private String genre;
    private Double rating;
    private Integer releaseYear;
    private String thumbnail;

    private List<MovieQualityDto> quality;
    private List<MovieScreenShotDto> screenShot;

    
    
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

	public List<MovieQualityDto> getQuality() {
		return quality;
	}

	public void setQuality(List<MovieQualityDto> quality) {
		this.quality = quality;
	}

	
	public List<MovieScreenShotDto> getScreenShot() {
		return screenShot;
	}

	public void setScreenShot(List<MovieScreenShotDto> screenShot) {
		this.screenShot = screenShot;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
    
}