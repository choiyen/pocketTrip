import React, { use, useEffect, useState } from "react";
import styled from "styled-components";
import MoneyLog from "./MoneyLog";
import { io, Socket } from "socket.io-client";
interface MoneyLogProps {
  LogState: "plus" | "minus";
  title: string;
  detail: string;
  profile: string;
  type: "카드" | "현금";
  money: string;
}

const ButtonsWrap = styled.div`
  display: flex;
  justify-content: space-around;
  max-width: 700px;
  margin: 30px auto;
`;
const Buttons = styled.button<{ $selected: boolean }>`
  width: 110px;
  background-color: ${(props) => (props.$selected ? "#051e31" : "transparent")};
  color: ${(props) => (props.$selected ? "white" : "#051e31")};
  border: 2px solid #051e31;
  border-radius: 20px;
  font-size: 16px;
  font-weight: bold;
  font-family: inherit;
  padding: 5px;
`;

const LogList = styled.ul`
  .LogItem:last-child {
    padding-bottom: 70px;
  }
`;

const SERVER_URL = process.env.REACT_APP_SERVER || "";

export default function Usehistory() {
  const [logs, setLogs] = useState<MoneyLogProps[]>([]);
  const [filteringLogs, setFilteringLogs] = useState<MoneyLogProps[]>([]);
  const [tabState, setTabState] = useState("종합");

  // 소켓 통신
  useEffect(() => {
    const newSocket = io(SERVER_URL, {
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
          profile: "./ProfileImage.png",
          type: "카드",
          money: "130,000",
        },
        {
          LogState: "plus",
          title: "급여",
          detail: "월급",
          profile: "./ProfileImage.png",
          type: "현금",
          money: "2,000,000",
        },
        {
          LogState: "minus",
          title: "식비",
          detail: "점심",
          profile: "./ProfileImage.png",
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

  // 탭에 따라서 보이는 목록 필터링
  useEffect(() => {
    if (tabState === "종합") {
      const data = logs.filter(
        (log) => log.type === "카드" || log.type === "현금"
      );
      setFilteringLogs(data);
    } else if (tabState === "카드") {
      const data = logs.filter((log) => log.type === "카드");
      setFilteringLogs(data);
    } else {
      const data = logs.filter((log) => log.type === "현금");
      setFilteringLogs(data);
    }
  }, [tabState, logs]);

  return (
    <div>
      <ButtonsWrap>
        {["종합", "카드", "현금"].map((tab, index) => (
          <Buttons
            key={index}
            $selected={tabState === tab}
            onClick={() => setTabState(tab)}
          >
            {tab}
          </Buttons>
        ))}
      </ButtonsWrap>

      <LogList>
        {filteringLogs.map((log, index) => (
          <MoneyLog
            className="LogItem"
            key={index}
            LogState={log.LogState}
            title={log.title}
            detail={log.detail}
            profile={log.profile}
            type={log.type}
            money={log.money}
          />
        ))}
      </LogList>
    </div>
  );
}
