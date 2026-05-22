import React from 'react'
import { Link, useLocation } from 'react-router-dom'
import logo from './name.png'
import { FaUserFriends, FaFilm, FaChartLine, FaCogs, FaCheckCircle, FaBroadcastTower } from "react-icons/fa";

const SideBar = () => {

  const location = useLocation();

  const menuItems = [
    { icon: <FaBroadcastTower />, label: "Dashboard", link: "/admin"  },
    { icon: <FaFilm />, label: "Manage Movies", link:'/admin/movie' },
    { icon: <FaUserFriends />, label: "Manage Users",  link: "/admin/user" },
    { icon: <FaChartLine />, label: "Analytics", },
    { icon: <FaCogs />, label: "Settings" },
    { icon: <FaCheckCircle />, label: "Content Approval"},
  ];

  return (
    <> 
        <div className='w-[280px] h-screen fixed left-0 top-0 bg-menu-bg border-r border-glass-border backdrop-blur-md p-8 pt-6'>
          <div className='flex flex-col'>
            <img src={logo} alt='logo' className='h-10 mb-[5px]'/>
            <h3 className='text-xs text-gray-400 p-1 tracking-wider'>Movie Streaming</h3>
          </div>
          
          <nav className='space-y-4 pt-8 '>
          {menuItems.map((item) => {
          const isActive = location.pathname === item.link;

          return (
            <Link
              key={item.label}
              to={item.link}
              className={`flex items-center gap-4 py-3 px-5 rounded-lg text-sm transition-all duration-300
                ${isActive
                  ? 'bg-cyan-400 text-gray-950 font-bold shadow-[0_0_10px_rgba(34,211,238,0.6)]'
                  : 'text-white hover:bg-glass-fill'}`}
            >
              <span className={`text-xl ${isActive ? '' : 'text-gray-400'}`}>
                {item.icon}
              </span>
              {item.label}
            </Link>
          );
        })}
          </nav>
        </div>
    </>
  )
}

export default SideBar