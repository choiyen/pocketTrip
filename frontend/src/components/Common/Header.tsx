import React, { use, useEffect, useState } from "react";
import Nav from "./Nav";
import styled, { keyframes } from "styled-components";
import Modal from "./Modal";
import { useSelector } from "react-redux";
import { RootState } from "../../store";
import { IoIosArrowBack } from "react-icons/io";
import { BsPersonSquare } from "react-icons/bs";
import { FaChartPie } from "react-icons/fa";
import { Link, useLocation, useNavigate } from "react-router-dom";
import OptionButton from "./OptionButton";

interface HeaderState {
  $bgColor?: string;
  userData?: { name: string; profile: string };
  id?: string;
}

const UserWrap = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  h2 {
    font-size: 17px;
  }

  strong {
    font-weight: bold;
  }
`;
const MainPageWrap = styled.div`
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;

  img {
    align-self: flex-end;
  }

  span {
    display: block;
  }

  .month {
    font-size: 30px;
    font-weight: 900;
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .year {
    font-size: 24px;
    font-weight: 300;
  }
`;
const DateWrap = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
`;
const HeaderWrap = styled.div<{ $bgColor: string; $pathName: string }>`
  padding: 20px;
  background-color: ${(props) => props.$bgColor};
  display: flex;
  justify-content: space-between;
  box-shadow: ${(props) =>
    props.$pathName === "/" ? "0px 3px 8.1px rgba(0,0,0,0.09)" : "none"};
`;

const BackButton = styled.button`
  padding: 0;
  width: 33px;
  height: 33px;
  background-color: transparent;
  border: none;
`;

const ButtonBox = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  button.optionButton {
    background-color: transparent;
    border: none;
    width: 40px;
  }
`;

export default function Header({
  $bgColor = "transparent",
  userData,
  id,
}: HeaderState) {
  const [pathName, setPathName] = useState("");
  const navigate = useNavigate();
  const location = useLocation();
  useEffect(() => {
    setPathName(location.pathname);
  }, []);

  // 리덕스로 모달 상태 글로벌 참조
  const modalState = useSelector(
    (state: RootState) => state.modalControl.modalState
  );

  // 오늘 날짜 계산
  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  const today = new Date();
  const date = today.getDate();
  const month = months[today.getMonth()];
  const year = today.getFullYear();

  const navPath = () => {
    switch (pathName) {
      case `/Tour/${id}`:
        navigate("/");
        break;
      case `/Tour/${id}/accountbook`:
        navigate(`/Tour/${id}`);
        break;
      case `/Tour/${id}/TourMembers`:
        navigate(`/Tour/${id}`);
        break;
      case `/Tour/${id}/MoneyChart`:
        navigate(`/Tour/${id}`);
        break;
    }
  };

  return (
    <HeaderWrap $bgColor={$bgColor} $pathName={pathName}>
      {/* 세부 페이지에서의 뒤로가기 버튼 설정*/}
      {pathName !== "/" && pathName !== "/mypage" && (
        <BackButton onClick={() => navPath()}>
          <IoIosArrowBack size={"25px"} />
        </BackButton>
      )}
      {/* 경로가 지갑페이지일때 */}
      {pathName === `/Tour/${id}` && (
        <ButtonBox>
          {/* <div> */}
          <Link to={`/Tour/${id}/MoneyChart`}>
            <button className="optionButton">
              <FaChartPie size={"25px"} />
            </button>
          </Link>
          <Link to={`/Tour/${id}/TourMembers`}>
            <button className="optionButton">
              <BsPersonSquare size={"25px"} />
            </button>
          </Link>
          <OptionButton />
          {/* </div> */}
        </ButtonBox>
      )}

      {/* 경로가 메인페이지일때 */}
      {pathName === "/" && (
        <MainPageWrap>
          <UserWrap>
            <h2>
              어서오세요, <strong>{userData ? userData.name : "테스터"}</strong>
              님! ✈️
            </h2>
            <img
              src={"./" + userData?.profile}
              alt="프로필 사진"
              width="50px"
              height="50px"
            />
          </UserWrap>
          <DateWrap>
            {/* 테두리만 있는 숫자(date)는 svg로 구현 */}
            <svg width="105px" height="60">
              <text
                x="0"
                y="50"
                fontSize="60px"
                stroke="#051E31"
                fill="none"
                strokeWidth="2"
              >
                {+date < 10 ? "0" + date : date}
              </text>
            </svg>

            <div>
              <span className="month">{month}</span>
              <span className="year">{year}</span>
            </div>
          </DateWrap>
        </MainPageWrap>
      )}

      {/* 필요에 따라 모달창 활성화 */}
      {modalState && <Modal />}

      {/* 네비 바 */}
      <Nav />
    </HeaderWrap>
  );
}
