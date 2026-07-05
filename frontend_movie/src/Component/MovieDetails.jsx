import React, { useEffect, useState } from 'react'
import thumbnail from '../assets/tb.png'
import download from '../assets/download.svg'
import video from '../assets/video.svg'
import { useParams } from 'react-router-dom'
import { useNavigate, useOutletContext } from 'react-router-dom';
import * as api from '../Config/authApi'

const MovieDetails = () => {
    const {movieId} = useParams();
    const [screenshots, setScreenshots] = useState([]);
    const [quality, setQuality] = useState([]);
    const [movie, setMovie] = useState();
    const navigate = useNavigate();

    /*const handleDownload = async (url) => {
      try {
        const response = await fetch(url);
    
        const blob = await response.blob();
    
        const downloadUrl = window.URL.createObjectURL(blob);
    
        const a = document.createElement("a");
        a.href = downloadUrl;
        a.download = "movie.mp4";
    
        document.body.appendChild(a);
        a.click();
    
        a.remove();
        window.URL.revokeObjectURL(downloadUrl);
    
      } catch (error) {
        console.error("Download failed", error);
      }
    };*/

    //Instead of waiting for each request to finish: start parallel
    const fetchMovieData = async () => {
        try {
          const [movieRes, screenshotRes, qualityRes] =
            await Promise.all([
              api.getMovieDetails(movieId),
              api.getScreenshots(movieId),
              api.getQualities(movieId)
            ]);
      
          console.log("Movie API:", movieRes.data);
          console.log("Screenshot API:", screenshotRes.data);
          console.log("Quality API:", qualityRes.data);
      
          setMovie(movieRes.data);
          setScreenshots(screenshotRes.data);
          setQuality(qualityRes.data);
      
        } catch (error) {
          console.error(error);
        }
      };
      
    useEffect(() => {
        fetchMovieData();
    }, [movieId]);

    if (!movie) {
        return (
          <div className="text-white text-center mt-10">
            Loading...
          </div>
        );
      }

  return (
    <div className='mx-6 mt-12' >
        <div className='flex gap-x-6'>
            <img src={`http://localhost:9090/api/movie/thumbnail/image/${movie.thumbnail}`} className="w-[170px] h-[270px] object-cover rounded-xl"/>

            <div className='flex flex-col gap-5 text-white flex-1'>
                <div className='flex justify-between'>
                    <h2 className='text-4xl text-white font-semibold'>{movie?.title} <span className='font-light'>{`(${movie?.releaseYear})`}</span></h2>
                    <div className='bg-[#e0ff1437] px-7 rounded-full'>
                        <div className='flex py-2'>
                            <h3 className='text-white font-semibold text-2xl'>{movie?.rating}/</h3>
                            <p className='text-white font-semibold text-2xl'>10</p>
                        </div>
                    </div>
                </div>
                <p >{movie?.genre}</p>
                <div className="h-[170px] overflow-y-auto pr-3">
                    <p className="text-sm leading-7 text-gray-300">
                        {movie?.description}
                    </p>
                </div>

                <div className="w-full h-[2px] bg-cyan-300 shadow-[0_0_5px_#22d3ee,0_0_20px_#22d3ee]"></div>
                <div className='flex flex-col gap-3'>
                    <h3 className='font-semibold text-xl'>Trailor</h3>
                    <div className="flex gap-x-8 w-full overflow-x-auto whitespace-nowrap p-2 scrollbar-thin">
                        
                    {screenshots.map((ss) => (
                        <img src={`http://localhost:9090/api/movie/screenshot/image/${ss.imagePath}`} className="w-[250px] h-[120px] rounded-md object-cover " key={ss.id} alt={movie?.title}/>
                    ))}
                        
            
                    </div>
                </div>

                <div className='flex flex-col gap-3 mt-4'>
                    <h3 className='font-semibold text-xl pb-3'>Download</h3>
                    <div className='flex justify-between h-[6rem]'>
                    {quality.map((q) => (
                        <div
                            key={q.id}
                            href={q.link}
                            target="_blank"
                            rel="noreferrer"
                            className="h-[4rem] flex items-center gap-8 px-5 py-1
                            border border-cyan-400 rounded-full
                            backdrop-blur-md
                            shadow-[0_0_10px_rgba(34,211,238,0.7)]
                            hover:shadow-[0_0_25px_rgba(34,211,238,0.8),0_0_50px_rgba(34,211,238,0.5)]
                            transition-all duration-300 hover:scale-105
                            no-underline cursor-pointer"
                            onClick={() =>
                              navigate(`/download/${movie.id}`, {
                                state: {
                                  title: movie.title,
                                  quality: q.quality,
                                  url: q.link
                                }
                              })
                            }
                        >
                            <img
                            src={video}
                            width="40"
                            alt=""
                            />

                            <span className="text-white text-xl font-semibold">
                            {q.quality}
                            </span>

                            <img
                            src={download}
                            width="45"
                            alt=""
                            />
                        </div>
                        ))}
                    </div>
                </div>
                
            </div>
        </div>
    </div>
  )
}

export default MovieDetails

