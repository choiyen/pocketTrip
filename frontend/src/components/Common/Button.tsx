import React from "react";
import styled from "styled-components";

// 백그라운드 속성 스트링에서 블루 레드 그린으로 바꾸기, 미들과 스몰 버튼에도 스타일 변경

interface ButtonState {
  size: "L" | "M" | "S";
  name: string;
  $width?: string;
  $bgColor?: string;
}

const CutomButton = styled.button`
  background-color: #0077cc;
  color: white;
  border: none;
  font-family: GmarketSansMedium, Arial, Helvetica, sans-serif;
  box-sizing: border-box;
`;

const LargeButton = styled(CutomButton)<{ $width: string; $bgColor: string }>`
  font-size: 18px;
  padding: 0 20px;
  height: 56px;
  border-radius: 15px;
  width: ${(props) => props.$width};
  background-color: ${(props) =>
    props.$bgColor === "blue"
      ? "#0077cc"
      : props.$bgColor === "red"
      ? "#CC0003"
      : "#4CAF50"};
`;
const MediumButton = styled(CutomButton)<{ $width: string; $bgColor: string }>`
  font-size: 16px;
  padding: 0 16px;
  height: 40px;
  border-radius: 10px;
  width: ${(props) => props.$width};
  background-color: ${(props) => props.$bgColor};
`;
const SmallButton = styled(CutomButton)<{ $width: string; $bgColor: string }>`
  font-size: 14px;
  padding: 0 12px;
  height: 36px;
  border-radius: 5px;
  width: ${(props) => props.$width};
  background-color: ${(props) => props.$bgColor};
`;
export default function Button({
  size,
  name,
  $width = "auto",
  $bgColor = "#0077cc",
}: ButtonState) {
  return (
    <>
      {size === "L" ? (
        <LargeButton $width={$width} $bgColor={$bgColor}>
          {name}
        </LargeButton>
      ) : size === "M" ? (
        <MediumButton $width={$width} $bgColor={$bgColor}>
          {name}
        </MediumButton>
      ) : (
        <SmallButton $width={$width} $bgColor={$bgColor}>
          {name}
        </SmallButton>
      )}
    </>
  );
}
