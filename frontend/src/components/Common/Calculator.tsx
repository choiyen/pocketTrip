import styled from "styled-components";
import Button from "./Button";


export default function Calculator() {

    const numbersElements = ["1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "="];
    const operatorElements = ["AC", "/", "*", "-", "+"];

    let input1 = 0;
    let input2 = 0;
    let result = 0;
    let i = 0;
    let isDecimal = false;
    let operator = "";

    function showNumber(number: string){

        if(number == "."){
            isDecimal = false;
            i = -1;
            return;
        }
        
        if(!isDecimal){
            const n = Number(number);
            input2 = input2*10 + n;
            i++;
            result = input2;
        } 
        else {
            const n = Number(number);
            input2 = result + n*(10**i);
            i--;
            result = input2;
        }

        console.log(input1, input2, result);
    }

    function operate(element: string) {
        if(element == "AC"){
            result = 0;
            input1 = 0;
            input2 = 0;
            i = 0;
            isDecimal = false;
            operator = "";
        }

        if(element != "="){
            operator = element;
            input1 = result;
            input2 = 0;
        } else {
            switch (operator) {
                case "+":
                    result = input1 + input2;
                    break;
                case "-":
                    result = input1 - input2;
                    break;
                case "*":
                    result = input1 * input2;
                    break;
                case "/":
                    result = input1 / input2;
                    break;
            }

            input1 = result;
            input2 = 0;
        }

        console.log(input1, input2, result);
    }

    return (
        <CalculatorBody>
            <Output1 />
            <Output2 />
            <InputBody>
                <Numbers>
                    {numbersElements.map((element) => {
                        return <button onClick={() => {
                            if(element == "="){
                                operate(element);
                            }
                            else {
                                showNumber(element);
                            }
                        }}>{element}</button>
                    })}
                </Numbers>
                <Operators>
                    {operatorElements.map((element) => {
                        return <button onClick={() => {operate(element)}}>{element}</button>
                    })}
                </Operators>
            </InputBody>
        </CalculatorBody>
    )
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

const Output1 = styled.div`
    background-color: #F4F4F4;
    width: 85vw;
    height: 120px;
    border-radius: 15px;
    margin: 8px;
`;

const Output2 = styled.div`
    background-color: #F4F4F4;
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

    & button{
        font-size: 36px;
        width: 70px;
        height: 70px;
        background: rgb(0, 0, 0, 0);
        border: 0;
    }

    & : last-child {
        color: #0077CC;
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

    & button{
        font-size: 48px;
        width: 60px;
        height: 60px;
        background: rgb(0, 0, 0, 0);
        border: 0;
        color: #B1B1B1;
    }

    & : first-child {
        font-weight: 700;
        font-size: 24px;
    }
`;