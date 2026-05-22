import axios from 'axios'

export const baseURL = "http://localhost:9090"
export const httpClient = axios.create({
    baseURL:baseURL,
    timeout:1000,   //maximum time Axios waits for server response
    headers:{
        'Content-Type' : 'application/json'
    }
})
