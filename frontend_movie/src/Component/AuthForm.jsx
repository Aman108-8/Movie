import React, { useEffect, useState } from 'react'
import name from '../assets/name.png'
import { FaUser, FaGoogle, FaFacebook } from "react-icons/fa";
import { FiEyeOff, FiEye, FiMail} from "react-icons/fi";
import { loginUser, registerUser } from '../Config/authApi';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

const AuthForm = ({onAuth, setAuthType, setShowAuth}) => {

    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const[loginFormData, setLoginFormData] = useState({
        email:'',
        password:'',
        rememberMe:false
    })

    const[signInFormData, setSignInFormData] = useState({
        name:'',
        email:'',
        password:'',
        rememberMe:false
    })

    const handleLoginChange = (e) => {
        const {name, value, type, checked} = e.target;

        setLoginFormData({
            ...loginFormData,
            [name]:type === 'checkbox' ? checked : value
        })
    }

    const handleSignUpChange = (e) => {
        const {name, value, type, checked} = e.target;

        setSignInFormData({
            ...signInFormData,
            [name]:type === 'checkbox' ? checked : value
        })
    }

    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        
        try {
            const response = await loginUser(loginFormData);
            console.log("Login Success:", response.data);
            localStorage.setItem("token", response.data.token);

            toast.success("Login successful!");
            setShowAuth(false);
            navigate('/');
        } catch (error) {
            console.error("Login Error:", error.response?.data || error.message);
        }

    };
    
    const handleSignUpSubmit = async (e) => {
        e.preventDefault();
    
        try {
            const response = await registerUser(signInFormData);
            toast.success("Signup successful! Please log in.");
            console.log("Signup Success:", response.data);
            setAuthType("Login");
        } catch (error) {
            console.error("Signup Error:", error.response?.data || error.message);
        }
    };

    useEffect(()=>{
        console.log(onAuth);
    },[onAuth])

    return (
        <div className='flex justify-center items-center px-4 mt-9'>
            {/*<div className='h-auto w-full max-w-[420px] backdrop-blur-md border rounded-xl border-white/20 p-8 shadow-[0_8px_50px_rgba(255,255,255,0.15)] text-white'>
            */}
                <div className='h-auto w-full max-w-[420px] rounded-xl p-8 text-white text-center'
                style={{
                    
                    // Technique 2: Corner Shading (Four subtle inset shadows)
                    boxShadow: `
                        0 8px 50px rgba(255,255,255,0.15), 
                        inset 10px 10px 15px -10px rgba(255,255,255,0.15), 
                        inset -10px 10px 15px -10px rgba(255,255,255,0.15), 
                        inset 10px -10px 15px -10px rgba(255,255,255,0.15), 
                        inset -10px -10px 15px -10px rgba(255,255,255,0.15)
                    `,
                    backgroundColor: 'rgba(255, 255, 255, 0.08)' // Base translucency
                }}
            >
                <div className='flex flex-col item-center mb-8 ' >
                <img src={name} alt="MovieGalaxy Logo" className='h-13 mb-[-15px]'/>
                    <h4 className='text-xs text-gray-400 tracking-wider'>MOVIE STREAMING</h4>
                    <h1 className='text-3xl font-bold mt-3'>Welcome Back!</h1>
                    <p className='text-gray-400 mt-1 text-sm'>{onAuth} to continue your cinematic journey</p>
                </div>

                <form className={onAuth === 'Login' ? "space-y-4" : "space-y-2"} onSubmit={onAuth === 'Login' ? handleLoginSubmit : handleSignUpSubmit}>

                    {onAuth==="Login"? 
                        (
                            <>
                        <div className='relative'>
                            <FaUser className="absolute left-3.5 top-1/2 -translate-y-1/2 text-white text-lg" />
                            <input type='text' placeholder="Enter UserName or Email" name="email" value={loginFormData.email} onChange={handleLoginChange} className='w-full py-1.5 pl-11 pr-4 rounded-md bg-transparent focus:outline-none placeholder:text-white border focus:border-cyan-400 text-sm'/>
                        </div>

                        <div  className='relative'>
                            {/* Toggle Icon */}
                            {showPassword ? (
                                <FiEye
                                onClick={() => setShowPassword(false)}
                                className="absolute left-3.5 top-1/2 -translate-y-1/2 text-white text-lg cursor-pointer"
                                />
                            ) : (
                                <FiEyeOff
                                onClick={() => setShowPassword(true)}
                                className="absolute left-3.5 top-1/2 -translate-y-1/2 text-white text-lg cursor-pointer"
                                />
                            )}
                            <input type={showPassword ? "text" : "password"} placeholder="Enter Password" name="password" value={loginFormData.password} onChange={handleLoginChange} className='w-full py-1.5 pl-11 pr-4 rounded-md bg-transparent focus:outline-none placeholder:text-white border focus:border-cyan-400 text-sm'/>
                        </div>

                        <div className='flex items-center justify-between text-xs pt-1.7'>
                            <div className='flex gap-2 items-center'>
                                <input type='checkbox' id="rememberMe" name="rememberMe" checked={loginFormData.rememberMe} onChange={handleLoginChange} className="accent-cyan-400 h-4 w-4 bg-transparent border-gray-600 rounded focus:ring-0"/>
                                <label htmlFor='rememberMe' className="text-gray-400 cursor-pointer">Remember me</label>
                            </div>

                            <p className='text-cyan-400 hover:underline cursor-pointer'>Forget Password</p>

                        </div>
                        </>)
                 :  
                        (
                        <>
                        <div className='relative'>
                            <FaUser className="absolute left-3.5 top-1/2 -translate-y-1/2 text-white text-lg" />
                            <input type='text' placeholder="Enter UserName" name="name" value={signInFormData.name} onChange={handleSignUpChange} className='w-full py-1.5 pl-11 pr-4 rounded-md bg-transparent focus:outline-none placeholder:text-white border focus:border-cyan-400 text-sm'/>
                        </div>

                        <div className='relative'>
                            <FiMail className="absolute left-3.5 top-1/2 -translate-y-1/2 text-white text-lg" />
                            <input type='text' placeholder="Enter Email" name="email" value={signInFormData.email} onChange={handleSignUpChange} className='w-full py-1.5 pl-11 pr-4 rounded-md bg-transparent focus:outline-none placeholder:text-white border focus:border-cyan-400 text-sm'/>
                        </div>

                        <div  className='relative'>
                            {/* Toggle Icon */}
                            {showPassword ? (
                                <FiEye
                                onClick={() => setShowPassword(false)}
                                className="absolute left-3.5 top-1/2 -translate-y-1/2 text-white text-lg cursor-pointer"
                                />
                            ) : (
                                <FiEyeOff
                                onClick={() => setShowPassword(true)}
                                className="absolute left-3.5 top-1/2 -translate-y-1/2 text-white text-lg cursor-pointer"
                                />
                            )}
                            <input type={showPassword ? "text" : "password"} placeholder="Enter Password" name="password" value={signInFormData.password} onChange={handleSignUpChange} className='w-full py-1.5 pl-11 pr-4 rounded-md bg-transparent focus:outline-none placeholder:text-white border focus:border-cyan-400 text-sm'/>
                        </div>

                        <div className='flex items-center justify-between text-xs py-2'>
                            <div className='flex gap-2 items-center'>
                                <input type='checkbox' id="rememberMe" name="rememberMe" checked={signInFormData.rememberMe} onChange={handleSignUpChange} className="accent-cyan-400 h-4 w-4 bg-transparent border-gray-600 rounded focus:ring-0"/>
                                <label htmlFor='rememberMe' className="text-gray-400 cursor-pointer">Remember me</label>
                            </div>

                            <p className='text-cyan-400 hover:underline cursor-pointer'>Forget Password</p>

                        </div>
                        </>)
                 }
                    

                    <button className='bg-cyan-400 text-black font-semibold text-md w-full my-4 py-[0.3rem] rounded-md hover:bg-cyan-300'>{onAuth}</button>
                </form>

                <div className='flex items-center py-4'>
                    <div className='border w-1/2 h-0'></div>
                    <p className='px-4'>OR</p>
                    <div className='border w-1/2 h-0'></div>
                </div>

                <div className="flex flex-col sm:flex-row gap-x-3 pb-4">
                    <button className="flex-1 flex items-center justify-center gap-3 bg-white text-gray-900 py-3 rounded-lg text-sm font-medium hover:bg-gray-100 transition">
                        <FaGoogle className="text-lg text-red-500" />
                        {onAuth}
                    </button>
                    <button className="flex-1 flex items-center justify-center gap-3 bg-white text-gray-900 py-3 rounded-lg text-sm font-medium hover:bg-gray-100 transition">
                        <FaFacebook className="text-xl text-blue-600" />
                        {onAuth}
                    </button>
                </div>

                {onAuth==='Login'?
                    (<><p className='text-center text-sm'>
                        New to MoveGalaxy? 
                        <span 
                          onClick={() => setAuthType("Signup")}
                          className='text-cyan-400 hover:underline cursor-pointer hover:text-cyan-200'
                        >
                          Create an Account
                        </span>
                      </p></>)
                    :
                    (<><p 
                        onClick={() => setAuthType("Login")}
                        className='text-cyan-400 hover:underline cursor-pointer hover:text-cyan-200'
                      >
                        Already have an Account
                      </p></>)
                }
                
            </div>
        </div>
    )
}
{/* #031519 */}
export default AuthForm



