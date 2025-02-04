import React, { useEffect } from "react";
import Header from "../../components/Common/Header";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../store";
import { ChangeCurrentPage } from "../../slices/currentPageSlice";
import styled from "styled-components";

export default function MyPage() {
  const dispatch: AppDispatch = useDispatch();
  useEffect(() => {
    dispatch(ChangeCurrentPage("mypage"));
  }, []);


  const travelList: any[] = [
    // {
    //   travelCode: "sdfsdfsdf",
    //   title: "일본 여행의 방",
    //   startDate: "25.01.27",
    //   endDate: "25.02.02",
    //   expense: 2000000,
    // },
    // {
    //   travelCode: "ddddddd",
    //   title: "일본 여행의 방",
    //   startDate: "25.01.27",
    //   endDate: "25.02.02",
    //   expense: 2000000,
    // },
    // {
    //   travelCode: "sdfsdfdfdfdf",
    //   title: "일본 여행의 방",
    //   startDate: "25.01.27",
    //   endDate: "25.02.02",
    //   expense: 2000000,
    // }
  ]

  return (
    <div>
      <Header />
      <ProfileContainer>
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="10.5" cy="3.5" r="1.5" fill="#1C1C1C" />
          <circle cx="10.5" cy="10.5" r="1.5" fill="#1C1C1C" />
          <circle cx="10.5" cy="17.5" r="1.5" fill="#1C1C1C" />
        </svg>
        <Profile>
          <img src="" alt="" />
          <span>name</span>
        </Profile>
      </ProfileContainer>
      {travelList.length != 0 ?
        <TravelListContainer>
          <TravelList>
            {travelList.map((travel) => {
              return (
                <Travel id={travel.travelCode}>
                  <div style={{ padding: "20px", display: "flex", flexDirection: "column", height: "100%", justifyContent: "space-around" }}>
                    <Title>{travel.title}</Title>
                    <Expense>{travel.expense}</Expense>
                  </div>

                  <div style={{ padding: "20px", display: "flex", flexDirection: "column", height: "100%", justifyContent: "space-between" }}>
                    <Duration>{travel.startDate} - {travel.endDate}</Duration>
                    {/* 참여자 목록 추가 */}
                  </div>
                  <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="10.5" cy="3.5" r="1.5" fill="#FFFFFF" />
                    <circle cx="10.5" cy="10.5" r="1.5" fill="#FFFFFF" />
                    <circle cx="10.5" cy="17.5" r="1.5" fill="#FFFFFF" />
                  </svg>
                </Travel>
              );
            })}

            <AddTravel>
              <div>+</div>
            </AddTravel>
          </TravelList>

        </TravelListContainer>
        :
        <NoTravelList>
          <svg width="172" height="268" viewBox="0 0 172 268" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M58.1718 178.562C58.1718 147.384 66.6618 130.063 89.6161 112.743C108.168 98.5711 118.23 89.1234 118.23 74.0071C118.23 58.2609 106.91 48.4982 87.1005 48.4982C66.0329 48.4982 54.713 63.2997 54.713 79.3608H0C0 34.0117 35.8464 0 86.7861 0C141.185 0 172 31.1775 172 74.0071C172 97.3114 160.68 115.892 140.241 131.638C117.287 149.589 110.055 160.926 110.055 178.562H58.1718ZM85.8428 268C66.6618 268 52.5119 254.143 52.5119 234.933C52.5119 215.723 66.6618 201.551 85.8428 201.551C105.024 201.551 119.174 215.723 119.174 234.933C119.174 254.143 105.024 268 85.8428 268Z" fill="#E8E8E8" fill-opacity="0.45" />
          </svg>
          <div>
            <p>등록된 여행이 없습니다.</p>
            <span>아직 떠날 준비가 안 되셨나요?</span>
            <span>새로운 여행을 추가해보세요!</span>
            <button>여행 추가</button>
          </div>
        </NoTravelList>}
    </div>
  );
}

const ProfileContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;

  position: relative;

  & svg {
    position: absolute;
    top: 0;
    right: 0;
    margin: 10px;
  }
`;


const Profile = styled.div`

display: flex;
flex-direction: column;
margin: 20px 0 0 0;
  
& img {
  border-radius: 100%;
  width: 160px;
  height: 160px;
  background-color: #111111;}

  & span {
    text-align: center;
    font-size: 20px;
    margin: 20px;
  }
`;

const TravelListContainer = styled.div`
  width: 100%;
  height: 60vh;
  // background: black;
  overflow: scroll;
`;

const NoTravelList = styled.div`

  display: flex;
  justify-content: center;
  position: relative;

  & svg {
    position: absolute;
    top: 140px;
    z-index: 0;
  }
  
  & div {
  position: relative;
  z-index: 1;
  background-color: #EAF6FF;
  width: 85vw;
  height: 500px;
  margin: 20px;
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  }

  & p {
    font-size: 24px;
    font-weight: 700;
    margin: 20px;
  }
  
  & span {
    font-size: 16px;
    font-weight: 500;
    text-align: center;
    margin: 5px;
  }

  & button {
    background-color: #0077CC;
    color: white;
    width: 110px;
    height: 35px;
    margin: 20px;
    border-radius: 8px;
    border: 0;
    font-size: 16px;
  }
`;

const TravelList = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Travel = styled.div`
  width: 85vw;
  height: 100px;
  background-color: #005EFF;
  border-radius: 15px;
  margin: 15px 0;
  // position: relative;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  color: white;

  & svg {
    // position: absolute;
    top: 0;
    right: 0;
    margin: 15px 10px;
  }
`;

const AddTravel = styled.div`
  width: 85vw;
  height: 100px;
  background-color: #DFDFDF;
  border-radius: 15px;
  margin: 15px 0;
  display: flex;
  justify-content: center;
  align-items: center;

  & div {
    background-color: #6E6E6E66;
    border-radius: 100%;
    width: 28px;
    height: 28px;
    color: white;
    font-size: 25px;
    text-align: center;
    line-height: 30px;
  }
`;

const Title = styled.div`
  font-size: 20px;
  font-weight: 700;
`;

const Expense = styled.div`
  font-size: 20px;
  font-weight: 500;
`;

const Duration = styled.div`
  font-size: 12px;
  font-weight: 500;
`;