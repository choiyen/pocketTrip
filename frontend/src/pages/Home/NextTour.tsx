import React from "react";
<<<<<<< HEAD
import styled from "styled-components";

interface nextTourProps {
  nextTour:
    | {
        selectedCountry: string;
        startDate: string;
        endDate: string;
      }
    | boolean;
}

const BannerWrap = styled.div`
  width: 80%;
  margin: 0 auto;
  display: flex;
  gap: 10px;
  background-color: #ede0d4;
  justify-content: space-evenly;
  padding: 30px 24px 20px;
  position: relative;
  z-index: 0;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.09);
  margin-bottom: 20px;

  img {
    width: 90px;
    height: 64px;
    margin-top: 10px;
  }

  div {
    text-align: center;
  }

  div > h2 {
    font-size: 20px;
    font-weight: 500;
    margin-bottom: 9px;
  }

  div > strong {
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 10px;
    display: block;
  }

  .noPlan {
    margin-bottom: 0;
    margin-top: 10px;
    font-size: 20px;
    line-height: 1.2;
  }

  div > p {
    font-size: 11px;
    font-weight: 500;
    color: #575757;
  }

  &::before {
    content: "";
    position: absolute;
    z-index: -1;
    width: 100%;
    height: 40px;
    background-color: #e1c3a7;
    bottom: 0;
  }
`;

export default function NextTour({ nextTour }: nextTourProps) {
  let leftDate;
  if (typeof nextTour !== "boolean") {
    // D-day 계산
    const today = new Date().getTime();
    const startedDate = new Date(nextTour!.startDate).getTime();
    leftDate = new Date(startedDate - today).getDate() - 1;
  }
  return (
    <BannerWrap>
      <img src="/package.png" alt="여행가방" />
      {typeof nextTour !== "boolean" ? (
        <div>
          <h2>{nextTour.selectedCountry}</h2>
          <strong>D - {leftDate}</strong>
          <p>
            {nextTour.startDate} - {nextTour.endDate}
          </p>
        </div>
      ) : (
        <div>
          <strong className="noPlan">
            여행 계획이 <br /> 없습니다!
          </strong>
        </div>
      )}
    </BannerWrap>
  );
=======

export default function NextTour() {
  return <div>NextTour</div>;
>>>>>>> d2d0d0e (백엔드 연결)
}
