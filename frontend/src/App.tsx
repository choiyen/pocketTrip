import React from "react";
import "./styles/reset.css";
import "./styles/global.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "./pages/Home/MainPage";
import MyPage from "./pages/Mypage/MyPage";
import Login from "./pages/Login/Login";
import Register from "./pages/Login/Register/Register";
import { Provider } from "react-redux";
import store from "./store";

function App() {
  return (
    <div className="App">
      <Provider store={store}>
        <BrowserRouter>
          <Routes>
            <Route path="/Login" element={<Login />} />
            <Route path="/Login/Register" element={<Register />} />
            <Route path="/" element={<MainPage />} />
            <Route path="/mypage" element={<MyPage />} />
          </Routes>
        </BrowserRouter>
      </Provider>
    </div>
  );
}

export default App;
