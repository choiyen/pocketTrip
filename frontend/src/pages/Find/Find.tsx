import React, { useState } from "react";
import "./Find.css";
import { useNavigate } from "react-router-dom";

const Find: React.FC = () => {
  const [usernameId, setUsernameId] = useState<string>("");
  const [phoneNumberId, setPhoneNumberId] = useState<string>("");
  const [usernamePw, setUsernamePw] = useState<string>("");
  const [phoneNumberPw, setPhoneNumberPw] = useState<string>("");
  const [emailAddrPw, setEmailAddrPw] = useState<string>("");
  const [errorMessages, setErrorMessages] = useState<any>({});

  const navigate = useNavigate();

  const findEmail = () => {
    // 이메일 찾기 요청
    console.log("이메일 찾기:", usernameId, phoneNumberId);
    // 추가적인 이메일 찾기 로직을 구현하세요.
  };

  const requestPasswordReset = () => {
    // 비밀번호 재설정 요청
    console.log("비밀번호 재설정:", usernamePw, phoneNumberPw, emailAddrPw);
    // 추가적인 비밀번호 재설정 로직을 구현하세요.
  };

  const closeModal = () => {
    document.getElementById("resultModal")?.classList.add("hidden");
  };

  const closeUserFoundModal = () => {
    document.getElementById("userFoundModal")?.classList.add("hidden");
  };

  const closeResetModal = () => {
    document.getElementById("resetPasswordModal")?.classList.add("hidden");
  };

  const closeErrorModal = () => {
    document.getElementById("errorModal")?.classList.add("hidden");
  };

  return (
    <div>
      <div className="logoSz">
        <a href="/login">
          <img
            style={{ width: "100%", height: "100%" }}
            src="/Money log logo.png"
            alt="로고위치"
          />
        </a>
      </div>

      <div className="findD">
        <form className="findForm">
          <h2>사용자 정보</h2>
          <input
            type="text"
            className="usernameId"
            name="usernameId"
            required
            placeholder="이름을 입력해 주세요"
            value={usernameId}
            onChange={(e) => setUsernameId(e.target.value)}
          />
          <span id="usernameIdError" className="error-text">
            {errorMessages.usernameIdError}
          </span>

          <input
            type="number"
            className="phoneNumberId"
            name="phoneNumberId"
            required
            placeholder="전화번호를 입력해 주세요"
            value={phoneNumberId}
            onChange={(e) => setPhoneNumberId(e.target.value)}
          />
          <span id="phoneNumberIdError" className="error-text">
            {errorMessages.phoneNumberIdError}
          </span>
        </form>
        <button className="findBt" type="button" onClick={findEmail}>
          이메일 찾기
        </button>

        <form className="findForm">
          <h2>비밀번호 재설정</h2>
          <input
            type="text"
            className="usernamePw"
            name="usernamePw"
            required
            placeholder="이름을 입력해 주세요"
            value={usernamePw}
            onChange={(e) => setUsernamePw(e.target.value)}
          />
          <span id="usernamePwError" className="error-text">
            {errorMessages.usernamePwError}
          </span>

          <input
            type="number"
            className="phoneNumberPw"
            name="phoneNumberPw"
            required
            placeholder="전화번호를 입력해 주세요"
            value={phoneNumberPw}
            onChange={(e) => setPhoneNumberPw(e.target.value)}
          />
          <span id="phoneNumberPwError" className="error-text">
            {errorMessages.phoneNumberPwError}
          </span>

          <input
            type="text"
            className="emailAddrPw"
            name="emailAddrPw"
            required
            placeholder="이메일을 입력해 주세요"
            value={emailAddrPw}
            onChange={(e) => setEmailAddrPw(e.target.value)}
          />
          <span id="emailAddrPwError" className="error-text">
            {errorMessages.emailAddrPwError}
          </span>
        </form>
        <button className="findBt" type="button" onClick={requestPasswordReset}>
          비밀번호 재설정
        </button>
      </div>

      <div className="isUser">
        <p style={{ fontSize: "11px" }}>이미 계정이 있으신가요?</p>
        <a className="lostEp" href="/login">
          로그인
        </a>
      </div>

      {/* 이메일 확인 모달 창 */}
      <div id="resultModal" className="hidden">
        <div id="modalContent">
          <div className="modalMessageBox">
            <p id="modalMessage">이메일을 찾을 수 없습니다.</p>
          </div>
          <button className="modalBt" onClick={closeModal}>
            확인
          </button>
        </div>
      </div>

      {/* 사용자 확인 모달 창 */}
      <div id="userFoundModal" className="hidden">
        <div id="userFoundModalContent">
          <div className="userFoundModalMessageBox">
            <p id="userFoundModalMessage">
              비밀번호 재설정 요청이 완료되었습니다.
            </p>
          </div>
          <button className="modalBt" onClick={closeUserFoundModal}>
            확인
          </button>
        </div>
      </div>

      {/* 비밀번호 재설정 모달 창 */}
      <div id="resetPasswordModal" className="hidden">
        <div id="resetPasswordContent">
          <h3>새 비밀번호 입력</h3>
          <input
            type="password"
            id="newPassword"
            placeholder="새 비밀번호"
            required
          />
          <input
            type="password"
            id="confirmPassword"
            placeholder="비밀번호 확인"
            required
          />
          <div className="resetPasswordContentBtn">
            <button
              className="modalBt"
              type="button"
              onClick={() => {
                /* 비밀번호 변경 처리 */
              }}
            >
              비밀번호 변경
            </button>
            <button className="modalOffBt" onClick={closeResetModal}>
              닫기
            </button>
          </div>
        </div>
      </div>

      {/* 오류 메시지 모달 창 */}
      <div id="errorModal" className="hidden">
        <div id="errorContent">
          <p id="errorMessage">입력 오류입니다.</p>
          <button className="modalBt" onClick={closeErrorModal}>
            닫기
          </button>
        </div>
      </div>
    </div>
  );
};

export default Find;
