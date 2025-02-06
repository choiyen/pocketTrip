import React, { useState } from "react";
import axios from "axios";
import "./LoginPage.css";
import Button from "../../components/Common/Button";

interface LoginResponse {
  success: boolean;
  message: string;
}

const LoginPage: React.FC = () => {
  const [email, setEmailAddr] = useState<string>(""); // emailAddr의 타입을 string으로 지정
  const [password, setPassword] = useState<string>(""); // password의 타입을 string으로 지정

  // 로그인 함수
  const loginUser = (): void => {
    axios
      .post<LoginResponse>("http://localhost:8080/auth/signin", { email: email, password: password },
        {headers: {
          "Content-Type": "application/json"
      }}) // 응답의 타입을 LoginResponse로 지정
      .then((response) => {
        if (response.data.success) {
          window.location.href = "/dashboard";
        }
      });
  };

  return (
    <div className="login-page" style={{ backgroundColor: "#ffffff" }}>
      <div className="logoSz">
        <a href="/login">
          <img
            style={{ width: "100%", height: "100%" }}
            src="/airplane.png"
            alt="로고위치"
          />
        </a>
      </div>

      <form className="loginForm">
        <label className="formLabel">아이디</label>
        <input
          type="text"
          className="emailAddr"
          name="emailAddr"
          required
          placeholder="이메일을 입력해 주세요"
          value={email}
          onChange={(e) => setEmailAddr(e.target.value)}
        />
        <label className="formLabel">비밀번호</label>
        <input
          type="password"
          className="password"
          name="password"
          required
          placeholder="비밀번호를 입력해 주세요"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <div className="lostE">
          <p style={{ fontSize: "9px" }}>
            이메일 또는 비밀번호를 잊어버리셨나요?
          </p>
          <a className="lostEp1" href="/login/find">
            이메일 & 비밀번호 찾기
          </a>
        </div>
        <Button size="L" name="로그인" $bgColor="blue" onClick={loginUser} />
      </form>

      {/* 회원가입 페이지 만들면 경로 바꾸기! */}
      <a className="lostEp2" href="/login/register">
        회원가입
      </a>
      {/* 경로 바꾸기! */}
      <a className="noLogin" href="/">
        로그인없이 구경하기
      </a>
    </div>
  );
};

export default LoginPage;
