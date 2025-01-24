import React from "react";
import "./styles/reset.css";
import "./styles/global.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "./pages/Home/MainPage";
import MyPage from "./pages/Mypage/MyPage";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import { useSelector } from "react-redux";
import { RootState } from "./store";
import AlertBox from "./components/Common/AlertBox";
import Where1 from "./pages/Where/Where1";
import Where2 from "./pages/Where/Where2";
import Where3 from "./pages/Where/Where3";
import Where4 from "./pages/Where/Where4";

function App() {
  const alertState = useSelector(
    (state: RootState) => state.AlertControl.alertState
  );
  return (
    <div className="App">
        <BrowserRouter>
          <Routes>
            <Route path="/Login" element={<Login />} />
            <Route path="/Login/Register" element={<Register />} />
            <Route path="/" element={<MainPage />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/Where1" element={<Where1 />} />
            <Route path="/Where2" element={<Where2 />} />
            <Route path="/Where3" element={<Where3 />} />
            <Route path="/Where4" element={<Where4 />} />
          </Routes>
        </BrowserRouter>
        {alertState && <AlertBox />}
    </div>
  );
}

export default App;
