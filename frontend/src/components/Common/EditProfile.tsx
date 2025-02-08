import styled from "styled-components";
import { useState, useRef } from "react";
import axios from "axios";
import Button from "./Button";
import { useDispatch } from "react-redux";
import { AppDispatch } from "../../store";
import {
  ChangeModalState,
  ChangeMovingModal,
} from "../../slices/ModalControlSlice";
import Alert from "./Alert";

const initialData = {
  name: "user1",
  email: "user1@example.com",
  password: "12345678",
  phone: "01012341234",
  profileImage: "/japan.jpg",
};

export default function EditProfile() {
  //   const [formData, setFormData] = useState(initialData);
  const [username, setUsername] = useState(initialData.name);
  const [userId, setUserId] = useState(initialData.email);
  const [userPhoneNumber, setUserPhoneNumber] = useState(initialData.phone);
  const [userPassword, setuserPassword] = useState("");
  const [previewImage, setPreviewImage] = useState(initialData.profileImage); // 임시 미리보기 이미지
  const fileInputRef = useRef<HTMLInputElement>(null);
  const dispatch: AppDispatch = useDispatch();

  const token = localStorage.getItem("token");

  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
  };
  const handleIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUserId(e.target.value);
  };
  const handlePhoneNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUserPhoneNumber(e.target.value);
  };
  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setuserPassword(e.target.value);
  };

  const handleImageClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setPreviewImage((prevImage) => {
        if (prevImage) {
          URL.revokeObjectURL(prevImage);
        }
        return imageUrl;
      });
    }
  };

  const ChangeState = () => {
    // // 모달창이 렌더링 되어 있는 상태면 내리는 동작 이후 제거
    dispatch(ChangeMovingModal());
    setTimeout(() => {
      dispatch(ChangeModalState());
    }, 500);
  };

  // 변경 사항 감지 및 상태 업데이트
  //   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  //     const { id, value } = e.target;
  //     setFormData((prev) => ({ ...prev, [id]: value }));
  //   };

  const handleSave = async () => {
    try {
      const updatedData = {
        name: username,
        email: userId,
        phone: userPhoneNumber,
        password: userPassword, // 비밀번호 포함
        profileImage: previewImage, // 변경된 이미지 적용
      };
      console.log(updatedData);
      const response = await axios.put(
        "http://localhost:8080/auth/edit",
        updatedData,
        {
          headers: {
            Authorization: `Bearer ${token}`, // 🔹 Bearer Token 추가
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 200) {
        alert("변경 사항이 저장되었습니다!");
        // setFormData(updatedData); // 성공하면 로컬 상태도 업데이트
        // setPreviewImage(""); // 미리보기 초기화
      } else {
        alert("저장에 실패했습니다.");
      }
    } catch (error) {
      console.error("저장 중 오류 발생:", error);
      alert("저장 중 오류가 발생했습니다.");
    }
  };

  // 변경 사항이 있는지 확인 (비교)
  //   const isModified =
  //     JSON.stringify(formData) !==
  //     JSON.stringify({
  //       ...initialData,
  //       password: userPassword,
  //       profileImage: previewImage || formData.profileImage,
  //     });

  return (
    <div>
      <ProfileContainer>
        <Profile>
          <img
            src={previewImage === "" ? "/ProfileImage.png" : previewImage}
            alt="프로필 이미지"
            onClick={handleImageClick}
          />
          <input
            type="file"
            ref={fileInputRef}
            style={{ display: "none" }}
            onChange={handleFileChange}
          />

          <ChangeButton size="S" name="교체하기" onClick={handleImageClick} />
        </Profile>

        <ProfileInfo>
          <Label htmlFor="username">이름</Label>
          <InputText
            id="username"
            type="text"
            value={username}
            onChange={handleNameChange}
          />
          <Label htmlFor="userid">아이디(e-mail)</Label>
          <InputText
            id="userid"
            type="text"
            value={userId}
            onChange={handleIdChange}
          />
          <Label htmlFor="userPassword">비밀번호</Label>
          <InputText
            id="userPassword"
            type="password"
            placeholder="새 비밀번호 입력"
            value={userPassword}
            onChange={handlePasswordChange}
          />
          <Label htmlFor="userPhoneNumber">전화번호(- 없이 입력)</Label>
          <InputText
            id="userPhoneNumber"
            type="number"
            value={userPhoneNumber}
            onChange={handlePhoneNumberChange}
          />
          {/* <Table>
            <tbody>
              <tr>
                <td>이름</td>
                <td>
                  <input
                    type="text"
                    id="name"
                    value={formData.name}
                    style={{ color: "grey" }}
                  />
                </td>
              </tr>
              <tr>
                <td>아이디</td>
                <td>
                  <input
                    type="email"
                    id="email"
                    value={formData.email}
                    style={{ color: "grey" }}
                  />
                </td>
              </tr>
              <tr>
                <td>전화번호</td>
                <td>
                  <input
                    type="tel"
                    id="phone"
                    value={formData.phone}
                    onChange={handleChange}
                  />
                </td>
              </tr>
              <tr>
                <td>비밀번호</td>
                <td>
                  <input
                    type="password"
                    id="password"
                    value={pwInput}
                    onChange={handlePasswordChange}
                  />
                </td>
              </tr>
            </tbody>
          </Table> */}
        </ProfileInfo>
        {/* <SaveButton onClick={handleSave} disabled={!isModified}>
          저장하기
        </SaveButton> */}
        <BoxWrap>
          <CancleButton size="S" name="취소" onClick={() => ChangeState()} />
          <Button size="S" name="저장" onClick={handleSave} />
        </BoxWrap>
      </ProfileContainer>
    </div>
  );
}

const ProfileInfo = styled.section`
  width: 85vw;
  max-width: 768px;
  padding: 20px;
  box-sizing: border-box;
`;
const Label = styled.label`
  font-size: 14px;
  font-weight: 500;
  font-family: inherit;
  color: #6f6f6f;
  margin-bottom: 5px;
  align-self: flex-start;
  display: block;
`;

const InputText = styled.input`
  background-color: transparent;
  border-bottom: 1px solid grey;
  border-radius: 0;
  width: 100%;
  max-width: 768px;
`;
const ChangeButton = styled(Button)`
  margin: 10px auto 20px;
  background-color: transparent;
  color: #0077cc;
  font-weight: 700;
  font-family: inherit;
  box-shadow: 0px 1px 4px rgba(0, 0, 0, 0.2);
`;
const BoxWrap = styled.div`
  display: flex;
  gap: 10px;
  flex-direction: row;
  /* @media (min-width: 768px) {
    flex-direction: row;
  } */
`;
const CancleButton = styled(Button)`
  background-color: #e8e8e8;
  color: black;
`;

const ProfileContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  position: relative;

  & .profileButton {
    position: absolute;
    top: 0;
    right: 0;
    margin: 10px;
  }
`;

const Profile = styled.div`
  display: flex;
  flex-direction: column;
  // margin: 20px 0 0 0;

  & img {
    border-radius: 100%;
    width: 150px;
    height: 150px;
    cursor: pointer;
    object-fit: cover;
  }

  & span {
    text-align: center;
    font-size: 20px;
    margin: 20px;
  }
`;

// const Table = styled.table`
//   font-size: 12px;

//   & td {
//     padding: 0 20px;
//   }

//   & input {
//     margin: 0;
//     padding: 0;
//     width: min-content;
//     background: none;
//   }

//   & button {
//     border: 0;
//     background: none;
//   }
// `;

// const SaveButton = styled.button`
//   margin-top: 20px;
//   padding: 10px 20px;
//   border: none;
//   background-color: ${(props) => (props.disabled ? "#ccc" : "#007BFF")};
//   color: white;
//   border-radius: 5px;
//   cursor: ${(props) => (props.disabled ? "not-allowed" : "pointer")};
//   transition: background 0.3s ease;

//   &:hover {
//     background-color: ${(props) => (props.disabled ? "#ccc" : "#0056b3")};
//   }
// `;
