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

  const [modalState, setModalState] = useState({
    result: false,
    userFound: false,
    resetPassword: false,
    error: false,
  });

  const navigate = useNavigate();

  const findEmail = () => {
    console.log("이메일 찾기:", usernameId, phoneNumberId);
    // 이메일 찾기 로직
    setModalState({ ...modalState, result: true });
  };

  const requestPasswordReset = () => {
    console.log("비밀번호 재설정:", usernamePw, phoneNumberPw, emailAddrPw);
    // 비밀번호 재설정 로직
    setModalState({ ...modalState, userFound: true });
  };

  return (
    <div>
      <div className="logoSz">
        <a href="/login">
          <img
            style={{ width: "100%", height: "100%" }}
            src="/PocktetTripLogo.png"
            alt="로고위치"
          />
        </a>
      </div>

      <div className="findD">
        <form className="findForm">
          <h2 style={{ marginBottom: "20px" }}>사용자 정보</h2>
          <input
            type="text"
            placeholder="이름을 입력해 주세요"
            value={usernameId}
            onChange={(e) => setUsernameId(e.target.value)}
          />
          <span className="error-text">{errorMessages.usernameIdError}</span>

          <input
            type="number"
            placeholder="전화번호를 입력해 주세요"
            value={phoneNumberId}
            onChange={(e) => setPhoneNumberId(e.target.value)}
          />
          <span className="error-text">{errorMessages.phoneNumberIdError}</span>
        </form>
        <button className="findBt" type="button" onClick={findEmail}>
          이메일 찾기
        </button>

        <form className="findForm">
          <h2 style={{ marginBottom: "20px" }}>비밀번호 재설정</h2>
          <input
            type="text"
            placeholder="이름을 입력해 주세요"
            value={usernamePw}
            onChange={(e) => setUsernamePw(e.target.value)}
          />
          <span className="error-text">{errorMessages.usernamePwError}</span>

          <input
            type="number"
            placeholder="전화번호를 입력해 주세요"
            value={phoneNumberPw}
            onChange={(e) => setPhoneNumberPw(e.target.value)}
          />
          <span className="error-text">{errorMessages.phoneNumberPwError}</span>

          <input
            type="text"
            placeholder="이메일을 입력해 주세요"
            value={emailAddrPw}
            onChange={(e) => setEmailAddrPw(e.target.value)}
          />
          <span className="error-text">{errorMessages.emailAddrPwError}</span>
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

      {/* 이메일 확인 모달 */}
      {modalState.result && (
        <div className="modal">
          <div className="modalContent">
            <p>이메일을 찾을 수 없습니다.</p>
            <button
              onClick={() => setModalState({ ...modalState, result: false })}
            >
              확인
            </button>
          </div>
        </div>
      )}

      {/* 사용자 확인 모달 */}
      {modalState.userFound && (
        <div className="modal">
          <div className="modalContent">
            <p>비밀번호 재설정 요청이 완료되었습니다.</p>
            <button
              onClick={() => setModalState({ ...modalState, userFound: false })}
            >
              확인
            </button>
          </div>
        </div>
      )}

      {/* 비밀번호 재설정 모달 */}
      {modalState.resetPassword && (
        <div className="modal">
          <div className="modalContent">
            <h3>새 비밀번호 입력</h3>
            <input type="password" placeholder="새 비밀번호" />
            <input type="password" placeholder="비밀번호 확인" />
            <button>비밀번호 변경</button>
            <button
              onClick={() =>
                setModalState({ ...modalState, resetPassword: false })
              }
            >
              닫기
            </button>
          </div>
        </div>
      )}

      {/* 오류 모달 */}
      {modalState.error && (
        <div className="modal">
          <div className="modalContent">
            <p>입력 오류입니다.</p>
            <button
              onClick={() => setModalState({ ...modalState, error: false })}
            >
              닫기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Find;
