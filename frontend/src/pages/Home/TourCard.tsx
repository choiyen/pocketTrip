import React from "react";
import styled from "styled-components";
import TourDateUi from "../../components/Common/TourDateUi";
import CardUserList from "./CardUserList";
import { Link } from "react-router-dom";
import OptionButton from "../../components/Common/OptionButton";

interface TravelData {
  id: string;
  name: string; // 여행지갑 이름
  location: string; // 여행지 이름
  expense: number; // 현재 누적 금액 (통화 단위 포함)
  ImgArr: string[]; // 참여 인원들의 프로필 이미지 경로 배열
  startDate: string; // 여행 시작일 (ISO 날짜 형식)
  endDate: string; // 여행 종료일 (ISO 날짜 형식)
  bgImg?: string;
}
interface TourCardProps {
  Tourdata: TravelData; // props 타입 정의
}
const Card = styled(Link)<{ $bgImg: string }>`
  height: 422px;
  background-image: linear-gradient(
      to top,
      rgba(0, 0, 0, 1) 0%,
      rgba(0, 0, 0, 0.34) 70%,
      rgba(0, 0, 0, 0) 100%
    ),
    url(${(props) => props.$bgImg});
  background-size: cover;
  background-position: center;

  border-radius: 20px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  color: white;
  padding: 15px;
  box-sizing: border-box;
  box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.09);

  h2 {
    font-size: 32px;
    font-weight: bold;
  }
  p {
    font-size: 24px;
    color: #a3a3a3;
    margin: 10px 0;
  }
`;
const TitleWrap = styled.div`
  display: flex;
  justify-content: space-between;
`;

const Container = styled.div`
  position: relative;
  width: 80%;
  margin: 0 auto;
`;
const StyledOptionButton = styled(OptionButton)`
  position: absolute;
  right: 10px;
  top: 10px;
`;

export default function TourCard({ Tourdata }: TourCardProps) {
  const {
    id,
    name,
    location,
    expense,
    ImgArr,
    startDate,
    endDate,
    bgImg = "/japan.jpg",
  } = Tourdata;
  // 참여유저의 프로필 이미지를 모두 가져오면 알아서 ui가 조정된다.
  const startedDate = new Date(startDate);
  const endedDate = new Date(endDate);
  const today = new Date();

  // 시작 종료일 시간 차이 계산
  const totalDuration = endedDate.getTime() - startedDate.getTime();
  // 오늘과 시작일 차이 계산
  const progressDuration = today.getTime() - startedDate.getTime();
  // 진행률 0과 100 사이로 제한두고 계산
  const progress =
    totalDuration > 0
      ? Math.min(100, Math.max(0, (progressDuration / totalDuration) * 100))
      : 0;

  const formattedBudget = new Intl.NumberFormat().format(expense);

  return (
    <Container>
      <Card to={`/Tour/${Tourdata.id}`} state={{ from: "/" }} $bgImg={bgImg}>
        <div>
          <TitleWrap>
            <h2>{location}</h2>
            <CardUserList user={ImgArr} $size="L" />
          </TitleWrap>
          <p>{formattedBudget} ₩</p>
          <TourDateUi
            $precent={progress ? progress.toFixed(2) + "%" : "0%"}
            startDate={startDate}
            endDate={endDate}
            $bgColor="white"
            $backGraphColor="#626262"
          />
          {/* 진행률 기입 시 자동 변경 */}
        </div>
      </Card>
      <StyledOptionButton remove={false} editType="editTourCard" />
    </Container>
  );
}
