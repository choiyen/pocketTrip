import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import Header from "../../components/Common/Header";
import { countryCurrencies } from "../../pages/Data/countryMoney"; // 데이터 불러오기
import { countryNamesInKorean } from "../../pages/Data/countryNames"; // 한글 국가명

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  width: 100%;
  max-width: 375px;
  margin: 0 auto;
  box-sizing: border-box;
  margin-top: -10%;
`;

// const Header = styled.div`
//   display: flex;
//   align-items: center;
//   justify-content: space-between;
//   width: 100%;
//   padding: 10px 20px;
//   font-size: 16px;
//   font-weight: bold;

//   svg {
//     margin-right: 10px;
//     cursor: pointer;
//     padding: 10px;
//   }

//   span {
//     text-align: center;
//     flex: 1;
//   }
// `;

const CurrencyButton = styled.button`
  background-color: #d9d9d9;
  color: black;
  border: none;
  border-radius: 20px;
  padding: 5px 15px;
  font-size: 17px;
  font-weight: bold;
  margin-top: 20%;
  cursor: pointer;
  height: 40px;
`;

const CurrencyDropdown = styled.ul`
  margin-top: 110px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  width: auto;
  max-height: 200px;
  overflow-y: auto;
  z-index: 10;
  position: absolute;
  padding: 10px 0;
  text-align: center;
`;

const SelectUserDropDown = styled(CurrencyDropdown)`
  margin-top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
`;

const CurrencyItem = styled.li`
  padding: 10px 20px;
  font-size: 16px;
  color: #333;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #f0f0f0;
  }
`;

const Display = styled.div<{ $hasAmount: boolean }>`
  font-size: 24px;
  font-weight: bold;
  color: ${(props) => (props.$hasAmount ? "#333" : "#b0b0b0")};
  margin: 20px 0;
  text-align: center;
  min-height: 30px;
  margin-top: 15%;
`;

const Keypad = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  width: 90%;
  margin: 20px 0;
  margin-top: 15%;
`;

const Key = styled.button`
  background-color: #d9d9d9;
  font-size: 24px;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  height: 60px;
  cursor: pointer;

  &:hover {
    background-color: #e0e0e0;
  }
`;

const DeleteKey = styled(Key)`
  svg {
    width: 24px;
    height: 24px;
  }
`;

const Footer = styled.div`
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-top: 20px;
  gap: 10px;
`;

const ActionButton = styled.button<{ $bgColor: string }>`
  background-color: ${(props) => props.$bgColor};
  color: white;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 20px;
  padding: 10px 30px;
  cursor: pointer;
  x &:hover {
    opacity: 0.9;
  }
