import styled from "styled-components";
import { useState } from "react";

export default function Calculator() {
  const [result, setResult] = useState(0);
  const [input1, setInput1] = useState(0);
  const [input2, setInput2] = useState(0);
  const [operator, setOperator] = useState("");
  const [isDecimal, setIsDecimal] = useState(false);
  const [decimalPosition, setDecimalPosition] = useState(0);

  const numbersElements = ["1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "="];
  const operatorElements = ["AC", "/", "*", "-", "+"];

  const showNumber = (number: string) => {
    if (number === ".") {
      setIsDecimal(true);
      setDecimalPosition(-1);
      return;
    }

    // When not in decimal mode
    if (!isDecimal) {
      const n = Number(number);
      setInput2((prevInput2) => {
        const newInput2 = prevInput2 * 10 + n;
        setResult(newInput2); // Set result immediately
        return newInput2;
      });
    } else {
      const n = Number(number);
      setInput2((prevInput2) => {
        const newInput2 = prevInput2 + n * Math.pow(10, decimalPosition);
        setResult(parseFloat(newInput2.toFixed(10))); // Set result immediately
        return parseFloat(newInput2.toFixed(10));
      });
      setDecimalPosition((prevDecimalPosition) => prevDecimalPosition - 1);
    }
    console.log(input1, input2, result);
  };

  const operate = (element: string) => {
    if (element === "AC") {
      setResult(0);
      setInput1(0);
      setInput2(0);
      setDecimalPosition(0);
      setIsDecimal(false);
      setOperator("");
      return;
    }

    if (element !== "=") {
      setOperator(element);
      setInput1(result);  // Use result as input1
      setInput2(0);
    //   setResult(0);
      setIsDecimal(false);
      setDecimalPosition(0);
    } else {
        let newResult = 0;
        switch (operator) {
            case "+":
                newResult = input1 + input2;
                break;
            case "-":
                newResult = input1 - input2;
                break;
            case "*":
                newResult = input1 * input2;
                break;
            case "/":
                newResult = input1 / input2;
                break;
            default:
                break;
        }

        setResult(parseFloat(newResult.toFixed(10))); // Limit the result to 10 decimal places
        setInput1(newResult); // Update input1 with the result of the operation
        setInput2(0);
    }

    console.log(input1, input2, result);
  };

  return (
    <CalculatorBody>
      <Output1>
        <Text><span>$</span> {result}</Text>
        <CurrencyInfo>$1 = ₩1,455.30</CurrencyInfo>
      </Output1>
      <Output2 />
      <InputBody>
        <Numbers>
          {numbersElements.map((element) => {
            return (
              <button
                key={element}
                onClick={() => {
                  if (element === "=") {
                    operate(element);
                  } else {
                    showNumber(element);
                  }
                }}
              >
                {element}
              </button>
            );
          })}
        </Numbers>
        <Operators>
          {operatorElements.map((element) => {
            return (
              <button key={element} onClick={() => operate(element)}>
                {element}
              </button>
            );
          })}
        </Operators>
      </InputBody>
    </CalculatorBody>
  );
}

const CalculatorBody = styled.div`
  width: 100%;
  height: 80%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  margin: 20px 0;
`;

const CurrencyInfo = styled.span`
  font-size: 12px;
  position: relative;
  color: black;
  bottom: 45px;
  right: 200px;
  z-index: 10;
  width: 100%;
`;

const Text = styled.div`
  width: 100%;
  font-size: 36px;
  margin: 40px;

  & span {
    font-weight: 500;
  }
`;

const Output1 = styled.div`
  background-color: #f4f4f4;
  width: 85vw;
  height: 120px;
  border-radius: 15px;
  margin: 8px;
  display: flex;
  align-items: center;
`;

const Output2 = styled.div`
  background-color: #f4f4f4;
  width: 85vw;
  height: 120px;
  border-radius: 15px;
  margin: 8px;
`;

const InputBody = styled.div`
  width: 85vw;
  display: flex;
`;

const Numbers = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: center;

  width: 75%;
  height: 360px;
  margin: 10px 0;

  & button {
    font-size: 36px;
    width: 70px;
    height: 70px;
    background: rgb(0, 0, 0, 0);
    border: 0;
  }

  & : last-child {
    color: #0077cc;
    font-size: 48px;
  }
`;

const Operators = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: center;

  width: 25%;
  height: 360px;
  margin: 10px 0;

  & button {
    font-size: 48px;
    width: 60px;
    height: 60px;
    background: rgb(0, 0, 0, 0);
    border: 0;
    color: #b1b1b1;
  }

  & : first-child {
    font-weight: 700;
    font-size: 24px;
  }
`;

