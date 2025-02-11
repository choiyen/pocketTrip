import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setTravelData } from "../../slices/travelSlice";
import Button from "../../components/Common/Button";
import "./Where6.css";
import { useMemo, useEffect } from "react";
import axios from "axios";

export default function Where6() {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();

  const travelData = location.state || {}; // 전달받은 데이터

  // useEffect(() => {
  //   if (Object.keys(travelData).length > 0) {
  //     dispatch(setTravelData(travelData));
  //   }
  // }, [dispatch, travelData]);

  const memoizedTravelData = useMemo(() => travelData, [travelData]);

  const goToIndex = async () => {
    navigate("/");
  };

  return (
    <div className="where-container6">
      <div className="where-title6">
        초대코드로
        <br />
        친구를 초대하세요!
      </div>
      <div className="code">{travelData.travelCode}</div>
      <div className="button-container6">
        <Button size="M" name="확인" $bgColor="blue" onClick={goToIndex} />
      </div>
      <div className="code2">초대 코드는 언제든 다시 확인할 수 있습니다.</div>

      {/* 받은 travelData 확인용 (디버깅) */}
      {/* <pre>{JSON.stringify(travelData, null, 2)}</pre> */}
    </div>
  );
}
