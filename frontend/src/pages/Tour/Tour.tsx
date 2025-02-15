import React, { useEffect, useMemo, useRef, useState } from "react";
import Header from "../../components/Common/Header";
import TourInfo from "./TourInfo";
import { useLocation, useParams } from "react-router-dom";
import MoneyInfo from "./MoneyInfo";
import Usehistory from "./Usehistory";
import { io } from "socket.io-client";
import SockJS from "sockjs-client"; // SockJS 추가
import { AppDispatch, RootState } from "../../store";
import { useDispatch, useSelector } from "react-redux";
import { savePath } from "../../slices/RoutePathSlice";
import axios from "axios";
import CryptoJS from "crypto-js";
import { Client, CompatClient, Stomp } from "@stomp/stompjs";

import { m } from "framer-motion";

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
//   }
// ];

export default function Tour() {
  const SOCKET_URL = process.env.REACT_APP_SOCKET_BASE_URL;
  const token = localStorage.getItem("accessToken");
  const SECRET_KEY = process.env.REACT_APP_SECRET_KEY!;
  const IV = CryptoJS.enc.Utf8.parse("1234567890123456"); // 16바이트 IV
  const stompClientRef = useRef<Client | null>(null);
  const decrypt = (encryptedData: string) => {
    // URL-safe Base64 복구
    const base64 = encryptedData.replace(/-/g, "+").replace(/_/g, "/");

    const decrypted = CryptoJS.AES.decrypt(
      base64,
      CryptoJS.enc.Utf8.parse(SECRET_KEY),
      {
        iv: IV,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7,
      }
    );

    return decrypted.toString(CryptoJS.enc.Utf8); // 복호화된 문자열 반환
  };
  const [travelCodes, setTravelCodes] = useState<string>();
  const [logs, setLogs] = useState<MoneyLogProps[]>([]);
  const dispatch: AppDispatch = useDispatch();
  const data = useSelector((state: RootState) => state.saveTourData);
  const { encrypted } = useParams<{ encrypted: string }>();

  // 뒤로가기 누를때 메인에서 온거면 메인, 마이페이지에서 온거면 그곳으로 되돌아가야한다.
  const { state } = useLocation(); // 메인 / 마이페이지 어디서 들어온 경로인지 판별
  const fromPage = state.from; // "/" 혹은 "/mypage" 경로 추출

  // 홈 혹은 마이페이지 중 어느 경로로 들어온건지 저장 (뒤로가기 기능)
  useEffect(() => {
    dispatch(savePath(fromPage));
  }, []);

  // url의 암호화 여행코드 복호화해서 저장
  useEffect(() => {
    const decode = decrypt(encrypted!);
    setTravelCodes(decode);
  }, [encrypted]);

  // 여행 정보들 중 여행코드가 일치하는 데이터만 고른다.
  const FilteringData = data.value.filter(
    (item) => item.encryptCode === encrypted
  );

  const { amount, paymentType, description, category } = state || {};

  // 여행 코드에 맞는 비용 내역 불러오는 코드
  useEffect(() => {
    if (!travelCodes) return;

    const fetchSpendingLogs = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/expenditures/${travelCodes}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );
        setLogs(response.data); // 서버에서 받은 데이터를 logs에 저장
      } catch (error) {
        console.error("지출 내역 불러오기 실패:", error);
      }
    };
  }, [travelCodes]);

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
    if (!token) {
      console.error("❌ AccessToken이 없습니다. WebSocket 연결 불가.");
      return;
    }
    // 재연결 방지
    if (stompClientRef.current && stompClientRef.current.active) {
      console.log("✅ 이미 WebSocket이 활성화되어 있습니다.");
      return;
    }

    if (!SOCKET_URL) return;

    // 소켓 연결 시작
    const socket = new SockJS(SOCKET_URL);

    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      debug: (msg) => console.log(msg),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    stompClient.onConnect = () => {
      console.log("연결 성공");

      // ✅ 서버에서 메시지를 받을 구독 경로 설정
      stompClient.subscribe(`/queue/${travelCodes}`, (message) => {
        console.log("📩 받은 메시지:", message.body);
      });

      // ✅ 서버로 메시지를 보내기
      stompClient.publish({
        destination: `/travelPlan/${travelCodes}`,
        body: JSON.stringify({ sender: "user1", content: "Hello WebSocket!" }),
      });
    };

    stompClient.onStompError = (frame) => {
      console.error("소켓 오류", frame);
    };

    // stompClient.activate();

    stompClientRef.current = stompClient;

    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.deactivate();
      }
    };
  }, []);

  return (
    <div>
      <Header $bgColor={"white"} encrypted={encrypted} fromPage={fromPage} />
      <TourInfo Tourdata={FilteringData[0]} />
      <MoneyInfo Tourdata={FilteringData[0]} />
      <Usehistory logs={logs} />
    </div>
  );
}
