import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../store";
import { incrementByAmount, minus, plus } from "../../slices/counterSlice";
import Header from "../../components/Common/Header";
import { ChangeCurrentPage } from "../../slices/currentPageSlice";
import Alert from "../../components/Common/Alert";
import OptionButton from "../../components/Common/OptionButton";
import TourCard from "./TourCard";

export default function MainPage() {
  // 글로벌 상태값을 변경하려면 필요한 함수 usedispatch();
  const dispatch: AppDispatch = useDispatch();
  // 원하는 글로벌 상태값을 불러오기위해 필요한 함수 useselector();
  const value = useSelector((state: RootState) => state.counter.value);
  useEffect(() => {
    dispatch(ChangeCurrentPage("home"));
  }, []);

  // 알림창 관련 로직------------------------------------
  const [isAlertVisible, setIsAlertVisible] = useState(false); // 알림 표시
  const [alertMessage, setAlertMessage] = useState(""); // 알림 메시지
  const [alertType, setAlertType] = useState<"success" | "error" | "info">(
    "success"
  ); // 알림 상태

  // 알림창 상태가 활성화되면 3초 뒤에 다시 끈다.
  useEffect(() => {
    if (isAlertVisible) {
      const timer = setTimeout(() => {
        setIsAlertVisible(false);
      }, 2000);

      return () => clearTimeout(timer);
    }
  }, [isAlertVisible]);

  // 알림창 내용을 결정해 활성화 하는 함수
  const handleAction = () => {
    setAlertMessage("작업이 성공적으로 완료되었습니다.");
    setAlertType("success");
    setIsAlertVisible(true);
  };

  const data = {
    name: "일본", // 여행지 이름
    cost: "2,000,000 ₩", // 현재 누적 금액
    ImgArr: [
      "./ProfileImage.png",
      "./ProfileImage.png",
      "./ProfileImage.png",
      "./ProfileImage.png",
      "./ProfileImage.png",
      "./ProfileImage.png",
      "./ProfileImage.png",
    ], // 참여인원들 프로필 이미지 주소
    startOfDay: "2025-01-18", // 여행 시작일
    endOfDay: "2025-02-20", // 여행 종료일
    bgImg: "./japan.jpg",
  };

  return (
    <div>
      <Header />

      <TourCard Tourdata={data} />
      {/* isAlertVisible 상태값에 따라서 알림창 표시 */}
      {isAlertVisible && (
        <Alert
          alertState={alertType}
          message={alertMessage}
          setIsAlertVisible={setIsAlertVisible}
        />
      )}
    </div>
  );
}
