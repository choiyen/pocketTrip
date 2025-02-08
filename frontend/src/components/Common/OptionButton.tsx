import React, { useState } from "react";
import { SlOptionsVertical } from "react-icons/sl";
import { HiMiniPencilSquare } from "react-icons/hi2";
import { FaTrash } from "react-icons/fa";
import styled from "styled-components";
import { AppDispatch, RootState } from "../../store";
import { useDispatch, useSelector } from "react-redux";
import {
  ChangeModalState,
  ChangeMovingModal,
} from "../../slices/ModalControlSlice";
import { ChangeAlertState } from "../../slices/AlertControlSlice";

interface OptionButtonProps {
  className?: string;
  edit?: boolean;
  remove?: boolean;
}

export const OptionWrap = styled.div`
  position: relative;
  /* position: absolute;
  top: 10px;
  right: 10px; */
`;
const Button = styled.button`
  width: 40px;
  height: 40px;
  font-size: 18px;
  background-color: transparent;
  border: none;
`;
const OptionMenu = styled.ul`
  background-color: white;
  position: absolute;
  width: min-content;
  z-index: 1;
  top: 100%;
  right: 100%;
  width: max-content;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
`;
const MenuButton = styled.button`
  width: 123px;
  font-size: 15px;
  font-family: inherit;
  white-space: nowrap;
  text-align: left;
  padding: 10px;
  line-height: 1.2;
  display: flex;
  align-items: center;
  background-color: transparent;
  border: none;
  border-radius: 10px;
  color: #5c5c5c;
  &:hover {
    background-color: #e0e0e0;
  }
  svg {
    font-size: 18px;
    margin-right: 5px;
  }
`;

const DeleteButton = styled(MenuButton)`
  color: #cc0003;
`;

export default function OptionButton({
  className,
  edit = true,
  remove = true,
}: OptionButtonProps) {
  const [visibleOption, setVisibleOption] = useState(false);

  const dispatch: AppDispatch = useDispatch();
  const modalState = useSelector(
    (state: RootState) => state.modalControl.modalState
  );

  // 버튼 동작에 따라서 모달창이 on/off된다.
  const ChangeState = () => {
    // 모달창이 렌더링 되기 전이면 렌더링 후 등장
    if (modalState === false) {
      dispatch(ChangeModalState());
      setTimeout(() => {
        dispatch(ChangeMovingModal());
      }, 50);
    } else {
      // // 모달창이 렌더링 되어 있는 상태면 내리는 동작 이후 제거
      dispatch(ChangeMovingModal());
      setTimeout(() => {
        dispatch(ChangeModalState());
      }, 500);
    }
    setVisibleOption(false);
  };

  const showAlert = () => {
    dispatch(ChangeAlertState());
    setVisibleOption(false);
  };

  return (
    <OptionWrap className={className}>
      <Button onClick={() => setVisibleOption((prev) => !prev)}>
        <SlOptionsVertical />
      </Button>
      {visibleOption && (
        <OptionMenu>
          {edit && (
            <li>
              <MenuButton onClick={() => ChangeState()}>
                <HiMiniPencilSquare />
                수정
              </MenuButton>
            </li>
          )}
          {remove && (
            <li>
              <DeleteButton onClick={() => showAlert()}>
                <FaTrash />
                삭제
              </DeleteButton>
            </li>
          )}
        </OptionMenu>
      )}
    </OptionWrap>
  );
}