`;

export default function AccountBook() {
  const [amount, setAmount] = useState(""); // 입력한 금액
  const [currency, setCurrency] = useState("KRW"); // 선택된 통화 코드
  const [currencySymbol, setCurrencySymbol] = useState("₩"); // 통화 기호
  const [currencyList, setCurrencyList] = useState<string[]>(["KRW", "USD"]); // 통화 리스트
  const [isCurrencyListVisible, setIsCurrencyListVisible] = useState(false); // 통화 선택 드롭다운 표시 여부
  const [selectedUser, setSelectedUser] = useState<{
    name: string;
    email: string;
  } | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const { encrypted } = useParams<{ encrypted: string }>(); // URL에서 id(나라) 가져오기
  const navigate = useNavigate(); // 페이지 이동 함수
  const location = useLocation(); // state로 전달된 location 정보
  const country = location.state?.location; // state에서 location을 가져옴

  const toggleDropDown = () => {
    setIsOpen((prev) => !prev);
  };

  const handleSelected = (option: { name: string; email: string }) => {
    setSelectedUser(option);
    setIsOpen(false);
  };

  const members = [
    { name: "황종현", email: "test@" },
    { name: "김철수", email: "email2@naver.com" },
    { name: "김영희", email: "email3@naver.com" },
    { name: "홍길동", email: "email4@naver.com" },
  ];

  useEffect(() => {
    if (country) {
      // 1. 한글 국가명으로 영어 국가명 찾기
      const englishCountryName = Object.keys(countryNamesInKorean).find(
        (key) => countryNamesInKorean[key] === country
      );

      if (englishCountryName) {
        // 2. 영어 국가명으로 통화 정보 가져오기
        const countryCurrency = countryCurrencies[englishCountryName];

        if (countryCurrency) {
          const [currencyCode, symbol] = countryCurrency.split(", ");
          setCurrency(currencyCode); // 통화 코드 설정
          setCurrencySymbol(symbol); // 통화 기호 설정
          //통화 리스트에 국가 통화 코드 추가
          setCurrencyList((prevList) => {
            if (!prevList.includes(currencyCode)) {
              return [...prevList, currencyCode];
            }
            return prevList;
          });
        } else {
          console.log("해당 국가의 통화 정보가 없습니다.");
        }
      } else {
        console.log("해당 국가의 영어 이름을 찾을 수 없습니다.");
      }
    } else {
      console.log("country 값이 전달되지 않았습니다.");
    }
  }, [country]);

  const handleKeyPress = (key: string) => {
    if (key === "delete") {
      setAmount((prev) => prev.slice(0, -1));
    } else if (key === ".") {
      if (!amount.includes(".")) setAmount((prev) => prev + key);
    } else {
      setAmount((prev) => prev + key);
    }
  };

  const toggleCurrencyList = () => {
    setIsCurrencyListVisible((prev) => !prev); // 통화 목록 보이기/숨기기
  };

  useEffect(() => {
    if (currency === "KRW") {
      setCurrencySymbol("₩");
    } else if (currency) {
      // currency가 설정된 경우 불러온 통화 심볼 사용
      const selectedSymbol =
        Object.entries(countryCurrencies)
          .find(([, value]) => value.startsWith(currency))?.[1]
          .split(", ")[1] || "$"; // 기본값은 $로 설정
      setCurrencySymbol(selectedSymbol);
    }
  }, [currency]);

  const handleCurrencySelect = (selectedCurrency: string) => {
    setCurrency(selectedCurrency); // 선택된 통화 코드 업데이트

    // 선택된 통화에 따라 심볼 설정
    const selectedSymbol =
      Object.entries(countryCurrencies)
        .find(([, value]) => value.startsWith(selectedCurrency))?.[1]
        .split(", ")[1] || "₩"; // 기본값 KRW(₩)

    setCurrencySymbol(selectedSymbol);
    setIsCurrencyListVisible(false); // 드롭다운 닫기
  };

  const handleNavigation = (paymentType: string) => {
    if (!amount || !selectedUser) {
      alert("사용 유저와 금액을 입력해주세요.");
      return;
    }
    const today = new Date();
    const options: Intl.DateTimeFormatOptions = {
      month: "2-digit",
      day: "2-digit",
      weekday: "short",
    };
    const formattedDate = today.toLocaleDateString("ko-KR", options);

    navigate(`/Tour/${encrypted}/categories`, {
      state: {
        amount,
        currency,
        paymentType,
        date: formattedDate,
        selectedUser,
      }, // 날짜 추가
    });
  };

  return (
    <>
      <Header encrypted={encrypted} />
      <Container>
        <CurrencyButton onClick={toggleCurrencyList}>
          {currency} ▼
        </CurrencyButton>

        {isCurrencyListVisible && (
          <CurrencyDropdown>
            {currencyList.map((cur, index) => (
              <CurrencyItem
                key={index}
                onClick={() => handleCurrencySelect(cur)}
              >
                {cur}
              </CurrencyItem>
            ))}
          </CurrencyDropdown>
        )}

        <Display $hasAmount={!!amount}>
          {amount
            ? `${parseFloat(amount).toLocaleString()} ${currencySymbol}`
            : "얼마를 사용하셨나요"}
        </Display>

        <div>
          <CurrencyButton onClick={toggleDropDown}>
            {selectedUser?.name ? selectedUser.name : "유저를 선택해주세요"} ▼
          </CurrencyButton>
          {isOpen && (
            <SelectUserDropDown>
              {members.map((option) => (
                <CurrencyItem
                  key={option.email}
                  onClick={() => handleSelected(option)}
                >
                  {option.name}
                </CurrencyItem>
              ))}
            </SelectUserDropDown>
          )}
        </div>

        <Keypad>
          {["1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0"].map(
            (key) => (
              <Key key={key} onClick={() => handleKeyPress(key)}>
                {key}
              </Key>
            )
          )}
          <DeleteKey onClick={() => handleKeyPress("delete")}>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              className="bi bi-backspace"
              viewBox="0 0 16 16"
            >
              <path d="M5.83 5.146a.5.5 0 0 0 0 .708L7.975 8l-2.147 2.146a.5.5 0 0 0 .707.708l2.147-2.147 2.146 2.147a.5.5 0 0 0 .707-.708L9.39 8l2.146-2.146a.5.5 0 0 0-.707-.708L8.683 7.293 6.536 5.146a.5.5 0 0 0-.707 0z" />
              <path d="M13.683 1a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2h-7.08a2 2 0 0 1-1.519-.698L.241 8.65a1 1 0 0 1 0-1.302L5.084 1.7A2 2 0 0 1 6.603 1zm-7.08 1a1 1 0 0 0-.76.35L1 8l4.844 5.65a1 1 0 0 0 .759.35h7.08a1 1 0 0 0 1-1V3a1 1 0 0 0-1-1z" />
            </svg>
          </DeleteKey>
        </Keypad>

        <Footer>
          <ActionButton
            $bgColor="#4CAF50"
            onClick={() => handleNavigation("cash")}
          >
            현금
          </ActionButton>
          <ActionButton
            $bgColor="#007BFF"
            onClick={() => handleNavigation("card")}
          >
            카드
          </ActionButton>
        </Footer>
      </Container>
    </>
  );
}
