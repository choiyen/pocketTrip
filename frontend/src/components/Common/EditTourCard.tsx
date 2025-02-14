import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { FcAddImage } from "react-icons/fc";
import SelectLocation from "../ui/SelectLocation";
import axios from "axios";
import { countryNamesInKorean } from "../../pages/Data/countryNames";
/*
배경이미지 > 파일 인풋
투어 장소 > 드롭다운 
방 이름 > 문자열 입력

소비 방식 > 0일경우 누적 > 숫자 입력시 한도 설정
시작일 > 달력
종료일  > 달력


처음 열면 기본값이 다 들어있다. 

소비 방식의 경우
0일때 한도를 입력으로 바꿀 시 > 누적되던 금액을 한도에서 뺀 값으로 표시한다. 
한도 입력에서 누적으로 바꿀 시 > 누적되던 금액으로 교체
*/
const Container = styled.div`
  padding: 10px;
  .hidden {
    display: none;
    border-radius: 10px;
  }
  .selectFile {
    border: 2px solid #e2e2e2;
    display: block;
    text-align: center;
    padding: 20px 0;
    margin-bottom: 50px;

    svg {
      display: block;
      margin: 0 auto 20px;
    }
  }
`;

const Label = styled.label`
  font-size: 14px;
  font-weight: 500;
  font-family: inherit;
  color: #6f6f6f;
  margin-bottom: 5px;
  margin-top: 30px;
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

const StyledSelection = styled(SelectLocation)`
  margin: 0 auto;
`;
export default function EditTourCard() {
  const [fileName, setFileName] = useState<string | null>(null);
  const [tourName, setTourName] = useState<string>("");
  const [countries, setCountries] = useState<string[]>([]); // 나라 목록
  const [search, setSearch] = useState<string>(""); // 검색어
  const [location, setSelectedCountry] = useState<string>(""); // 선택된 나라
  const [isEditing, setIsEditing] = useState<boolean>(false); // 드롭다운 활성화 여부

  // API 호출로 나라 목록 불러오기
  useEffect(() => {
    const fetchCountries = async () => {
      try {
        const response = await axios.get("https://restcountries.com/v3.1/all");
        const countryNames = response.data.map((country: any) => {
          const englishName: string = country.name.common;
          return countryNamesInKorean[englishName] || englishName;
        });
        setCountries(countryNames); // 나라 목록 상태 업데이트
      } catch (error) {
        console.error("API 호출 중 오류 발생:", error);
      }
    };

    fetchCountries();
  }, []);

  // 검색어로 필터링된 나라 목록
  const filteredCountries = countries.filter((country) =>
    country.toLowerCase().includes(search.toLowerCase())
  );

  // 한글 순으로 정렬
  const sortedCountries = filteredCountries.sort((a, b) =>
    a.localeCompare(b, "ko")
  );

  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTourName(e.target.value);
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      setFileName(event.target.files[0].name);
    }
  };

  return (
    <Container>
      <input
        type="file"
        id="file-upload"
        className="hidden"
        onChange={handleFileChange}
      />
      <label htmlFor="file-upload" className="selectFile">
        <div>
          <FcAddImage size={"100px"} />
          {fileName ? fileName : "파일을 선택하세요"}
        </div>
      </label>

      <StyledSelection
        setIsEditing={setIsEditing}
        setSearch={setSearch}
        setSelectedCountry={setSelectedCountry}
        isEditing={isEditing}
        search={search}
        filteredCountries={filteredCountries}
        location={location}
      />

      <Label htmlFor="username">여행 이름</Label>
      <InputText
        id="TourName"
        type="text"
        value={tourName}
        onChange={handleNameChange}
        placeholder="여행 이름을 입력하세요"
      />
    </Container>
  );
}
