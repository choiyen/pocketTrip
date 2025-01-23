import React from "react";
import "./Where.css"; // 스타일 파일 추가

export default function Where() {
  return (
    <div className="where-container">
      <div className="where-title">어느 나라로 \n 떠날 계획인가요?</div>
      <div className="where-option">
        일본{" "}
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path
            fillRule="evenodd"
            d="M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708"
          />
        </svg>
      </div>
    </div>
  );
}
