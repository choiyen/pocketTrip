import React from "react";
import styled from "styled-components";
import { FcFullTrash } from "react-icons/fc";
import Button from "./Button";

const BoxWrap = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 300px;
  background-color: white;
  border-radius: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  box-sizing: border-box;
  text-align: center;

  h2 {
    margin-top: 30px;
    text-align: center;
    line-height: 1.5;
    font-size: 20px;
    font-weight: bold;
  }

  p {
    margin: 10px 0;
    color: #a5a5a5;
  }
`;

export default function AlertBox() {
  return (
    <BoxWrap>
      <FcFullTrash size={"80px"} />
      <h2>삭제</h2>
      <p>정말 삭제하시겠습니까?</p>
      <Button size="M" name="지우기" $bgColor="red" />
    </BoxWrap>
  );
}
