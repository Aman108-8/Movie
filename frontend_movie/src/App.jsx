import { BrowserRouter, Routes, Route } from "react-router-dom";
import AuthForm from "./Component/AuthForm";
import MainLayout from "./pages/MainLayout";
import MainLayoutAdmin from "./admin/pages/MainLayoutAdmin";
import Movie from "./Component/Movie";
import { useState, useRef, useEffect } from "react";
import User from "./admin/Component/User";
import MovieAdmin from "./admin/Component/Movie";
import Pricing from "./Component/Pricing";
import Edit from "./admin/Component/Edit";
import MovieDetails from "./Component/MovieDetails";
import DownloadPage from "./Component/DownloadPage";
import { ToastContainer} from 'react-toastify';

function App() {
  const [showAuth, setShowAuth] = useState(false);
  const [authType, setAuthType] = useState("Login");
  const modalRef = useRef();

  // ✅ Close when clicking outside
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (modalRef.current && !modalRef.current.contains(e.target)) {
        setShowAuth(false);
      }
    };

    if (showAuth) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [showAuth]);

  return (
    <div>
      <ToastContainer />
    <BrowserRouter>
  <Routes>

    {/* USER SIDE */}
    <Route
      path="/"
      element={
        <MainLayout
          onAuthOpen={(type) => {
            setShowAuth(true);
            setAuthType(type);
          }}
        />
      }
    >
      <Route index element={<Movie />} />
      <Route path="pricing" element={<Pricing />} />
      <Route path="MovieDetails/:movieId?" element={<MovieDetails />} />
      <Route path="/download/:movieId" element={<DownloadPage />} />
    </Route>

    {/* ADMIN SIDE */}
    <Route path="/admin" element={<MainLayoutAdmin />}>
      <Route index element={<MovieAdmin />} />
      <Route path="movie" element={<MovieAdmin />} />
      <Route path="user" element={<User />} />
      <Route path="edit/:movieId?" element={<Edit />} />
      <Route path="edit" element={<Edit />} />
    </Route>

  </Routes>

  {/* MODAL */}
  {showAuth && (
    <div className="fixed inset-0 z-40 flex items-center justify-center backdrop-blur-[3px] bg-black/20">
      <div ref={modalRef}>
        <AuthForm onAuth={authType} setAuthType={setAuthType} setShowAuth={setShowAuth} />
      </div>
    </div>
  )}
</BrowserRouter>
</div>
  );
}

export default App;