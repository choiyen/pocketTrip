import React from "react";
import styled from "styled-components";
import OptionButton from "../../components/Common/OptionButton";
import CardUserList from "../Home/CardUserList";
import { Link } from "react-router-dom";

interface TravelPlan {
  travel: {
    id: string;
    encryptCode: string;
    travelCode: string;
    title: string;
    location: string;
    startDate: string;
    endDate: string;
    expense: number;
    profiles: string[];
    bgImg?: string;
  };
  formattedBudget: string[];
  index: number;
}
const TrvelWrap = styled.div`
  position: relative;
  .travelButton {
    position: absolute;
    z-index: 1;
    top: 10px;
    right: 10px;
    margin: 10px 0;

    & button svg {
      fill: white;
    }

    & ul li button svg {
      fill: currentColor;
    }
  }
`;

const Travel = styled(Link)<{ $bgImg?: string }>`
  width: 85vw;
  /* background-color: #0077cc; */
  background: ${(props) =>
    props.$bgImg
      ? `linear-gradient(
      to top,
      rgba(0, 0, 0, 0.6) 0%,
      rgba(0, 0, 0, 0.6) 100%
    ),url(${props.$bgImg})`
      : "#5a6974"};
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  /* background-color: #5a6974; */
  border-radius: 15px;
  margin: 15px 0;
  /* position: relative; */
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  color: white;
`;

const Title = styled.h2`
  font-size: 20px;
  font-weight: 700;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
`;

const Duration = styled.span`
  font-size: 12px;
  font-weight: 500;
  color: white;
  line-height: 1.5;
  margin: 5px 0 20px;
`;

const Expense = styled.strong`
  font-size: 20px;
  font-weight: 500;
  margin-top: 10px;
`;

const Location = styled.div`
  padding: 20px;
`;

const SmallUserBox = styled(CardUserList)`
  /* transform: scale(0.8); */
`;

export default function TourCardList({
  travel,
  formattedBudget,
  index,
}: TravelPlan) {
  return (
    <TrvelWrap>
      <Travel
        to={`/Tour/${travel.encryptCode}`}
        $bgImg={travel.bgImg ? travel.bgImg : "/japan.jpg"}
        state={{ from: "/mypage" }}
      >
        <div
          style={{
            padding: "20px",
            display: "flex",
            flexDirection: "column",
            height: "100%",
            justifyContent: "space-around",
          }}
        >
          <Title>{travel.title}</Title>
          <Duration>
            {travel.startDate} - {travel.endDate}
          </Duration>
          {/* 백엔드 수정 후 user 추가 */}
          <SmallUserBox $size={"S"} />
          <Expense>₩ {formattedBudget[index]}</Expense>
        </div>
        <div>
          <Location>{travel.location}</Location>
        </div>
      </Travel>
      <OptionButton className="travelButton" editType="editTourCardList" />
    </TrvelWrap>
  );
}
