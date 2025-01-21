import React, { useState } from "react";
import axios from "axios";
import "./LoginPage.css";

interface LoginResponse {
  success: boolean;
  message: string;
}

const LoginPage: React.FC = () => {
  const [emailAddr, setEmailAddr] = useState<string>(""); // emailAddr의 타입을 string으로 지정
  const [password, setPassword] = useState<string>(""); // password의 타입을 string으로 지정
  const [errorMessage, setErrorMessage] = useState<string>(""); // errorMessage의 타입을 string으로 지정
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false); // isModalVisible의 타입을 boolean으로 지정

  // 로그인 함수다.아
  const loginUser = (): void => {
    axios
      .post<LoginResponse>("/login", { emailAddr, password }) // 응답의 타입을 LoginResponse로 지정
      .then((response) => {
        if (response.data.success) {
          window.location.href = "/dashboard"; // 성공 후 대시보드 페이지로 이동
        } else {
          showLoginError(response.data.message);
        }
      })
      .catch((error) => {
        showLoginError("서버 오류가 발생했습니다.");
      });
  };

  // 로그인 오류 메시지 모달 함수
  const showLoginError = (message: string): void => {
    setErrorMessage(message);
    setIsModalVisible(true);
  };

  // 모달 닫기 함수
  const closeModal = (): void => {
    setIsModalVisible(false);
  };

  return (
    <div className="login-page">
      <div className="logoSz">
        <a href="/">
          <img
            style={{ width: "100%", height: "100%" }}
            src="../static/img/titleLogo.png"
            alt="Logo"
          />
        </a>
      </div>

      <form className="loginForm">
        <input
          type="text"
          className="emailAddr"
          name="emailAddr"
          required
          placeholder="이메일을 입력해 주세요"
          value={emailAddr}
          onChange={(e) => setEmailAddr(e.target.value)}
        />
        <input
          type="password"
          className="password"
          name="password"
          required
          placeholder="비밀번호를 입력해 주세요"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button type="button" className="loginBt" onClick={loginUser}>
          로그인
        </button>

        <div className="lostE">
          <p style={{ fontSize: "11px" }}>
            email 또는 비밀번호를 잊어버리셨나요?
          </p>
          <a className="lostEp" href="/login/find">
            email & 비밀번호 찾기
          </a>
        </div>
      </form>

      <div className="newUser">
        <p style={{ fontSize: "11px" }}>계정이 없으신가요?</p>
        <a className="lostEp" href="/login/register">
          회원가입
        </a>
      </div>

      {/* 로그인 실패 모달 */}
      {isModalVisible && (
        <div id="loginErrorModal" className="modal">
          <div className="modal-content">
            <p className="modal-message">{errorMessage}</p>
            <button className="close-btn" onClick={closeModal}>
              닫기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default LoginPage;
