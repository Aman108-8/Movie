import axios from "axios";
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

export const getMovieDetails = (movieId) => {
    return httpClient.get(`api/movie/${movieId}`)
}

export const getScreenshots=(movieId)=>{
    return httpClient.get(`api/movie/${movieId}/screenshots`)
}

export const getQualities=(movieId)=>{
    return httpClient.get(`api/movie/${movieId}/quality`)
}

export const downloadMovie = (url, quality) => {
    return axios.post(
        "http://localhost:9090/api/download",
        { url, quality },
        {
            timeout: 0 // no timeout
        }
    );
};

export const getDownloadProgress = () => {
    return httpClient.get(
        "/api/download/progress"
    );
};

export const cancelDownload = () => {
    return httpClient.post(
        "/api/download/cancel"
    );
};

export const pauseDownload = () => {
    return httpClient.post(
        "/api/download/pause"
    );
}

export const resumeDownload = () => {
    return httpClient.post(
        "/api/download/resume"
    );
}

//amount
export const createPaymentOrder = (amount) => {
    return axios.post("http://localhost:9090/api/payment/create-order", { amount });
};

export const verifyPayment = (data) => {
    return axios.post("http://localhost:9090/api/payment/verify", data);
};