import React from 'react'
import name from '../assets/name.png'
import { FaSearch } from "react-icons/fa";
import { Link } from "react-router-dom";

const TopMenu = ({onAuth, search, setSearch}) => {
  return (
    <div className="fixed top-0 left-0 right-0 z-[100] bg-[#031519] text-white shadow-[0_10px_30px_rgba(0,0,0,0.6)] rounded-bl-md rounded-br-md" style={{
                    
      // Technique 2: Corner Shading (Four subtle inset shadows)
      boxShadow: `
          0 8px 50px rgba(255,255,255,0.15), 
          inset 10px 10px 15px -10px rgba(255,255,255,0.15), 
          inset -10px 10px 15px -10px rgba(255,255,255,0.15), 
          inset 10px -10px 15px -10px rgba(255,255,255,0.15), 
          inset -10px -10px 15px -10px rgba(255,255,255,0.15)
      `,
      backgroundColor: '#031519' // Base translucency
  }}>
      <div className='w-11/12 mx-auto py-4'>
        <div className='flex items-center justify-between'>
          
          <div className='flex items-center gap-16'>
            {/* Logo */}
            <div className='flex flex-col'>
              <img src={name} className='h-10' alt="logo" />
              <h3 className='text-sm mt-[-5px] p-1'>Streaming</h3>
            </div>

            {/* Menu */}
            <div className='flex gap-9 items-center h-10 text-sm'>
              <Link to='#' className='hover:text-cyan-400 font-medium'>Home</Link>
              <Link to='#' className='hover:text-cyan-400 font-medium'>Movies</Link>
              <Link to='#' className='hover:text-cyan-400 font-medium'>WebSeries</Link>
              <Link to='#' className='hover:text-cyan-400 font-medium'>Anime</Link>
              <Link to='#' className='hover:text-cyan-400 font-medium'>New Release</Link>
              <Link to='/pricing' className='hover:text-cyan-400 font-medium'>Pricing</Link>
            </div>
          </div>

          <div className='flex flex-row gap-7'>
            <div className='flex items-center border border-white rounded-lg overflow-hidden group'>
              <input 
                type='text' 
                placeholder="Search" 
                value={search}
                onChange={(e)=>setSearch(e.target.value)}
                className='bg-transparent py-1.5 px-3 text-sm focus:outline-none placeholder:text-white w-54'
              />
              <button type='submit' className='py-1.5 px-3'>
                <FaSearch className='text-gray-400 text-lg'/>
              </button>
            </div>
            <button
              onClick={() => onAuth("Login")}
              className="text-sm font-semibold border border-gray-600 px-4 py-1.5 rounded-md hover:bg-white hover:text-gray-900 transition"
            >
              Login
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default TopMenu