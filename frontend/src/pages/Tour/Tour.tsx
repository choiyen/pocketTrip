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
import Modal from "../..//components/Common/Modal";
import AccountModal from "./AccountModal";

export interface MoneyLogProps {
  LogState: "plus" | "minus";
  title: string;
  detail: string;
  profile: string;
  type: "카드" | "현금";
  money: string;
}
interface TravelPlan {
  id: string;
  travelCode: string;
  title: string;
  founder: string;
  location: string;
  startDate: string; // 날짜 문자열
  endDate: string; // 날짜 문자열
  expense: number;
  calculate: boolean;
  participants: string[]; // 참가자 리스트 (배열)
  encryptCode: string;
}

interface PaymentState {
  amount: string | null;
  selectedUser: selectedUserType | null;
  paymentType: string | null;
}
type selectedUserType = { name: string; email: string };

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
  const [TourDataArr, setTourDataArr] = useState<TravelPlan[]>([]);
  const [FilteringData, setFilteringData] = useState<TravelPlan[]>([]);
  const dispatch: AppDispatch = useDispatch();
  const { encrypted } = useParams<{ encrypted: string }>();

  const { state } = useLocation(); // 메인 / 마이페이지 어디서 들어온 경로인지 판별
  const fromPage = state.from; // "/" 혹은 "/mypage" 경로 추출
  const [modalVisible, setModalVisible] = useState<boolean>(false); // 모달 생성
  const [modalMoving, setModalMoving] = useState<boolean>(false); // 모달 움직임 제어
  const [accountModalContent, setAccountModalContent] = useState<
    "AccountBook" | "categories"
  >("AccountBook");

  useEffect(() => {
    dispatch(savePath(fromPage)); // 뒤로가기 경로 설정
    const decode = decrypt(encrypted!); // 여행코드 해독
    setTravelCodes(decode); // 여행코드 저장
    getTravelData(token!); // 모든 여행 리스트 요청
  }, []);

  // 여행 리스트와 코드를 기반으로 하나의 여행 선택
  useEffect(() => {
    setFilteringData(
      TourDataArr.filter((item) => item.travelCode === travelCodes)
    );
  }, [TourDataArr]);

  const { amount, paymentType, description, category } = state || {};

  // 여행 코드에 맞는 비용 내역 불러오는 코드
  useEffect(() => {
    if (!travelCodes) return;

    const fetchSpendingLogs = async () => {
      try {
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

  // 유저의 모든 여행 기록을 받아와서 암호화 코드를 추가 한다.
  const getTravelData = async (token: string) => {
    const response = await axios.post(
      `${process.env.REACT_APP_API_BASE_URL}/plan/find`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );
    const TourData = response.data.data;
    setTourDataArr(TourData);
  };

  // 버튼 동작에 따라서 모달창이 on/off된다.
  const ChangeState = () => {
    // 모달창이 렌더링 되기 전이면 렌더링 후 등장
    if (modalVisible === false) {
      setModalVisible(true);
      setTimeout(() => {
        setModalMoving(true);
      }, 50);
    } else {
      // // 모달창이 렌더링 되어 있는 상태면 내리는 동작 이후 제거
      setModalMoving(false);
      setTimeout(() => {
        setModalVisible(false);
      }, 500);
      setAccountModalContent("AccountBook");
    }
  };
  return (
    <div>
      <Header $bgColor={"white"} encrypted={encrypted} fromPage={fromPage} />
      {FilteringData[0] && <TourInfo Tourdata={FilteringData[0]} />}
      {FilteringData[0] && (
        <MoneyInfo Tourdata={FilteringData[0]} ChangeState={ChangeState} />
      )}
      <Usehistory logs={logs} />
      {modalVisible && (
        <AccountModal
          modalMoving={modalMoving}
          ChangeState={ChangeState}
          travel={FilteringData[0]}
          accountModalContent={accountModalContent}
          setAccountModalContent={setAccountModalContent}
        />
      )}
    </div>
  );
}
