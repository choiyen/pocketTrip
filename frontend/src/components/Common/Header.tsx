import React, { useState } from "react";
import Nav from "./Nav";
import styled, { keyframes } from "styled-components";
import Modal from "./Modal";
import { useSelector } from "react-redux";
import { RootState } from "../../store";

export default function Header() {
  // 리덕스로 모달 상태 글로벌 참조
  const modalState = useSelector(
    (state: RootState) => state.modalControl.modalState
  );

  return (
    <div>
      Header
      {modalState && <Modal />}
      <Nav />
    </div>
  );
}
