import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { FaPlus } from "react-icons/fa6";

interface UserListProps {
  user: string[];
}
const UserList = styled.ul`
  display: flex;

  li {
    position: relative;
    border-radius: 50%;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: center;
    align-items: center;
  }

  li:nth-child(5) {
    font-size: 20px;
  }
`;

export default function CardUserList({ user }: UserListProps) {
  const [overFour, setOverFour] = useState(false);
  useEffect(() => {
    if (user.length >= 5) {
      setOverFour(true);
    } else {
      setOverFour(false);
    }
  }, [user]);
  let sliceUser;
  if (user.length >= 5) {
    sliceUser = user.slice(0, 4);
  } else {
    sliceUser = [...user];
  }

  return (
    <UserList>
      {sliceUser &&
        sliceUser.map((item, idx) => {
          if (idx >= 4) {
          }
          return (
            <li key={idx}>
              <img src={item} alt="프로필 이미지" width={"30px"} />
            </li>
          );
        })}

      {/* 유저 수 5 이상일 때는 플러스 렌더링 */}
      {overFour && (
        <li>
          <FaPlus />
        </li>
      )}
    </UserList>
  );
}
