import React from "react";
import { useNavigate } from "react-router-dom";
import Button from "../../components/Common/Button";
import "./Where6.css";

export default function Where6() {
  const navigate = useNavigate();

  const goToIndex = () => {
    navigate("/");
  };

  return (
    <div className="where-container6">
      <div className="where-title6">
        초대코드로
        <br />
        친구를 초대하세요!
      </div>
      <div className="code">SP1W34</div>
      <div className="button-container6">
        <Button size="M" name="확인" $bgColor="blue" onClick={goToIndex} />
      </div>
      <div className="code2">초대 코드는 언제든 다시 확인할 수 있습니다.</div>
    </div>
  );
}
