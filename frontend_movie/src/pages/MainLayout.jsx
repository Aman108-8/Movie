import React, { useState } from 'react'
import TopMenu from '../Component/TopMenu'
import { Outlet } from "react-router-dom";

const MainLayout = ({onAuthOpen}) => {
  
  const [search, setSearch] = useState("");

  return (
    <div className='bg-[#031519] min-h-screen'>
      {/* Fixed TopMenu */}
      <TopMenu onAuth={onAuthOpen} search={search} setSearch={setSearch}/>
      
      {/* Spacer div with same height as TopMenu to push content down */}
      <div className="h-[5rem]"></div>
      
      {/* Content */}
      <div>
        <Outlet context={search}/>
      </div>
    </div>
  )
}

export default MainLayout