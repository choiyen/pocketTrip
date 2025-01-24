import React from "react";
import styled from "styled-components";

// 사용방법
interface ButtonState {
  size: "L" | "M" | "S"; // 크기 설정
  name: string; // 버튼 이름
  $bgColor?: "green" | "red" | "blue" | "transparent"; // 버튼 색
  onClick?: () => void; // 클릭 메서드
  disabled?: boolean; // 추가된 disabled prop
}

const CutomButton = styled.button<{ $bgColor: string; disabled: boolean }>`
  letter-spacing: 1px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  background-color: ${(props) =>
    props.disabled
      ? "#d3d3d3"
      : props.$bgColor === "transparent"
      ? "transparent"
      : "#0077cc"};
  color: ${(props) => (props.$bgColor === "transparent" ? "#121212" : "white")};
  border: none;
  font-family: GmarketSansMedium, Arial, Helvetica, sans-serif;
  font-weight: bold;
  box-sizing: border-box;
  opacity: ${(props) => (props.disabled ? 0.4 : 1)};
`;

const LargeButton = styled(CutomButton)<{
  $bgColor: string;
  disabled: boolean;
}>`
  font-size: 15px;
  padding: 0 20px;
  height: 40px;
  border-radius: 10px;
  width: clamp(150px, 50vw, 250px);
  background-color: ${(props) =>
    props.$bgColor === "green"
      ? "#4CAF50"
      : props.$bgColor === "red"
      ? "#CC0003"
      : props.$bgColor === "transparent"
      ? "transparent"
      : "#0077cc"};
`;

const MediumButton = styled(CutomButton)<{
  $bgColor: string;
  disabled: boolean;
}>`
  font-size: 16px;
  padding: 0 16px;
  height: 50px;
  border-radius: 10px;
  width: clamp(120px, 50vw, 180px);
  background-color: ${(props) =>
    props.$bgColor === "green"
      ? "#4CAF50"
      : props.$bgColor === "red"
      ? "#CC0003"
      : "#0077cc"};
`;

const SmallButton = styled(CutomButton)<{
  $bgColor: string;
  disabled: boolean;
}>`
  font-size: 14px;
  padding: 0 12px;
  height: 40px;
  border-radius: 10px;
  width: clamp(80px, 25vw, 150px);
  background-color: ${(props) =>
    props.$bgColor === "green"
      ? "#4CAF50"
      : props.$bgColor === "red"
      ? "#CC0003"
      : "#0077cc"};
`;

export default function Button({
  size,
  name,
  $bgColor = "blue",
  onClick,
  disabled = false,
}: ButtonState & { disabled?: boolean }) {
  return (
    <>
      {size === "L" ? (
        <LargeButton disabled={disabled} $bgColor={$bgColor} onClick={onClick}>
          {name}
        </LargeButton>
      ) : size === "M" ? (
        <MediumButton disabled={disabled} $bgColor={$bgColor} onClick={onClick}>
          {name}
        </MediumButton>
      ) : (
        <SmallButton disabled={disabled} $bgColor={$bgColor} onClick={onClick}>
          {name}
        </SmallButton>
      )}
    </>
  );
}
