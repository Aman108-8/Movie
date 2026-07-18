import React, { useEffect, useState } from 'react'
import { deleteUser, getAllUser } from '../Config/authApi';
import { FaTrash } from "react-icons/fa";
import { toast } from 'react-toastify';

const User = () => {
  
  const bgStyle = {
    boxShadow: `
      0 8px 50px rgba(255,255,255,0.15), 
      inset 10px 10px 15px -10px rgba(255,255,255,0.15), 
      inset -10px 10px 15px -10px rgba(255,255,255,0.15), 
      inset 10px -10px 15px -10px rgba(255,255,255,0.15), 
      inset -10px -10px 15px -10px rgba(255,255,255,0.15)
    `,
    backgroundColor: 'rgba(255, 255, 255, 0.08)'
  };

  const[users, setUser] = useState([]);

  const fetchUsers = async () => {
    try {
      toast.warn("wait users are loading");
      const resp = await getAllUser();
      setUser(resp.data.content || resp.data);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const handleDeleteUser = async (userId) => {
    try {
      // Call the API to delete the user
      await deleteUser(userId);
      toast.success("User deleted successfully");
      // Refresh the user list
      fetchUsers();
    } catch (error) {
      console.error("Error deleting user:", error);
      toast.error("Failed to delete user");
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);
  
  return (
    <>
      <div className='ml-[18rem] pb-3 space-y-7 px-3'>
        <div className='max-w-[1480px]'>
          <div className='w-full'>
            <h1 className='text-white text-4xl font-semibold'>User Management</h1>
            
            <table className="w-full border-separate border-spacing-y-4">
              <thead className="text-white" style={bgStyle}>
                <tr className="text-left ">
                  <th className="px-[3rem] py-2 text-center">User ID</th>
                  <th className="px-[3rem] py-2 text-center">Name</th>
                  <th className="px-[3rem] py-2 text-center">Status</th>
                  <th className="px-[3rem] py-2 text-center">Action</th>
                </tr>
              </thead>
              
              <tbody>
                {(users||[]).map((user, index)=>(
                  <tr key={user.id} className="bg-white/5 text-white hover:bg-white/10 transition">
  
                  <td className="px-4 py-2 text-center border-b border-white/10">
                    {String(index+1).padStart(2,"0")}
                  </td>
                
                  <td className="px-4 py-2 text-center border-b border-white/10">
                    {user.name}
                  </td>
                
                  <td className="px-4 py-2 text-center border-b border-white/10">
                    {user.email}
                  </td>
                  
                    <td className="px-4 py-1 text-white text-center">
                    <button
                    className="p-2 rounded-md transition duration-300
                    hover:bg-red-500/20 hover:text-red-400
                    hover:shadow-[0_0_20px_rgba(255,0,0,0.6)]"
                    onClick={() => handleDeleteUser(user.id)}
                  >
                    <FaTrash className="text-lg" />
                  </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  )
}

export default User

const userCard = () => {
  
}