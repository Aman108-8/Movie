import React from 'react'
import { useEffect, useState } from 'react'
import { getAllMovies, searchMovie } from '../Config/authApi'
import { useOutletContext } from 'react-router-dom';
import {FaPlus} from "react-icons/fa";

const Movie = () => {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(false);
  const search = useOutletContext();

  const fetchMovies = async () => {
    try {
      const resp = await getAllMovies();
      setMovies(resp.data.content);
    } catch (error) {
      console.error("Error fetching movies:", error);
    }
  }

  useEffect(()=>{
    fetchMovies();
  }, []);

  useEffect(() => {
    if (!search || search.trim === "") {
      fetchMovies(); // show all movies
      return;
    }
  
    const delay = setTimeout(async () => {
      try {
        //console.log("Searching:", search);
        setLoading(true);

        const resp = await searchMovie(search);
        setMovies(resp.data.content || resp.data);

        setLoading(false);
      } catch (error) {
        console.error("Search error:", error);
        setMovies([]);       // ✅ show "No Movies Found"
        setLoading(false);  
      }
    }, 400);
  
    return () => clearTimeout(delay);
  
  }, [search]);
  
  return (
    <div className='h-screen'>
      <div className="sticky top-16 z-40  pb-0 pt-6">
        <div className="max-w-[1480px] mx-auto px-4 py-2 bg-[#031519]">
          <div className="flex gap-6">

            <div className="flex items-center gap-2">
              <span className="text-gray-400 font-semibold">Sort By</span>
              <div className="border border-gray-600 px-3 py-1 rounded-xl">
                <span className="text-white text-sm">Release Date</span>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-gray-400 font-semibold">Filter</span>
              <div className="border border-gray-600 px-3 py-1 rounded-xl">
                <span className="text-white text-sm">All Genres</span>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div className='max-w-[1480px] mx-auto pt-4 px-1 grid grid-cols-6 gap-x-6 gap-y-6'>
      {
        loading ? (
        <p className="text-white text-center mt-10">Searching...</p>
      ) : movies.length === 0 ?(
            <div className="text-white text-center mt-20 ">
              <h2 className="text-2xl font-semibold text-gray-300">
                😕 No Movies Found
              </h2>
              <p className="text-gray-500 mt-2">
                Try searching something else
              </p>
            </div>
      ):(
        movies.map((movie) => (
          <MovieCard 
            key={movie.id} 
            movie={movie}
          />
        )))}
        
      </div>
    </div>
  )
}



export default Movie



const MovieCard = ({movie}) => {
  return (
    <div className="group relative w-[200px] h-[300px] rounded-2xl overflow-hidden cursor-pointer
      transition-all duration-300 hover:scale-105
      shadow-[0_0_15px_rgba(34,211,238,0.3)]
      hover:shadow-[0_0_25px_rgba(34,211,238,0.8),0_0_50px_rgba(34,211,238,0.5)]">

      {/* IMAGE */}
      <img
        src={`http://localhost:9090/api/movie/thumbnail/image/${movie.thumbnail}`}
        alt={movie.title}
        className="w-full h-full object-cover"
      />

      {/* DARK OVERLAY */}
      <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/10 to-transparent" />

        {/* CONTENT (INSIDE IMAGE) */}
        <div className="absolute bottom-0 w-full p-3 text-white">

          {/* TITLE */}
          <h2 className="text-lg font-bold leading-tight">
            {movie.title}
          </h2>

          {/* RATING */}
          <div className="flex text-yellow-400 text-sm my-1">
            ⭐⭐⭐⭐⭐
          </div>

          {/* BUTTONS */}
          {/*<div className="flex gap-2 mt-2">
            <button className="bg-cyan-400 text-black text-xs px-3 py-1 rounded-md hover:bg-cyan-300">
              Play
            </button>
            <button className="bg-white/20 text-white text-xs px-3 py-1 rounded-md hover:bg-white/30">
              Details
            </button>
          </div>*/}

      </div>

      {/* GLOW BORDER */}
      <div className="absolute inset-0 rounded-2xl border border-cyan-400/40 opacity-0 group-hover:opacity-100 transition"></div>

    </div>
  )
}