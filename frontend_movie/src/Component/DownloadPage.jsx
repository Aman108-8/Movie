import React, { useEffect, useRef, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom';
import * as api from '../Config/authApi'

const DownloadPage = () => {
    const [progress, setProgress] = useState(0);
    const [downloaded, setDownloaded] = useState("0 MB");
    const [totalSize, setTotalSize] = useState("0 MB");
    const [speed, setSpeed] = useState("0 MB/s");
    const [eta, setEta] = useState("Calculating...");
    const [status, setStatus] = useState("Starting...");
    const [isPaused, setIsPaused] = useState(false);

    // 1. Move timer to a ref so handleCancel can access it
    const timerRef = useRef(null);
    const isCancelledRef = useRef(false);
    const location = useLocation();
    const isTerminal = ["Completed", "Cancelled", "Failed"].includes(status);
    //const isManuallyResumingRef = useRef(false);

    const 
    {
        title,
        quality,
        url
    } = location.state || {};

    useEffect(() => 
    {

        //let timer;
      
        const startDownload = async () => 
        {
            isCancelledRef.current = false; //resets the cancel flag to its starting state, important because if you'd previously cancelled a download and now you're starting a new one (different quality, or a fresh attempt), the old true flag from last time must not leak into this fresh attempt.

            setProgress(0);
            setDownloaded("0 MB");
            setTotalSize("0 MB");
            setSpeed("0 MB/s");
            setEta("Calculating...");
            setStatus("Starting...");
            
      
            try 

            {
      
                setStatus("Downloading...");
        
                // timerRef.current resets the cancel flag to its starting state, important because if you'd previously cancelled a download and now you're starting a new one (different quality, or a fresh attempt), the old true flag from last time must not leak into this fresh attempt.
                timerRef.current = setInterval(async () => {

                    const response = await api.getDownloadProgress();   //get ptogress from backend
                
                    setProgress(response.data.progress);
                    setSpeed(response.data.speed);
                    setEta(response.data.eta);
                    setTotalSize(response.data.totalSize);
                    setDownloaded(response.data.downloadedSize);

                    if (response.data.progress >= 100) 
                    {
                        clearInterval(timerRef.current);
                        setStatus("Completed");
                    } 
                    else if (response.data.eta === "Reconnecting...") {
                        setStatus("Reconnecting...");
                        // do NOT setIsPaused(true) — there's nothing to resume, it's already running
                    }
                    else if (response.data.eta === "Paused") 
                    {
                        //if (isManuallyResumingRef.current) return;  // ← ignore stale "Paused" right after resume click
                        setIsPaused(true);
                        setStatus("Paused");          // ← keep polling, don't clear interval
                    }
                    else if (response.data.eta === "Failed") 
                    {
                        clearInterval(timerRef.current);
                        setStatus("Failed");
                    } 
                    else if (response.data.eta === "Cancelled") 
                    {
                        clearInterval(timerRef.current);
                        setStatus("Cancelled");
                    }
                    else {
                        //isManuallyResumingRef.current = false;  
                        setIsPaused(false);
                        setStatus("Downloading...");  // ← real progress came back, clear paused UI
                    }
                
                }, 100);
        
                console.log("Sending quality:", quality);   // ← add right before this line
                await api.downloadMovie(url, quality);

                //clearInterval(timer);
                
                setStatus("Downloading...");

            } 
            catch (err) 
            {
                if (isCancelledRef.current) {return}; 
                console.error(err);
                setStatus("Failed");
        
            }
      
        };
      
        // ─── Tab close / page refresh ───────────────────────────────────────────
        const handleBeforeUnload = () => 
        {
            // sendBeacon works even as the page is dying; fetch/axios do not
            //sendBeacon() is used to send a request to the backend when:Browser tab close, refresh, crash, etc
            navigator.sendBeacon("http://localhost:9090/api/download/cancel");
        };
        window.addEventListener("beforeunload", handleBeforeUnload);

        if (url) startDownload();

        // ─── Navigate away / component unmount ──────────────────────────────────
        return () => 
        {
            isCancelledRef.current = true;           // silence the catch block
            if (timerRef.current) {clearInterval(timerRef.current);}
            window.removeEventListener("beforeunload", handleBeforeUnload);
            api.cancelDownload();                    // no await — fire and forget
        };
      
    }, [url, quality]);

    const handleCancel = async()=>
    {
        console.log("cancel")
        try {
            isCancelledRef.current = true;       // ← set BEFORE the API call
            if (timerRef.current) clearInterval(timerRef.current);
            setStatus("Cancelled");
            await api.cancelDownload();
            if (timerRef.current) clearInterval(timerRef.current);
            
            setEta("Cancelled");
        } 
        catch (error) {
            console.error(error);
        }
    }

    const handlePauseResume = async () =>
    {
        try {
            if (isPaused) {
                //isManuallyResumingRef.current = true;
                console.log("resume")
                setStatus("Downloading...");
                setEta("Calculating...");
                await api.resumeDownload();
                setIsPaused(false);
            } 
            else {
                console.log("pause ")
                setStatus("Paused");
                await api.pauseDownload();
                setIsPaused(true);
                
            }
        } catch (error) {
            console.error(error);
        }
    }

  return (
    <div className='pt-7 px-6'>
        <div className='flex flex-col gap-10'>
            <div className=''>
                <h2 className='text-white font-bold text-4xl tracking-wide drop-shadow-[0_0_15px_rgba(48,235,255,0.4)]'> 
                  {title} 
                </h2>
                
            </div>

            <div className='space-y-3'>
                <div className='flex justify-between'>
                    <p className='text-white text-2xl pl-2' style={{
                            textShadow: `
                            0 0 4px #fff,
                            0 0 10px rgba(34, 211, 238, 0.8),
                            0 0 20px rgba(34, 211, 238, 0.6),
                            0 0 40px rgba(6, 182, 212, 0.4)
                            `
                        }}>
                            {quality}
                    </p>
                    <p className='text-white text-xl pl-2' style={{
                            textShadow: `
                            0 0 4px #fff,
                            0 0 10px rgba(34, 211, 238, 0.8),
                            0 0 20px rgba(34, 211, 238, 0.6),
                            0 0 40px rgba(6, 182, 212, 0.4)
                            `
                        }}>{status}</p>
                    
                </div>
                    
                {/* --- PROGRESS CONTAINER TRACK --- */}
                <div className="relative w-full h-12 bg-black/70 rounded-xl border border-cyan-200 p-[3px] shadow-[inset_0_2px_10px_rgba(0,0,0,0.8)]">
                    
                    {/* 1. UNDERLAY GLOW EFFECT */}
                    {/* This handles the ambient lighting that bleeds past the borders */}
                    <div 
                    className="absolute top-0 left-0 h-full rounded-xl bg-cyan-500/40 blur-md transition-all duration-500"
                    style={{ width: `${progress}%` }}
                    />

                    {/* 2. THE PROGRESS FILL BASE */}
                    <div 
                    className="relative h-full rounded-lg overflow-hidden transition-all duration-500 ease-out flex items-center"
                    style={{ 
                        width: `${progress}%`,
                        boxShadow: '0 0 20px rgba(48,235,255,0.7), inset 0 1px 3px rgba(255,255,255,0.5)'
                    }}
                    >
                    {/* 3. THE RUNNING SHINING PLASMA CORE (The direct fix for your issue) */}
                    {/* This moving linear gradient creates the bright electric wave right down the center */}
                    <div 
                        className="absolute inset-0 animate-plasma"
                        style={{
                        backgroundImage: 'linear-gradient(-45deg, #0891b2 0%, #22d3ee 25%, #ffffff 50%, #22d3ee 75%, #0891b2 100%)'
                        }}
                    />

                    {/* 4. INTERNAL LAYER MASKS FOR EXTRA DEEP DIMENSION */}
                    {/* Horizontal glow line running strictly through the middle */}
                    <div className="absolute top-[35%] left-0 w-full h-[30%] bg-white/40 blur-[1px] rounded-full mix-blend-overlay" />
                    
                    {/* Top-to-bottom shading gradient to make the bar appear 3D cylinder shaped */}
                    <div className="absolute inset-0 bg-gradient-to-b from-white/25 via-transparent to-black/40" />
                    </div>

                    {/* 5. METRIC FLOATING DISPLAY */}
                    <div className="absolute right-4 top-1/2 -translate-y-1/2 text-xl text-white font-mono font-bold text-sm select-none drop-shadow-[0_2px_4px_rgba(0,0,0,0.8)]">
                        {progress}%
                    </div>
                </div>

                <div className='text-white text-lg flex justify-between px-1' style={{
                        textShadow: `
                        0 0 4px #fff,
                        0 0 10px rgba(34, 211, 238, 0.8),
                        0 0 20px rgba(34, 211, 238, 0.6),
                        0 0 40px rgba(6, 182, 212, 0.4)
                        `
                    }}>
                    <div className='space-x-2'>
                        <span>
                            {downloaded} / {totalSize}
                        </span>
                    </div>
                    <span>{speed}</span>
                </div>
            </div>

            <div className=''>
                <div className="text-white bg-glass-fill border border-glass-border rounded-lg text-sm text-gray-400 px-4 pt-2.5 pb-5 border-cyan-400">
                    <p className='text-white text-2xl pl-1' style={{
                        textShadow: `
                        0 0 4px #fff,
                        0 0 10px rgba(34, 211, 238, 0.8),
                        0 0 20px rgba(34, 211, 238, 0.6),
                        0 0 40px rgba(6, 182, 212, 0.4)
                        `
                    }}>ESTIMATED TIME REMAINING</p>
                    <p className="text-white font-mono font-bold text-5xl text-center tracking-wider"
                    style={{
                        textShadow: `
                        0 0 4px #fff,
                        0 0 10px rgba(34, 211, 238, 0.8),
                        0 0 20px rgba(34, 211, 238, 0.6),
                        0 0 40px rgba(6, 182, 212, 0.4)
                        `
                    }}
                    >
                    {eta}
                    </p>
                </div>
            </div>

            <div className='w-full flex gap-5'>
                {!isTerminal && (
                    <button
                        className="w-full bg-white/10 hover:bg-white/20 hover:shadow-[0_0_25px_rgba(34,211,238,0.8),0_0_50px_rgba(34,211,238,0.5)] text-white font-medium px-5 py-2.5 border border-[#30EBFF] border-[2px] rounded-xl backdrop-blur-md shadow-[0_0_15px_rgba(48,235,255,0.2)] transition-all duration-300 text-sm"
                        onClick={handleCancel}
                    >
                        Cancel
                    </button>
                )}
                {!isTerminal && (
                    <button
                        className="w-full bg-white/10 hover:bg-white/20 hover:shadow-[0_0_25px_rgba(34,211,238,0.8),0_0_50px_rgba(34,211,238,0.5)] text-white font-medium px-7 py-2.5 border border-[#30EBFF] border-[2px] rounded-xl backdrop-blur-md shadow-[0_0_15px_rgba(48,235,255,0.2)] transition-all duration-300 text-sm"
                        onClick={handlePauseResume}
                    >
                        {isPaused ? "Resume" : "Pause"}
                    </button>
                )}
            </div>
        </div>
    </div>
    
  )
}

export default DownloadPage