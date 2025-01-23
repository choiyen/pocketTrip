import React from "react";
import { useNavigate } from "react-router-dom";
import Button from "../../components/Common/Button";
import "./Where1.css";

export default function Where1() {
  const navigate = useNavigate();

  const goToIndex = () => {
    navigate("/");
  };

  const goToWhere2 = () => {
    navigate("/where2");
  };

  const goToWhere3 = () => {
    navigate("/where3");
  };

  return (
    <div className="where-container1">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="30"
        height="30"
        fill="currentColor"
        className="x"
        viewBox="0 0 16 16"
        onClick={goToIndex}
      >
        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708" />
      </svg>
      <div className="where-title1">어디로 여행 가시나요?</div>
      <div className="button-container">
        <Button size="S" name="국내" $bgColor="blue" onClick={goToWhere3} />
        <Button size="S" name="해외" $bgColor="blue" onClick={goToWhere2} />
      </div>
    </div>
  );
}
