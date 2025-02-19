import React, { useEffect, useState } from "react";
import Header from "../../components/Common/Header";
import UserListItem from "./UserListItem";
import styled from "styled-components";
import Button from "../../components/Common/Button";
import { useParams } from "react-router-dom";
import CryptoJS, { enc } from "crypto-js";
import axios from "axios";

const CodeWrap = styled.div`
  width: 90%;
  margin: 0 auto;
  border-radius: 20px;
  margin-bottom: 40px;
  padding: 20px;
`;
const CurrentMembersWrap = styled.div`
  padding: 0px 20px;
  height: 50vh;
  overflow: scroll;

  ul {
    border-radius: 20px;
    padding: 10px;
  }

  h3 > span {
    font-weight: 500;
    font-size: 15px;
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

const UserContainer = styled.ul`
  // height: 40vh;
  overflow: scroll;
  scrollbar-width: none;
`;


export default function TourMembers() {

  const [travelCode, setTravelCode] = useState<string>("");
  const [userName, setUserName] = useState<string[]>([]);
  const [userProfile, setUserProfile] = useState<string[]>([]);
  const [applicantsName, setApplicantsName] = useState<string[]>([]);
  const [applicantsProfile, setApplicantsProfile] = useState<string[]>([]);

  const SECRET_KEY = process.env.REACT_APP_SECRET_KEY!;
  const IV = CryptoJS.enc.Utf8.parse("1234567890123456"); // 16바이트 IV
  const decrypt = (encryptedData: string) => {
    // URL-safe Base64 복구
    const base64 = encryptedData.replace(/-/g, "+").replace(/_/g, "/");

    const decrypted = CryptoJS.AES.decrypt(
      base64,
      CryptoJS.enc.Utf8.parse(SECRET_KEY),
      {
        iv: IV,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7,
      }
    );

    return decrypted.toString(CryptoJS.enc.Utf8); // 복호화된 문자열 반환
  };
  const { encrypted } = useParams<{ encrypted: string }>();

  // const userData = [
  //   {
  //     name: "홍길동",
  //     profile: "profileImage.png",
  //   },
  //   {
  //     name: "홍길동",
  //     profile: "profileImage.png",
  //   },
  //   {
  //     name: "홍길동",
  //     profile: "profileImage.png",
  //   },
  // ];

  // const applicants = [
  //   {
  //     name: "홍길동",
  //     profile: "profileImage.png",
  //   },
  //   {
  //     name: "홍길동",
  //     profile: "profileImage.png",
  //   },
  //   {
  //     name: "홍길동",
  //     profile: "profileImage.png",
  //   },
  // ]

  const fetchApplicants = async (code: string) => {
    const token = localStorage.getItem("accessToken");
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_BASE_URL}/apply/select/${code}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      setApplicantsName(response.data.data[0].userList);
      console.log(response.data.data[0].userList);

      if(response.data.data !== null){
        axios.post(`${process.env.REACT_APP_API_BASE_URL}/auth/profile`,
          response.data.data[0].userList,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        ).then((res) => {
          setApplicantsProfile(res.data.data);
          console.log(res);
        })
      }

      
    } catch (error) {
      console.error("Error fetching applicants:", error);
    }
  };

  useEffect(() => {
    if (encrypted !== undefined) {
      const decryptedCode = decrypt(encrypted);
      setTravelCode(decryptedCode);
      fetchApplicants(decryptedCode);
    }
  }, [encrypted]);


  return (
    <TourMembersWrap>
      <Header encrypted={encrypted} />
      <ContentBox>
        <CodeWrap>
          <h2 className="TourMemberTitle">초대코드</h2>
          <span className="InviteCode">{decrypt(encrypted!)}</span>
        </CodeWrap>
        <CurrentMembersWrap>
          {/* <h3>
            현재 참여 인원 <span>({userData.length}명)</span>
          </h3>
          <UserContainer>
            {userData.map((data, index) => (
              <UserListItem
                travelCode={travelCode}
                key={index}
                name={data.name}
                profile={data.profile}
              />
            ))}
          </UserContainer> */}

          <h3>
            신청 인원 <span>({applicantsName.length}명)</span>
          </h3>
          <UserContainer>
            {applicantsName.map((data, index) => (
              <UserListItem
                travelCode={travelCode}
                key={index}
                email={data}
                profile={applicantsProfile[index]}
              />
            ))}
          </UserContainer>
        </CurrentMembersWrap>

        <StyledButtons size="L" name="여행 나가기" $bgColor="red" />
      </ContentBox>
    </TourMembersWrap>
  );
}
