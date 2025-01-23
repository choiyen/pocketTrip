import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Button from "../../components/Common/Button";
import "./Where2.css";

// 영어에서 한글로 나라 이름을 매핑하는 객체
const countryNamesInKorean: { [key: string]: string } = {
  Germany: "독일",
  France: "프랑스",
  Italy: "이탈리아",
  Japan: "일본",
  USA: "미국",
  Spain: "스페인",
  Canada: "캐나다",
  Australia: "호주",
  India: "인도",
  China: "중국",
  "South Korea": "한국",
  "United Kingdom": "영국",
};

export default function Where2() {
  const navigate = useNavigate();
  const [countries, setCountries] = useState<string[]>([]);
  const [search, setSearch] = useState("");
  const [selectedCountry, setSelectedCountry] = useState("");
  const [isEditing, setIsEditing] = useState(false);

  const goToWhere1 = () => {
    navigate("/where1");
  };
  const goToWhere3 = () => {
    navigate("/where3");
  };

  // API 호출로 나라 목록 불러오기
  useEffect(() => {
    const fetchCountries = async () => {
      try {
        const response = await axios.get("https://restcountries.com/v3.1/all");
        // 나라 이름을 영어에서 한글로 변환
        const countryNames = response.data.map((country: any) => {
          const englishName = country.name.common;
          return countryNamesInKorean[englishName] || englishName; // 한글 이름이 있으면 그것을, 없으면 영어 이름 사용
        });
        setCountries(countryNames); // 나라 목록을 상태에 저장
      } catch (error) {
        console.error("API 호출 중 오류 발생:", error);
      }
    };

    fetchCountries();
  }, []); // 컴포넌트가 마운트될 때 한 번만 호출

  // 검색어로 필터링된 나라 목록
  const filteredCountries = countries.filter((country) =>
    country.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="where-container2">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="16"
        height="16"
        fill="currentColor"
        className="left"
        viewBox="0 0 16 16"
        onClick={goToWhere1}
      >
        <path
          fill-rule="evenodd"
          d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0"
        />
      </svg>
      <div className="where-title2">어느 나라로 떠날 계획인가요?</div>

      {/* 나라 선택 텍스트와 화살표 */}
      <div className="where-option2">
        <div
          className="country-selector"
          onClick={() => setIsEditing(true)} // 클릭 시 입력 모드로 전환
        >
          {isEditing || selectedCountry ? (
            <input
              type="text"
              className="search-input"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="나라를 검색하세요"
              autoFocus
            />
          ) : (
            "나라 선택"
          )}
        </div>

        {/* 화살표 */}
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          fill="currentColor"
          viewBox="0 0 16 16"
          className="dropdown-arrow"
        >
          <path
            fillRule="evenodd"
            d="M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708"
          />
        </svg>
      </div>

      {/* 검색 결과를 드롭다운 메뉴로 표시 */}
      {isEditing && filteredCountries.length > 0 && (
        <div className="dropdown-menu">
          {filteredCountries.map((country, index) => (
            <div
              key={index}
              className="dropdown-option"
              onClick={() => {
                setSelectedCountry(country); // 나라 선택 시 상태 변경
                setSearch(""); // 검색창 초기화
                setIsEditing(false); // 입력 모드 종료
              }}
            >
              {country}
            </div>
          ))}
          {filteredCountries.length === 0 && (
            <div className="no-results">검색 결과가 없습니다.</div>
          )}
        </div>
      )}

      <div className="button-container">
        <Button size="S" name="확인" $bgColor="blue" onClick={goToWhere3} />
      </div>
    </div>
  );
}
