import React from "react";
import Header from "../../components/Common/Header";
import UserListItem from "./UserListItem";
import styled from "styled-components";
import Button from "../../components/Common/Button";
import { useParams } from "react-router-dom";

const CodeWrap = styled.div`
  width: 90%;
  margin: 0 auto;
  border-radius: 20px;
  margin-bottom: 40px;
  padding: 20px;
`;
const CurrentMembersWrap = styled.div`
  padding: 0px 20px;

  ul {
    border-radius: 20px;
    padding: 10px;
  }
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

const StyledButtons = styled(Button)`
  align-self: center;
  background-color: #cc0003;
  margin-top: 30px;
`;

const ContentBox = styled.div`
  background-color: white;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  border-radius: 20px;
  padding: 30px 0;
`;

export default function TourMembers() {
  const { id } = useParams<{ id: string }>();
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
      <Header id={id} />
      <ContentBox>
        <CodeWrap>
          <h2 className="TourMemberTitle">초대코드</h2>
          <span className="InviteCode">1H3D4G</span>
        </CodeWrap>
        <CurrentMembersWrap>
          <h3>현재 참여 인원</h3>
          <ul>
            {userData.map((data, index) => (
              <UserListItem
                key={index}
                name={data.name}
                profile={data.profile}
              />
            ))}
          </ul>
        </CurrentMembersWrap>

        <StyledButtons size="L" name="여행 나가기" $bgColor="red" />
      </ContentBox>
    </TourMembersWrap>
  );
}
