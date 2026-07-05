import React, { useEffect, useState } from 'react'
import { getAllMovies } from '../Config/authApi'
import { FaSearch, FaPlus } from "react-icons/fa";
import {deleteMovies, searchMovie} from '../Config/authApi'
import { Link } from 'react-router-dom';
import Edit from './Edit';
import { useNavigate } from "react-router-dom";

const Movie = () => {

  const [search, setSearch] = useState("")
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const fetchMovies = async () => {
    try {
      const resp = await getAllMovies();
      setMovies(resp.data.content);
    } catch (error) {
      console.error("Error fetching movies:", error);
    }
  };

  useEffect(() => {
    fetchMovies();
  }, []);

  useEffect(() => {
    if (!search || search.trim() === "") {
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

  const handleDelete = async (id) => {
    try {
      console.log(id)
      await deleteMovies(id);
      setMovies(prev => prev.filter(m => m.id !== id)); // ✅ refresh after delete
    } catch (error) {
      console.error("Delete failed", error);
    }
  };

  return (
    <div className='ml-[18rem] pb-3 space-y-7 px-3'>
      <div className='pt-5 flex flex-row justify-between'>
        <h1 className='text-white text-4xl font-semibold'>Admin Panel</h1>
        <div className='flex gap-x-3'>
        <div className='flex items-center border border-white rounded-lg overflow-hidden group'>
              <input 
                type='text' 
                placeholder="Search" 
                value={search}
                onChange={(e)=>setSearch(e.target.value)}
                className='bg-transparent py-1.5 px-3 text-sm focus:outline-none text-white placeholder:text-white w-54'
              />
              <button type='submit' className='py-1.5 px-3'>
                <FaSearch className='text-gray-400 text-lg'/> 
              </button>
              
        </div>
        <button
          onClick={() => navigate("/admin/edit")}
          className='py-1.5 px-3'
        >
          <FaPlus className='text-gray-400 text-lg hover:text-cyan-300' />
        </button>
        </div>
      </div>
      
      <div className='max-w-[1480px] mx-auto px-1 grid grid-cols-6 gap-x-8 gap-y-6'>
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
            onDelete={handleDelete}
          />
        )))}
        
      </div>
        

          
    </div>
  )
}

export default Movie



const MovieCard = ({ movie, onDelete, onEdit }) => {
  return (
    <div className='flex flex-col gap-y-3'>
      <div className="relative w-[180px] h-[230px] rounded-2xl overflow-hidden">
        <img
          src={`http://localhost:9090/api/movie/thumbnail/image/${movie.thumbnail}`}
          alt={movie.title}
          className="w-full h-full object-cover"
        />
        
        <div className="absolute inset-0 bg-gradient-to-t from-black/50 via-black/10 to-transparent" />

        {/* CONTENT (INSIDE IMAGE) */}
        <div className="absolute bottom-0 w-full p-3 text-white">

          {/* TITLE */}
          <h2 className="text-lg font-bold leading-tight">
            {movie.title}
          </h2>

      </div>
      </div>

      <div className='text-white flex gap-x-2'>
      <ActionButton 
        type="edit" 
        action={() => window.location.href = `/admin/edit/${movie.id}`}
      >
        Edit
      </ActionButton>

        <ActionButton 
          type="delete" 
          action={() => onDelete(movie.id)} // ✅ correct
        >
          Delete
        </ActionButton>
      </div>
    </div>
  );
};

const ActionButton = ({ children, type, action }) => {

  const buttonStyle = {
    boxShadow: `
      0 8px 50px rgba(255,255,255,0.15), 
      inset 10px 10px 15px -10px rgba(255,255,255,0.15), 
      inset -10px 10px 15px -10px rgba(255,255,255,0.15), 
      inset 10px -10px 15px -10px rgba(255,255,255,0.15), 
      inset -10px -10px 15px -10px rgba(255,255,255,0.15)
    `,
    backgroundColor: 'rgba(255, 255, 255, 0.08)'
  };

  const color =
    type === "delete"
      ? "hover:text-red-400"
      : type === "edit"
      ? "hover:text-cyan-400"
      : "hover:text-yellow-400";

  return (
    <button
      className={`font-semibold border px-6 rounded-md text-sm transition duration-300 ${color}
      shadow-[0_0_15px_rgba(34,211,238,0.3)]
      hover:shadow-[0_0_25px_rgba(34,211,238,0.8),0_0_50px_rgba(34,211,238,0.5)]`}
      style={buttonStyle} onClick={action}
    >
      {children}
    </button>
  );
};
