import React from "react";
import styled from "styled-components";
const HistoryWrap = styled.div`
  display: flex;
  justify-content: space-around;
  max-width: 700px;
  margin: 30px auto;
`;
const Buttons = styled.button`
  width: 110px;
  background-color: transparent;
  border: 2px solid #051e31;
  border-radius: 20px;
  font-size: 16px;
  font-weight: bold;
  font-family: inherit;
  padding: 5px;
`;

export default function Usehistory() {
  return (
    <HistoryWrap>
      <Buttons>종합</Buttons>
      <Buttons>카드</Buttons>
      <Buttons>현금</Buttons>
    </HistoryWrap>
  );
}
