package in.AY.Movie.Backend.Movie.Payload;

public class MovieScreenShotDto {

    private String imagePath;
    private Long id;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}