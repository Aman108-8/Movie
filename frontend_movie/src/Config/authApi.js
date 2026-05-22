import { httpClient } from "./AxiosHelper";

export const loginUser = (data) => {
    return httpClient.post("/auth/users/login", data);
}

export const registerUser = (data) => {
    return httpClient.post("/auth/users/register", data);
}

export const getAllMovies = (page = 0) => {
    return httpClient.get(`/api/movie/?pageNumber=${page}`);
};

export const searchMovie = (movieName) => {
    return httpClient.get(`/api/movie/search/${movieName}`);
};