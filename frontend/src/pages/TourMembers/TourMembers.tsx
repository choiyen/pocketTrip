import React from "react";
import Header from "../../components/Common/Header";
import UserListItem from "./UserListItem";
import styled from "styled-components";

const CodeWrap = styled.div`
  background-color: white;
  width: 90%;
  margin: 0 auto;
  border-radius: 20px;
  margin-bottom: 40px;
  padding: 20px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
`;
const CurrentMembersWrap = styled.div`
  padding: 0px 20px;
`;
const TourMembersWrap = styled.div`
  .TourMemberTitle {
    font-size: 16px;
    color: #919191;
    text-align: center;
  }

  .InviteCode {
    font-size: 40px;
    color: #051e31;
    letter-spacing: 10px;
    font-weight: bold;
    text-align: center;
    display: block;
    margin-top: 15px;
  }

  h3 {
    font-size: 17px;
    color: #051e31;
    margin-bottom: 20px;
    font-weight: bold;
  }
`;

export default function TourMembers() {
  const userData = [
    {
      name: "홍길동",
      profile: "profileImage.png",
    },
    {
      name: "홍길동",
      profile: "profileImage.png",
    },
    {
      name: "홍길동",
      profile: "profileImage.png",
    },
    {
      name: "홍길동",
      profile: "profileImage.png",
    },
  ];
  return (
    <TourMembersWrap>
      <Header />
      <CodeWrap>
        <h2 className="TourMemberTitle">초대코드</h2>
        <span className="InviteCode">1H3D4G</span>
      </CodeWrap>
      <CurrentMembersWrap>
        <h3>현재 참여 인원</h3>
        <ul>
          {userData.map((data, index) => (
            <UserListItem key={index} name={data.name} profile={data.profile} />
          ))}
        </ul>
      </CurrentMembersWrap>
    </TourMembersWrap>
  );
}
