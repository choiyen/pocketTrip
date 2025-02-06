import styled from "styled-components";
import { useState, useRef } from "react";
import axios from "axios";

const initialData = {
    name: "user1",
    email: "user1@example.com",
    password: "12345678",
    phone: "010-1234-1234",
    profileImage: ""
};

export default function EditProfile() {
    const [formData, setFormData] = useState(initialData);
    const [pwInput, setPwInput] = useState(initialData.password);
    const [previewImage, setPreviewImage] = useState(""); // 임시 미리보기 이미지
    const fileInputRef = useRef<HTMLInputElement>(null);

    const token = localStorage.getItem("token");

    // 변경 사항 감지 및 상태 업데이트
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target;
        setFormData(prev => ({ ...prev, [id]: value }));
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPwInput(e.target.value);
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
            setPreviewImage(imageUrl);
        }
    };

    const handleSave = async () => {
        try {
            const updatedData = {
                name: formData.name,
                email: formData.email,
                phone: formData.phone,
                password: pwInput, // 비밀번호 포함
                profileImage: previewImage || formData.profileImage, // 변경된 이미지 적용
            };
    
            const response = await axios.put("http://localhost:8080/auth/edit", updatedData, {
                headers: {
                    "Authorization": `Bearer ${token}`,  // 🔹 Bearer Token 추가
                    "Content-Type": "application/json"
                }
            });
    
            if (response.status === 200) {
                alert("변경 사항이 저장되었습니다!");
                setFormData(updatedData); // 성공하면 로컬 상태도 업데이트
                setPreviewImage(""); // 미리보기 초기화
            } else {
                alert("저장에 실패했습니다.");
            }
        } catch (error) {
            console.error("저장 중 오류 발생:", error);
            alert("저장 중 오류가 발생했습니다.");
        }
    };

    // 변경 사항이 있는지 확인 (비교)
    const isModified = JSON.stringify(formData) !== JSON.stringify({ ...initialData, password: pwInput, profileImage: previewImage || formData.profileImage });

    return (
        <div>
            <ProfileContainer>
                <Profile>
                    <img 
                        src={previewImage || formData.profileImage || "default-profile.png"} 
                        alt="프로필 이미지" 
                        onClick={handleImageClick} 
                    />
                    <input type="file" ref={fileInputRef} style={{ display: "none" }} onChange={handleFileChange} />
                    <span>{formData.name}</span>
                </Profile>
                <ProfileInfo>
                    <Table>
                        <tbody>
                            <tr>
                                <td>이름</td>
                                <td><input type="text" id="name" value={formData.name} style={{color: "grey"}}/></td>
                            </tr>
                            <tr>
                                <td>아이디</td>
                                <td><input type="email" id="email" value={formData.email} style={{color: "grey"}} /></td>
                            </tr>
                            <tr>
                                <td>전화번호</td>
                                <td><input type="tel" id="phone" value={formData.phone} onChange={handleChange} /></td>
                            </tr>
                            <tr>
                                <td>비밀번호</td>
                                <td>
                                    <input type="password" id="password" value={pwInput} onChange={handlePasswordChange} />
                                </td>
                            </tr>
                        </tbody>
                    </Table>
                </ProfileInfo>
                <SaveButton onClick={handleSave} disabled={!isModified}>
                    저장하기
                </SaveButton>
            </ProfileContainer>
        </div>
    );
}

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

const ProfileInfo = styled.div`
    width: 85vw;
    height: 180px;
    background-color: #EAF6FF;
    border-radius: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
`;

const Table = styled.table`
    font-size: 12px;

    & td {
        padding: 0 20px;
    }

    & input {
        margin: 0;
        padding: 0;
        width: min-content;
        background: none;
    }

    & button {
        border: 0;
        background: none;
    }
`;

const SaveButton = styled.button`
    margin-top: 20px;
    padding: 10px 20px;
    border: none;
    background-color: ${props => (props.disabled ? "#ccc" : "#007BFF")};
    color: white;
    border-radius: 5px;
    cursor: ${props => (props.disabled ? "not-allowed" : "pointer")};
    transition: background 0.3s ease;

    &:hover {
        background-color: ${props => (props.disabled ? "#ccc" : "#0056b3")};
    }
`;
