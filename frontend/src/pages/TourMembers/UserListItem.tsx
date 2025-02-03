import React from "react";
import styled from "styled-components";
interface UserInfoProps {
  name: string;
  profile: string;
}

export default function UserListItem({ name, profile }: UserInfoProps) {
  return (
    <li>
      <img src={"./" + profile} alt="프로필 사진" width="40px" height="40px" />
      <div>
        <h2>이름</h2>
        <span>{name}</span>
      </div>
    </li>
  );
}
