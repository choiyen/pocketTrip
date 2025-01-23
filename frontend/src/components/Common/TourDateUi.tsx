import React from "react";
import styled from "styled-components";
import { IoAirplane } from "react-icons/io5";
const DateWrap = styled.div`
  display: flex;
  font-family: inherit;
  justify-content: space-between;
`;
const DateNum = styled.div`
  font-size: 16px;
  font-weight: 200;

  span {
    font-size: 24px;
    font-weight: 400;
  }
`;
const StartDate = styled(DateNum)`
  text-align: left;
`;
const EndDate = styled(DateNum)`
  text-align: right;
`;
const Graph = styled.div`
  width: 100%;
  position: relative;
  &::before {
    content: "";
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 90%;
    height: 5px;
    background-color: #626262;
    border-radius: 30px;
  }
`;

const MovingGraph = styled.div`
  width: 30%; // 움직이는 속성
  position: absolute;
  top: 50%;
  left: 0%;
  transform: translateY(-50%);
  height: 5px;
  background-color: white;
  border-radius: 30px;

  svg {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    right: 0;
  }
`;
export default function TourDateUi() {
  return (
    <DateWrap>
      <StartDate>
        <span>18</span> <br />
        25.01
      </StartDate>
      <Graph>
        <MovingGraph>
          <IoAirplane
            size="30px"
            color="black"
            style={{ transform: "translate(-3px, -50%)", opacity: "0.3" }}
          />
          <IoAirplane size="30px" />
        </MovingGraph>
      </Graph>
      <EndDate>
        <span>25</span> <br />
        25.01
      </EndDate>
    </DateWrap>
  );
}
