import React from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import styled from "styled-components";
import { RootState } from "../../store";
import { FaHouse } from "react-icons/fa6";

interface NavButtonProps {
  where: string; // where는 문자열 타입
}

interface ButtonProps {
  $isSelected?: boolean;
}

const Linked = styled(Link)<ButtonProps>`
  width: 72px;
  height: 56px;
  font-size: 20px;
  text-decoration: none;
  color: ${(props) => (props.$isSelected ? "#0077CC" : "#E1E1E1")};
  text-align: center;
  font-family: GmarketSansMedium, Arial, Helvetica, sans-serif;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
`;

const HomeLinked = styled(Linked)``;

const MypageLinked = styled(Linked)``;

const Span = styled.span`
  padding-bottom: 5px;
  font-size: 12px;
`;

export default function NavButton({ where }: NavButtonProps) {
  const value = useSelector((state: RootState) => state.currentPage.value);
  return (
    <>
      {where === "home" ? (
        <HomeLinked to="/" $isSelected={value === "home" ? true : false}>
          <FaHouse />
          <Span>홈화면</Span>
        </HomeLinked>
      ) : (
        <MypageLinked
          to="mypage"
          $isSelected={value === "mypage" ? true : false}
        >
          <Span>내 정보</Span>
        </MypageLinked>
      )}
    </>
  );
}
