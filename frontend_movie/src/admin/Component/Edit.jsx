import React, { useEffect, useState } from 'react'
import {useParams } from 'react-router-dom'
import { getMovieById } from '../Config/authApi'
import EditForm from './EditForm';

const Edit = () => {

  const { movieId } = useParams(); // ✅ get ID from URL
  const [movie, setMovie] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchMovie = async () => {
    if (!movieId) {
      setLoading(false);
      return;
    }
    
    try {
      const resp = await getMovieById(movieId);
      setMovie(resp.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // ADD MODE
    if (!movieId) {
      setMovie({
        title: "",
        description: "",
        genre: "",
        rating: "",
        releaseYear: "",
        thumbnail: ""
      });
  
      return;
    }
    
    fetchMovie();

  }, [movieId]);

  useEffect(() => {
    fetchMovie();
  }, [movieId]);

  if (!movie) {
    return <p className='text-white'>Loading...</p>;
  }

  return <EditForm movie={movie} />;
};

export default Edit;


