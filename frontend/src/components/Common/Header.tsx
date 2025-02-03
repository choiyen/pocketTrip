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

interface HeaderState {
  $bgColor?: string;
  userData?: { name: string; profile: string };
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

  button {
    background-color: transparent;
    border: none;
    width: 40px;
  }
`;

export default function Header({
  $bgColor = "transparent",
  userData,
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
  console.log(date, month, year);
  return (
    <HeaderWrap $bgColor={$bgColor} $pathName={pathName}>
      {pathName !== "/" && pathName !== "/mypage" && (
        <BackButton onClick={() => navigate(-1)}>
          <IoIosArrowBack size={"25px"} />
        </BackButton>
      )}
      {pathName === "/Tour" && (
        <ButtonBox>
          <div>
            <Link to="/TourMembers">
              <button>
                <FaChartPie size={"25px"} />
              </button>
            </Link>
            <Link to="/MoneyChart">
              <button>
                <BsPersonSquare size={"25px"} />
              </button>
            </Link>
          </div>
        </ButtonBox>
      )}
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
            <svg width="105px" height="60">
              <text
                x="0"
                y="50"
                font-size="60px"
                stroke="#051E31"
                fill="none"
                stroke-width="2"
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
      {modalState && <Modal />}
      <Nav />
    </HeaderWrap>
  );
}
