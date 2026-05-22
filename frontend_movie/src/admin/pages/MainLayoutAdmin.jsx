import React, { useState } from 'react'
import SideBar from '../Component/SideBar'
import { Link, Outlet } from 'react-router-dom'

const MainLayoutAdmin = () => {
  const [search, setSearch] = useState("");
  return (
    <div className='flex w-full'>
      <Link to='/admin'>
        <SideBar/>
      </Link>
      

        <Outlet context={{ search, setSearch }} />
    </div>
  )
}

export default MainLayoutAdmin