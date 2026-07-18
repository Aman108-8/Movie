import { httpClient } from "./AxiosHelper";

export const loginUser = (data) => {
    return httpClient.post("/auth/users/login", data);
}

export const registerUser = (data) => {
    return httpClient.post("/auth/users/register", data);
}

export const getAllUser = () => {
    return httpClient.get(`/api/users/`);
};

export const deleteUser = (userId) => {
    return httpClient.delete(`/api/users/${userId}`);
}


//movie
export const getAllMovies = (page = 0) => {
    return httpClient.get(`/api/admin/movie/?pageNumber=${page}`);
};

export const addMovie = (data)=>{
  return httpClient.post(`/api/admin/movie/`,data)
}

export const deleteMovies = (movieId) => {
    return httpClient.delete(`/api/admin/movie/${movieId}`);
};

export const searchMovie = (movieName) => {
    return httpClient.get(`/api/admin/movie/search/${movieName}`);
};

export const updateMovie = (movieId, data) => {
  return httpClient.put(`/api/admin/movie/${movieId}`, data);
};

export const getMovieById = (movieId) => {
    return httpClient.get(`/api/admin/movie/${movieId}`)
}

// SCREENSHOTS
export const getScreenshots = (movieId) => {
    return httpClient.get(`/api/admin/movie/${movieId}/screenshots`);
  };
  

export const uploadScreenshots = (movieId, files) => {
  const formData = new FormData();

  files.forEach(file => formData.append("images", file));

  return httpClient.post(
    `/api/admin/movie/${movieId}/screenshots`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data" // ✅ MUST
      }
    }
  );
};

export const updateThumbnail = (movieId, file) => {
  const formData = new FormData();
  formData.append("image", file);

  return httpClient.put(`/api/admin/movie/${movieId}/thumbnail`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

export const updateScreenShot = (selectedSS, movieId, file) => {
  const formData = new FormData();
  formData.append("image", file);

  return httpClient.put(`/api/admin/movie/${movieId}/screenshot/${selectedSS}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

export const deleteScreenshot = (movieId, screenshotId) => {
  return httpClient.delete(`/api/admin/movie/${movieId}/screenshot/${screenshotId}`);
};


// QUALITY
export const getQualities = (movieId) => {
  return httpClient.get(`/api/admin/movie/${movieId}/quality`);
};

export const addQuality = (movieId, data) => {
  return httpClient.post(`/api/admin/movie/${movieId}/quality`, data);
};

export const updateQuality = (qualityId, data) => {
  return httpClient.put(`/api/admin/movie/quality/${qualityId}`, data);
};

export const deleteQuality = (qualityId) => {
  return httpClient.delete(`/api/admin/movie/quality/${qualityId}`);
};