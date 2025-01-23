import React from "react";
import styled from "styled-components";
import TourDateUi from "../../components/Common/TourDateUi";

const Card = styled.div`
  width: 80%;
  height: 422px;
  background-image: linear-gradient(
      to top,
      rgba(0, 0, 0, 1) 0%,
      rgba(0, 0, 0, 0.34) 70%,
      rgba(0, 0, 0, 0) 100%
    ),
    url("./japan.jpg");
  background-size: cover;
  background-position: center;
  margin: 0 auto;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  color: white;
  padding: 15px;

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

export default function TourCard() {
  return (
    <Card>
      <div>
        <h2>일본</h2>
        <p>2,000,000 ₩</p>
        <TourDateUi />
      </div>
    </Card>
  );
}
