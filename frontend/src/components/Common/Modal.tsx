import React from "react";
import styled from "styled-components";
import { IoIosArrowDown } from "react-icons/io";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../store";
import {
  ChangeModalState,
  ChangeMovingModal,
} from "../../slices/ModalControlSlice";

const ModalBox = styled.div<{ $isActive: boolean }>`
  background-color: white;
  height: 100%;
  width: 100%;
  max-width: 768px;
  border-radius: 20px 20px 0 0;
  box-shadow: 0px -1px 3px 3px rgba(0, 0, 0, 0.2);
  position: absolute;
  left: 50%;
  z-index: 80;
  transform: ${({ $isActive }) =>
    $isActive ? "translate(-50%, 0vh)" : "translate(-50%, 100vh)"};
  transition-duration: 500ms;
  padding: 20px;
  box-sizing: border-box;
`;

const CloseButton = styled.button`
  width: 100%;
  font-size: 20px;
  background-color: transparent;
  border: none;
  transition-duration: 300ms;
  border-radius: 20px;
  margin-bottom: 20px;

  &:hover {
    background-color: #e1e1e1;
  }
  &:active {
    background-color: #e1e1e1;
  }
`;

export default function Modal() {
  const movingModal = useSelector(
    (state: RootState) => state.modalControl.movingModal
  );

  const dispatch: AppDispatch = useDispatch();
  // 버튼 동작에 따라서 모달창이 on/off된다.
  const ChangeState = () => {
    // // 모달창이 렌더링 되어 있는 상태면 내리는 동작 이후 제거
    dispatch(ChangeMovingModal());
    setTimeout(() => {
      dispatch(ChangeModalState());
    }, 500);
  };
  return (
    <ModalBox $isActive={movingModal}>
      <CloseButton onClick={() => ChangeState()}>
        <IoIosArrowDown />
      </CloseButton>
      childrenss
    </ModalBox>
  );
}
