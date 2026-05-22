import React, { useEffect, useRef, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { updateMovie, deleteScreenshot, getScreenshots, getQualities, updateThumbnail, updateScreenShot, uploadScreenshots, updateQuality, deleteQuality, addQuality} from '../Config/authApi'
import thumbnail from '../assets/tb.png'
import { FaArrowLeft,FaTrash, FaPlusCircle, FaCamera} from "react-icons/fa";
import defaultImg from '../assets/default.png';

const EditForm = ({movie}) => {
    const inputClass = "text-white bg-glass-fill border border-glass-border rounded-lg text-sm text-gray-400 px-4 py-2.5 focus:outline-none focus:border-cyan-400";
    const labelClass = "text-sm text-cyan-400 font-semibold mb-2 block"; // Problem 3: addDriveIcon, use cyan accent here
    
    

    const boxStyle = {
        boxShadow: `
          0 8px 50px rgba(255,255,255,0.15), 
          inset 10px 10px 15px -10px rgba(255,255,255,0.15), 
          inset -10px 10px 15px -10px rgba(255,255,255,0.15), 
          inset 10px -10px 15px -10px rgba(255,255,255,0.15), 
          inset -10px -10px 15px -10px rgba(255,255,255,0.15)
        `,
        backgroundColor: 'rgba(255, 255, 255, 0.08)'
      };

    const inputStyle = {
        boxShadow: `
            0 8px 50px rgba(255,255,255,0.15), 
            inset 10px 10px 15px -10px rgba(255,255,255,0.15), 
            inset -10px 10px 15px -10px rgba(255,255,255,0.15), 
            inset 10px -10px 15px -10px rgba(255,255,255,0.15), 
            inset -10px -10px 15px -10px rgba(255,255,255,0.15)
        `,
        backgroundColor: 'rgba(255, 255, 255, 0.08)'
      };

      const [formData, setFormData] = useState({
        title: "",
        description: "",
        genre: "",
        rating: "",
        releaseYear: "",
        thumbnail: ""
      });

      const [screenshots, setScreenshots] = useState([]);
      const [quality, setQuality] = useState([]);
      const [savingId, setSavingId] = useState(null); // Optional: for loading state
      const  [editingField, setEditingField] = useState(null);
      const fileThumbInputRef = useRef();
      const fileMultiSSInputRef = useRef();
      //const [selectedSS, setSelectedSS] = useState(null);
      const selectedSSRef = useRef(null);
      
      useEffect(() => {
        if (movie) {
          setFormData({
            title: movie.title || "",
            description: movie.description || "",
            genre: movie.genre || "",
            rating: movie.rating || "",
            releaseYear: movie.releaseYear || "",
            thumbnail: movie.thumbnail || ""
          });
        }
      }, [movie]);

      useEffect(() => {
        if (movie?.id) {
          fetchScreenshots(movie.id);
        }
      }, [movie]);

      useEffect(() => {
        if (movie?.id) {
          fetchQualities(movie.id);
        }
      }, [movie]);
      
      const fetchScreenshots = async (movieId) => {
        try {
          const res = await getScreenshots(movieId);
          console.log("Screenshots:", res.data);
          console.log("Screenshot object:", screenshots);
          setScreenshots(res.data);
        } catch (error) {
          console.error("Error fetching screenshots", error);
        }
      };

      const fetchQualities = async (movieId) => {
        try {
          const res = await getQualities(movieId);
          console.log("getQualities:", res.data);
          console.log("getQualities object:", screenshots);
          setQuality(res.data);
        } catch (error) {
          console.error("Error fetching screenshots", error);
        }
      };

      const handleMovieSave = async () => {
        try {
          const res = await updateMovie(movie.id, formData);
      
          console.log("Saved:", res.data);
      
        } catch (err) {
          console.error("Error saving movie:", err);
        }
      };

      const handleChange = (e) => { 
        const { name, value } = e.target; 
        setFormData(prev => (
          { 
            ...prev, [name]: value 
          }
        )); 
      };

      const handleThumbnailClick = () => {
        fileThumbInputRef.current.click();
      };

      //handle change and add screenshot
      const handleScreenshotClick = (ssId) => {
        console.log("Clicked screenshot ID:", ssId); // Debug log
        selectedSSRef.current = ssId; // Store in ref instead of dataset
        fileMultiSSInputRef.current.click();
      };

      const handleThumbnailChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;
      
        try {
          const res = await updateThumbnail(movie.id, file);

          // ✅ BEST: use backend response
          const newFileName = res.data;

          // 🔥 refresh thumbnail
          setFormData(prev => ({
            ...prev,
            thumbnail: newFileName // or refetch from API if needed
          }));
      
        } catch (err) {
          console.error("Thumbnail update failed", err);
        }
      };

      const handleAddScreenShot = async (e) => {
        const files = Array.from(e.target.files); // ✅ convert FileList → Array
      
        if (!files.length) return;
      
        try {
          await uploadScreenshots(movie.id, files);
          
          console.log('upload success')

          fetchScreenshots(movie.id)
      
        } catch (err) {
          console.error("Screenshot update failed", err);
        }
      };

      const handleDeleteScreenshot = async (ssId) => {
        try {
          await deleteScreenshot(movie.id, ssId);
          console.error("Delete success",ssId);
          // ✅ remove from UI instantly
          setScreenshots(prev => prev.filter(ss => ss.id !== ssId));
      
        } catch (err) {
          console.error("Delete failed", err);
        }
      };

      const handleQualityChange = (e, id, field) => {
        const { value } = e.target;
        
        // Update local state immediately
        setQuality(prev =>
          prev.map(item =>
            item.id === id
              ? { ...item, [field]: value }
              : item
          )
        );
      };
    
      
      const handleSaveLink = async (q) => {
        try {
          setSavingId(q.id); // Optional: show saving indicator

          if(q.isNew){
            const res = await addQuality(
              movie.id, {
                quality : q.quality,
                link: q.link
              }
            )

            setQuality(prev =>prev.map(item=>
              item.id === q.id ? res.data : item
            ));
          }
          
          else {
            // 🟢 UPDATE EXISTING
            await updateQuality(q.id, {
              quality: q.quality,
              link: q.link
            });
          }
      
          setEditingField(null);
      
        } catch (err) {
          console.error("Error saving:", err);
        } finally {
          setSavingId(null);
          setEditingField(null); // 🔥 exit edit mode
        }
      };

      const addQualityRow = () =>{
        const tempId = Date.now();

        const newRow = {
          id : tempId,
          quality : "",
          link: "",
          isNew: true
        }

        setQuality(prev => [...prev, newRow]);

        setEditingField({id : tempId, field: "quality"});
      }

      const handleQualityDelete = async (qid) =>{
        try {
          await deleteQuality(qid);
          console.log("successfully quality deleted");
          setQuality(prev => prev.filter(q => q.id !== qid));
        } catch (error) {
          console.log("failed quality deleted", error);
        }
      }
    
    return(
        <div className='pt-3 ml-[280px] pl-[1.5rem] space-y-4 h-[100vh]  overflow-hidden' style={boxStyle}>
            <div className='flex flex-row gap-x-3 items-center'>
                <button className='text-gray-400 text-xl py-auto'>
                  <Link to='/admin/movie'>
                    <FaArrowLeft/>
                  </Link>
                </button>
                <h1 className='text-white font-medium text-3xl'>ASTRO QUEST</h1>
            </div>

            <div className='flex flex-row gap-x-[2rem] pr-7'>
                <div 
                className=" relative bg-white/10 border border-glass-border rounded-3xl backdrop-blur-[25px] px-10 pt-7 z-10 "
                style={boxStyle}
                >
                    <div className=''>
                        <h1 className='text-white font-medium text-xl pb-4'>Basic Information</h1>
                            <div className="flex flex-col gap-x-10 gap-y-3 ">
                                <div>
                                    <label className={labelClass}>Title</label>
                                    <input 
                                        type='text'
                                        name='title'
                                        value={formData.title}
                                        onChange={handleChange}
                                        className={`w-[30rem] ${inputClass}`}
                                        style={inputStyle}
                                        />
                                </div>
                                
                                <div className="col-span-2">
                                    <label className={labelClass}>Description</label>
                                    <textarea 
                                        name='description'
                                        value={formData.description}
                                        onChange={handleChange}
                                        className={`${inputClass} h-32 resize-none w-[30rem]`}
                                        style={inputStyle}
                                        />
                                </div>
                                <div className='flex justify-between pb-5'>
                                    <div className='flex flex-col gap-y-3'>
                                        <div>
                                            <label className={labelClass}>Genre</label>
                                            <input 
                                            type='text'
                                            name='genre'
                                            value={formData.genre}
                                            onChange={handleChange}
                                            className={`w-[9rem] ${inputClass}`}
                                            style={inputStyle}
                                            />
                                        </div>
                                        <div>
                                            <label className={labelClass}>Thumbnail</label>
                                            <div className="group relative w-[130px] h-[170px] cursor-pointer" onClick={handleThumbnailClick}>

                                              <img
                                                src={
                                                  formData.thumbnail? `http://localhost:9090/api/movie/thumbnail/image/${formData.thumbnail}?t=${Date.now()}`
                                                  : defaultImg
                                              }
                                                className="w-full h-full object-cover rounded-lg"
                                              />

                                              {/* 🔥 Overlay - won't block interactions */}
                                              <div className="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 flex items-center justify-center rounded-lg transition duration-300 pointer-events-none">
                                                <FaCamera className="text-white text-xl" />
                                              </div>

                                          </div>
                                            <input
                                              type="file"
                                              accept="image/*"
                                              ref={fileThumbInputRef}
                                              onChange={handleThumbnailChange}
                                              className="hidden"
                                            />
                                        </div>
                                    </div>
                                    <div className='flex flex-col justify-between item'>
                                        <div className='flex flex-col gap-y-4'>
                                            <div>
                                                <label className={labelClass}>Rating (0.0 - 10.0)</label>
                                                <input 
                                                    type='number'
                                                    name='rating'
                                                    value={formData.rating}
                                                    onChange={handleChange}
                                                    step="0.1"
                                                    min="0"
                                                    max="10"
                                                    className={`w-[15rem] ${inputClass}`}
                                                    style={inputStyle}
                                                    />
                                            </div>
                                            <div>
                                                <label className={labelClass}>Release Year</label>
                                                <input 
                                                    type='number'
                                                    name='releaseYear'
                                                    value={formData.releaseYear}
                                                    onChange={handleChange}
                                                    className={`w-[15rem] ${inputClass}`}
                                                    style={inputStyle}
                                                    />
                                            </div>
                                        </div>
                                        <button className='bg-cyan-400 text-gray-950 font-bold py-2 ml-[60%]  rounded-lg text-xs hover:bg-cyan-300 transition-all duration-300' onClick={handleMovieSave}>Save</button> 
                                    </div>
                                </div>
                            </div>
                        </div>
                </div>

                <div className='w-[610px] flex flex-col justify-between'> 
                    <div className="relative bg-white/10 border border-glass-border rounded-3xl backdrop-blur-[25px] px-10 py-7 z-10" style={boxStyle} > 
                        
                        <div className='flex justify-between pb-4 items-center'>
                            <h1 className='text-white font-medium text-xl text-center'>Media Assist</h1>
                            <button className='flex items-center gap-2 text-cyan-400 text-sm font-semibold hover:text-cyan-300 transition' onClick={() => fileMultiSSInputRef.current.click()}>
                                <FaPlusCircle className='text-lg' /> Add Screenshot 
                            </button>
                        </div>

                        <div className='flex flex-wrap gap-3 overflow-y-auto max-h-[165px] h-[165px] pr-2'>
                        {screenshots.length > 0 ? 
                          (
                            screenshots.map((ss, index) => (
                              <div
                                key={ss.id}
                                onClick={(e) => {
                                  handleScreenshotClick(ss.id)
                                }}
                                className="group relative w-[150px] h-[80px] cursor-pointer"
                              >
                                <img
                                  src={`http://localhost:9090/api/movie/screenshot/image/${ss.imagePath}?t=${Date.now()}`}
                                  className="w-full h-full object-cover rounded-lg"
                                />

                                {/* 🔥 Overlay */}
                                <div className="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 flex items-center justify-center rounded-lg transition duration-300">
                                  <FaCamera className="text-white text-xl" />
                                </div>

                                {/* ❌ Delete Button */}
                                <button
                                  onClick={(e) => {
                                  e.stopPropagation(); // ✅ STOP parent click
                                  handleDeleteScreenshot(ss.id)}}
                                  className="absolute top-2 right-2 bg-red-500 hover:bg-red-600 text-white p-1 rounded-full opacity-0 group-hover:opacity-100 transition duration-300 z-10"
                                >
                                  <FaTrash className="text-sm" />
                                </button>
                              </div>
                          ))
                          
                        ) : (
                          <p className="text-gray-400 text-sm w-full text-center mt-6">
                            No Screenshots Available
                          </p>
                        )}
                        </div>
                        <input
                            type="file"
                            accept="image/*"
                            multiple
                            ref={fileMultiSSInputRef}
                            onChange={handleAddScreenShot}
                            className="hidden"
                          />
                    </div>  
                        
                    <div className='max-w-[1480px] mx-auto mt-1 space-y-8 z-50' > 
                      <div className='flex flex-col mb-2 bg-glass-fill border border-glass-border rounded-3xl backdrop-blur-md overflow-hidden'> 
                          <div className='flex justify-between py-4 px-6 items-center'> 
                              <h1 className='text-white font-medium text-xl'>Media Quality</h1> 
                              <button className='flex items-center gap-2 text-cyan-400 text-sm font-semibold hover:text-cyan-300 transition' onClick={addQualityRow}> 
                                  <FaPlusCircle className='text-lg' /> Add New Quality 
                              </button> 
                          </div> 
                              
                          <div className='flex-1 p-5 pt-0 overflow-hidden'> 
                              <div className='max-h-[240px] h-[240px] overflow-y-auto pr-3 scrollbar-thin scrollbar-thumb-white/10'> 
                                  <table className="text-white w-full text-left text-xs table-fixed"> 
                                      <thead className="bg-white/10 border-b border-white/15"> 
                                          <tr className="uppercase text-gray-400 tracking-wider"> 
                                              <th className='py-4 px-6 font-semibold w-[20%]'>Quality</th> 
                                              <th className='py-4 px-6 font-semibold w-[60%]'>Link</th> 
                                              <th className='py-4 px-6 font-semibold w-[20%] text-center'>Action</th>
                                          </tr> 
                                      </thead> 
                                      <tbody className='odd:bg-white/5 border-glass-border text-[#CACACA]'> 
                                        
                                        {quality.length > 0 ? (
                                            quality.map((q, index) => (
                                              <tr key={q.id || index} className='border-b border-white/10 hover:bg-white/5'>
                                                
                                                {/* Quality */}
                                                <td className='py-5 px-6 font-medium w-[20%]'>
                                                {editingField?.id === q.id && editingField?.field === "quality" ? (
    
                                                  <input
                                                    type="text"
                                                    value={q.quality}
                                                    autoFocus
                                                    onChange={(e) => handleQualityChange(e, q.id, "quality")}
                                                    onBlur={() => handleSaveLink(q)}
                                                    className="bg-search-fill border border-cyan-400 rounded-md px-2 py-1 text-xs w-full"
                                                  />

                                                ) : (
                                                  
                                                  <span
                                                    onClick={() => setEditingField({ id: q.id, field: "quality" })}
                                                    className="cursor-pointer hover:text-cyan-400"
                                                  >
                                                    {q.quality || "Click to edit"}
                                                  </span>

                                                )}
                                                </td>

                                                {/* Link */}
                                                <td className='py-5 px-6 w-[60%]'>
                                                  <input
                                                    type="text"
                                                    value={q.link}
                                                    onChange={(e) => handleQualityChange(e, q.id, "link")}
                                                    onBlur={() => handleSaveLink(q)}
                                                    className='text-[#CACACA] bg-search-fill border border-glass-border 
                                                    focus:border-cyan-400 rounded-md px-3 py-2 text-xs font-mono w-full'
                                                    disabled={savingId === q.id} // Optional: disable while saving
                                                  />
                                                  {savingId === q.id && (
                                                    <span className="text-xs text-cyan-400 ml-2">Saving...</span>
                                                  )}
                                                </td>

                                                {/* Delete Button */}
                                                <td className='py-5 px-6 w-[20%] text-center'>
                                                  <button 
                                                    onClick={() => handleQualityDelete(q.id)}
                                                    className='bg-red-500/20 hover:bg-red-500 text-red-400 hover:text-white 
                                                    font-bold py-2 px-4 rounded-lg text-xs transition-all duration-300'
                                                  >
                                                    Delete
                                                  </button>
                                                </td>

                                              </tr>
                                            ))
                                          ) : (
                                            <tr>
                                              <td colSpan="3" className="text-center py-6 text-gray-400 text-sm">
                                                No Quality Available
                                              </td>
                                            </tr>
                                          )}
                                      </tbody> 
                                  </table> 
                              </div> 
                          </div> 
                      </div> 
                    </div> 
                </div> 
            </div> 
        </div> 
    ) 
}

export default EditForm