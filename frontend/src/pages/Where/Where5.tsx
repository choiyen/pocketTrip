import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Button from "../../components/Common/Button";
import "./Where5.css";

interface Where5Props {
  travelData: {
    isDomestic: boolean;
    selectedCountry: string;
    startDate: string | null;
    endDate: string | null;
    name: string;
    budget: number;
  };
  updateTravelData: (data: any) => void;
}

const Where5: React.FC<Where5Props> = ({ travelData, updateTravelData }) => {
  const [budget, setBudget] = useState<number>(travelData.budget); // 예산 상태
  const navigate = useNavigate();

  const goToWhere4 = () => {
    navigate("/where4");
  };

  const goToWhere6 = () => {
    // 예산(budget)을 travelData에 추가하여 업데이트
    updateTravelData({
      budget: budget, // 예산 값 업데이트
    });

    // 업데이트된 travelData 로그 출력
    console.log("업데이트된 travelData:", {
      ...travelData,
      budget: budget, // 예산 업데이트
    });

    // Where6 페이지로 이동
    navigate("/Where6");
  };

  const isButtonDisabled = budget <= 0; // 예산이 0 이하이면 버튼 비활성화

  return (
    <div className="where-container5">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="16"
        height="16"
        fill="currentColor"
        className="left"
        viewBox="0 0 16 16"
        onClick={goToWhere4}
      >
        <path
          fillRule="evenodd"
          d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0"
        />
      </svg>
      <div className="where-title5">
        여행 예산을 <br />
        설정하시겠어요?
      </div>
      <input
        type="text"
        className="input"
        value={budget.toLocaleString()} // 숫자 포맷으로 표시
        placeholder="숫자만 입력해주세요."
        onChange={(e) => {
          const rawValue = e.target.value.replace(/[^0-9]/g, ""); // 숫자만 추출
          setBudget(rawValue === "" ? 0 : Number(rawValue)); // 예산 상태 업데이트
        }}
      />
      <div className="button-container">
        <Button
          size="S"
          name="확인"
          $bgColor="blue"
          onClick={goToWhere6}
          disabled={isButtonDisabled} // 예산이 0이면 버튼 비활성화
        />
        <Button
          size="XL"
          name="예산 설정 없이 기록 시작"
          $bgColor="red"
          onClick={goToWhere6}
        />
      </div>
      <div className="chaGok">0부터 차곡차곡</div>
    </div>
  );
};

export default Where5;
