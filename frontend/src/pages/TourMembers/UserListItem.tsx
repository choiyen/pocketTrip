import React from "react";
import styled from "styled-components";
import OptionButton from "../../components/Common/OptionButton";
interface UserInfoProps {
  name: string;
  profile: string;
}

const ListItem = styled.li`
  display: flex;
  padding: 10px;
  border-radius: 20px;
  margin-bottom: 10px;
  div {
    margin: 0 0 0 20px;
    padding-bottom: 10px;
    border-bottom: 2px solid #dedede;
    flex-grow: 1;
  }
  div > h2 {
    font-size: 12px;
    color: #919191;
    margin-bottom: 5px;
  }
  div > span {
    font-size: 18px;
    color: #051e31;
  }
`;

export default function UserListItem({ name, profile }: UserInfoProps) {
  return (
    <ListItem>
      <img src={"/" + profile} alt="프로필 사진" width="40px" height="40px" />
      <div>
        <h2>이름</h2>
        <span>{name}</span>
      </div>
    </ListItem>
  );
}
