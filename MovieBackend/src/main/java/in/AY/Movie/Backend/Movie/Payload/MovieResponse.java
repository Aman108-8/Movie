package in.AY.Movie.Backend.Movie.Payload;

import java.util.List;

public class MovieResponse {
	private List<MovieDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElement;
	private int totalPage;
	private boolean lastPage;
	
	public List<MovieDto> getContent() {
		return content;
	}
	public void setContent(List<MovieDto> content) {
		this.content = content;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotalElement() {
		return totalElement;
	}
	public void setTotalElement(long totalElement) {
		this.totalElement = totalElement;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
}
