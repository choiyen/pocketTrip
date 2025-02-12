import React, { useEffect, useMemo, useState } from "react";
import Header from "../../components/Common/Header";
import TourInfo from "./TourInfo";
import { useLocation, useParams } from "react-router-dom";
import MoneyInfo from "./MoneyInfo";
import Usehistory from "./Usehistory";
import { io } from "socket.io-client";
import { AppDispatch, RootState } from "../../store";
import { useDispatch, useSelector } from "react-redux";
import { savePath } from "../../slices/RoutePathSlice";
const SECRET_KEY = process.env.REACT_APP_SECRET_KEY!;

const decrypt = (ciphertext: string): string => {
  const bytes = CryptoJS.AES.decrypt(ciphertext, SECRET_KEY);
  return bytes.toString(CryptoJS.enc.Utf8);
};

export interface MoneyLogProps {
  LogState: "plus" | "minus";
  title: string;
  detail: string;
  profile: string;
  type: "카드" | "현금";
  money: string;
}

// const data = [
//   {
//     id: "1",
//     travelCode: "sdsdds",
//     title: "일본여행지갑", // 여행지갑 이름
//     location: "일본", // 여행지 이름
//     expense: 2000000, // 현재 누적 금액
//     ImgArr: [
//       "./ProfileImage.png",
//       "./ProfileImage.png",
//       "./ProfileImage.png",
//       "./ProfileImage.png",
//       "./ProfileImage.png",
//       "./ProfileImage.png",
//       "./ProfileImage.png",
//     ], // 참여인원들 프로필 이미지 주소
//     startDate: "2025-01-18", // 여행 시작일
//     endDate: "2025-02-20", // 여행 종료일
//     bgImg: "./japan.jpg",
//   },
//   {
//     id: "2",
//     travelCode: "ddddddd",
//     title: "미국 여행의 방", // 여행지갑 이름
//     location: "미국", // 여행지 이름
//     expense: 1000000,
//     ImgArr: ["./ProfileImage.png", "./ProfileImage.png"], // 참여인원들 프로필 이미지 주소
//     startDate: "2024-10-20",
//     endDate: "2024-10-25",
//   },
//   {
//     id: "3",
//     travelCode: "sdfsdfdfdfdf",
//     title: "프랑스 여행의 방",
//     location: "프랑스", // 여행지 이름
//     expense: 2500000,
//     ImgArr: ["./ProfileImage.png"], // 참여인원들 프로필 이미지 주소
//     startDate: "2024-05-02",
//     endDate: "2024-05-10",
//   },
// ];
const SERVER_URL = process.env.REACT_APP_SERVER || "";

export default function Tour() {
  const dispatch: AppDispatch = useDispatch();
  const data = useSelector((state: RootState) => state.saveTourData);

  const { encrypted } = useParams<{ encrypted: string }>();

  // 뒤로가기 누를때 메인에서 온거면 메인, 마이페이지에서 온거면 그곳으로 되돌아가야한다.
  const { state } = useLocation(); // 메인 / 마이페이지 어디서 들어온 경로인지 판별
  const fromPage = state.from; // "/" 혹은 "/mypage" 경로 추출

  useEffect(() => {
    dispatch(savePath(fromPage));
  }, []);

  const [logs, setLogs] = useState<MoneyLogProps[]>([]);
  const FilteringData = data.value.filter(
    (item) => item.encryptCode === encrypted
  );
  console.log(data.value, encrypted);

  const { amount, paymentType, description, category } = state || {};

  useEffect(() => {
    if (category) {
      setLogs([
        {
          LogState: "minus",
          title: category.label,
          detail: description || "설명 없음",
          profile: "/ProfileImage.png",
          type: paymentType === "cash" ? "현금" : "카드",
          money: Number(amount).toLocaleString(),
        },
      ]);
    }
  }, [amount, paymentType, description, category]);

  // 소켓 통신 (필요시 추가)
  useEffect(() => {
    const newSocket = io(SERVER_URL, {
      query: { travelCode: encrypted },
      reconnectionAttempts: 1,
      timeout: 500,
    });

    newSocket.on("connect", () => {
      console.log("소켓 연결됨!");
    });

    newSocket.on("moneyLogs", (data) => {
      setLogs(data);
    });

    newSocket.on("connect_error", (error) => {
      console.error("소켓 연결 오류:", error.message);
    });

    return () => {
      if (newSocket) {
        newSocket.off("connect");
        newSocket.off("moneyLogs");
        newSocket.off("connect_error");
        newSocket.disconnect();
      }
    };
  }, [encrypted]);

  return (
    <div>
      <Header $bgColor={"white"} encrypted={encrypted} fromPage={fromPage} />
      <TourInfo Tourdata={FilteringData[0]} />
      <MoneyInfo Tourdata={FilteringData[0]} />
      <Usehistory logs={logs} />
    </div>
  );
}
