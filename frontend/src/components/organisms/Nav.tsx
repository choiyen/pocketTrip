import React from "react";
import styled from "styled-components";
import NavButton from "../atoms/NavButton";

const Navbar = styled.div`
  max-width: 768px;
  height: 56px;
  background-color: white;
  position: fixed;
  width: 100%;
  bottom: 0;
  border-radius: 15px;
  box-shadow: 0px -1px 2px 0px rgba(0, 0, 0, 0.25);
  display: flex;
  justify-content: space-around;
`;

export default function Nav() {
  return (
    <Navbar>
      <NavButton where="home" />
      <NavButton where="mypage" />
    </Navbar>
  );
}
