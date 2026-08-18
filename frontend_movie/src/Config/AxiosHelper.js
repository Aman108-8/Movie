import axios from 'axios'

export const baseURL = import.meta.env.VITE_API_URL
export const httpClient = axios.create({
    baseURL:baseURL,
    timeout:5000,   //maximum time Axios waits for server response
    headers:{
        'Content-Type' : 'application/json'
    }
})

// Automatically attach JWT token
httpClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);
