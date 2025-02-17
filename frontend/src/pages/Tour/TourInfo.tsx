import React from "react";
import styled from "styled-components";
import TourDateUi from "../../components/Common/TourDateUi";

interface TravelData {
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

interface TourCardProps {
  Tourdata: TravelData; // props 타입 정의
}

const TourWrap = styled.div`
  padding: 20px;
  background-color: white;
  position: relative;
  box-shadow: 0px 9px 9px rgba(0, 0, 0, 0.1);

  h2 {
    font-size: 16px;
    color: #919191;
    margin-bottom: 10px;
  }
  h3 {
    font-size: 40px;
    font-weight: bold;
    color: #051e31;
    margin-bottom: 20px;
  }

  &::after {
    content: "";
    width: 0px;
    height: 0px;
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translate(-50%, -1px);
    border-top: 20px solid white;
    border-left: 20px solid transparent;
    border-right: 20px solid transparent;
    border-bottom: 20px solid transparent;
    filter: drop-shadow(0px 6px 6px rgba(0, 0, 0, 0.1));
  }
`;
export default function TourInfo({ Tourdata }: TourCardProps) {
  // 참여유저의 프로필 이미지를 모두 가져오면 알아서 ui가 조정된다.
  const startDate = new Date(Tourdata.startDate);
  const endDate = new Date(Tourdata.endDate);
  const today = new Date();

  // 시작 종료일 시간 차이 계산
  const totalDuration = endDate.getTime() - startDate.getTime();
  // 오늘과 시작일 차이 계산
  const progressDuration = today.getTime() - startDate.getTime();
  // 진행률 0과 100 사이로 제한두고 계산
  const progress =
    totalDuration > 0
      ? Math.min(100, Math.max(0, (progressDuration / totalDuration) * 100))
      : 0;
  return (
    <TourWrap>
      <h2>여행지</h2>
      <h3>{Tourdata.location}</h3>
      <TourDateUi
        $precent={progress ? progress.toFixed(2) + "%" : "10%"}
        startDate={Tourdata.startDate}
        endDate={Tourdata.endDate}
        $bgColor="black"
        $backGraphColor="#E9E9E9"
      />
    </TourWrap>
  );
}
