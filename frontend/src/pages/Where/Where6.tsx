import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setTravelData } from "../../slices/travelSlice";
import Button from "../../components/Common/Button";
import "./Where6.css";
import { useMemo, useEffect } from "react";

export default function Where6() {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();

  const travelData = location.state || {}; // 전달받은 데이터

  React.useEffect(() => {
    if (Object.keys(travelData).length > 0) {
      dispatch(setTravelData(travelData));
    }
  }, [dispatch, travelData]);

  const goToIndex = () => {
    console.log("최종 travelData:", travelData); // 최종 데이터 확인
    navigate("/");
  };

  const memoizedTravelData = useMemo(() => travelData, [travelData]);

  useEffect(() => {
    console.log("여행 데이터 변경 감지:", memoizedTravelData);
  }, [memoizedTravelData]);

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

      {/* 받은 travelData 확인용 (디버깅) */}
      {/* <pre>{JSON.stringify(travelData, null, 2)}</pre> */}
    </div>
  );
}
