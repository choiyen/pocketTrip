import React, { useEffect, useState } from "react";
import "./Register.css";
import Button from "../../components/Common/Button";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface RegisterResponse {
  status: string;
  message: string;
  data?: string[];
}

const Register: React.FC = () => {
  const [formData, setFormData] = useState({
    username: "",
    emailAddr: "",
    password: "",
    passwordConfirm: "",
    phoneNumber: "",
    profile: "blob:http://localhost:3000/ProfileImage.png",
  });
  const [errors, setErrors] = useState({
    emailAddrError: "",
    passwordConfirmError: "",
    phoneNumberError: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    // 입력값이 변경되면 해당 에러 메시지를 초기화
    if (name === "emailAddr") {
      setErrors((prevErrors) => ({
        ...prevErrors,
        emailAddrError: "", // 이메일 입력시 에러 메시지 초기화
      }));
    }

    if (name === "passwordConfirm") {
      setErrors((prevErrors) => ({
        ...prevErrors,
        passwordConfirmError: "", // 비밀번호 확인 입력시 에러 메시지 초기화
      }));
    }

    if (name === "phoneNumber") {
      setErrors((prevErrors) => ({
        ...prevErrors,
        phoneNumberError: "", // 전화번호 입력시 에러 메시지 초기화
      }));
    }

    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // Validation and API submission logic here
    console.log(formData);
    axios
      .post(
        "http://localhost:8080/auth/signup",
        {
          name: formData.username,
          email: formData.emailAddr,
          password: formData.password,
          phone: formData.phoneNumber,
          profile: formData.profile,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      )
      .then((response) => {
        if (response.status === 200) {
          // 성공 처리
          console.log(response);
          if (response.data.resultType === "success") {
            navigate("/Login");
          } else {
            console.log(response.data.message);
          }
        }
      })
      .catch((error) => {
        // 400 에러 발생 시 처리
        if (error.response) {
          if (error.response.status === 400) {
            console.error("400 Error:", error.response.data.message);
            if (error.response.data.message.includes("already exists")) {
              setErrors((prevErrors) => ({
                ...prevErrors,
                emailAddrError: "이미 가입된 아이디입니다.",
              }));
            }
          } else {
            console.error("Unexpected Error:", error.response);
          }
        } else {
          console.error("Network Error:", error.message);
        }
      });
  };

  const navigate = useNavigate();

  return (
    <div className="Register-page" style={{ backgroundColor: "#ffffff" }}>
      <div className="logoSz">
        <a href="/login">
          <img
            style={{ width: "100%", height: "100%" }}
            src="/Money log logo.png"
            alt="로고위치"
          />
        </a>
      </div>

      <form
        action="http://localhost:8080/auth/signup"
        method="POST"
        id="registerForm"
        onSubmit={handleSubmit}
      >
        <div className="inD">
          <label className="formLabel">이름</label>
          <input
            type="text"
            className="username"
            name="username"
            required
            placeholder="이름을 입력해 주세요"
            value={formData.username}
            onChange={handleChange}
          />
        </div>

        <div className="inD">
          <label className="formLabel">아이디</label>
          <input
            type="text"
            className="emailAddr"
            name="emailAddr"
            required
            placeholder="이메일을 입력해 주세요"
            value={formData.emailAddr}
            onChange={handleChange}
          />
          {errors.emailAddrError && (
            <span
              id="emailAddrError"
              className={`error-text ${errors.emailAddrError ? "show" : ""}`}
            >
              {errors.emailAddrError}
            </span>
          )}
        </div>

        <div className="inD">
          <label className="formLabel">비밀번호</label>
          <input
            type="password"
            className="password"
            name="password"
            required
            placeholder="비밀번호를 입력해 주세요"
            value={formData.password}
            onChange={handleChange}
          />
        </div>

        <div className="inD">
          <label className="formLabel">비밀번호확인</label>
          <input
            type="password"
            className="password_confirm"
            name="passwordConfirm"
            required
            placeholder="비밀번호를 한번 더 입력해 주세요"
            value={formData.passwordConfirm}
            onChange={handleChange}
          />
          {errors.passwordConfirmError && (
            <span id="password_confirmError" className="error-text">
              {errors.passwordConfirmError}
            </span>
          )}
        </div>

        <div className="inD">
          <label className="formLabel">전화번호</label>
          <input
            type="number"
            className="phoneNumber"
            name="phoneNumber"
            required
            placeholder="전화번호를 입력해 주세요"
            value={formData.phoneNumber}
            onChange={handleChange}
          />
          {errors.phoneNumberError && (
            <span id="phoneNumberError" className="error-text">
              {errors.phoneNumberError}
            </span>
          )}
        </div>

        <div className="inD">
          <Button size="L" name="회원가입" $bgColor="blue" type="submit" />
        </div>
      </form>

      <div id="successModal">
        <div className="modal">
          <h2>회원가입 완료!</h2>
          <p>회원가입이 성공적으로 완료되었습니다.</p>
          <div className="modalBtBox">
            <button className="modalOffBt">
              <a href="/login">완료</a>
            </button>
          </div>
        </div>
      </div>

      <div className="isUser">
        <p style={{ fontSize: "11px" }}>이미 계정이 있으신가요?</p>
        <a className="lostEp" href="/login">
          로그인
        </a>
      </div>
    </div>
  );
};

export default Register;
