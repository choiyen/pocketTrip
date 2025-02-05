import React, { useEffect, useState } from "react";
import Header from "../../components/Common/Header";
import TourInfo from "./TourInfo";
import { useLocation, useParams } from "react-router-dom";
import MoneyInfo from "./MoneyInfo";
import Usehistory from "./Usehistory";
import { io } from "socket.io-client";

interface MoneyLogProps {
  LogState: "plus" | "minus";
  title: string;
  detail: string;
  profile: string;
  type: "카드" | "현금";
  money: string;
}

// 초대 코드 혹은 클릭했던 여행카드의 id에 맞게 여행 정보를 불러온다.
const data = {
  id: 1,
  name: "일본여행지갑", // 여행지갑 이름
  selectedCountry: "일본", // 여행지 이름
  budget: 2000000, // 현재 누적 금액
  ImgArr: [
    "./ProfileImage.png",
    "./ProfileImage.png",
    "./ProfileImage.png",
    "./ProfileImage.png",
    "./ProfileImage.png",
    "./ProfileImage.png",
    "./ProfileImage.png",
  ], // 참여인원들 프로필 이미지 주소
  startDate: "2025-01-18", // 여행 시작일
  endDate: "2025-02-20", // 여행 종료일
  bgImg: "./japan.jpg",
};
const SERVER_URL = process.env.REACT_APP_SERVER || "";

export default function Tour() {
  const { id } = useParams<{ id: string }>();
  const [logs, setLogs] = useState<MoneyLogProps[]>([]);
  const { state } = useLocation();
  const { Tourdata } = state || {};

  // 소켓 통신
  useEffect(() => {
    const newSocket = io(SERVER_URL, {
      query: { tourId: id },
      reconnectionAttempts: 1,
      timeout: 500,
    });

    // 소켓 연결 상태 확인용
    newSocket.on("connect", () => {
      console.log("소켓 연결됨!");
    });

    // 마운트 시 소켓 연결한다.
    newSocket.on("moneyLogs", (data) => {
      setLogs(data);
    });

    newSocket.on("connect_error", (error) => {
      console.error("소켓 연결 오류:", error.message);
      setLogs([
        {
          LogState: "minus",
          title: "숙소 비용",
          detail: "숙박",
          profile: "/ProfileImage.png",
          type: "카드",
          money: "130,000",
        },
        {
          LogState: "plus",
          title: "급여",
          detail: "월급",
          profile: "/ProfileImage.png",
          type: "현금",
          money: "2,000,000",
        },
        {
          LogState: "minus",
          title: "식비",
          detail: "점심",
          profile: "/ProfileImage.png",
          type: "카드",
          money: "20,000",
        },
      ]);
    });
    // 언마운트 시 소켓 정리
    return () => {
      if (newSocket) {
        newSocket.off("connect");
        newSocket.off("moneyLogs");
        newSocket.off("connect_error");
        newSocket.disconnect();
      }
    };
  }, []);

  return (
    <div>
      <Header $bgColor={"white"} id={id} />
      <TourInfo Tourdata={data} />
      <MoneyInfo Tourdata={data} />
      <Usehistory logs={logs} />
    </div>
  );
}
