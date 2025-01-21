import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../store";
import { incrementByAmount, minus, plus } from "../../slices/counterSlice";
import Header from "../templates/Header";
import { ChangeCurrentPage } from "../../slices/currentPageSlice";

export default function MainPage() {
  // 글로벌 상태값을 변경하려면 필요한 함수 usedispatch();
  const dispatch: AppDispatch = useDispatch();

  // 원하는 글로벌 상태값을 불러오기위해 필요한 함수 useselector();
  const value = useSelector((state: RootState) => state.counter.value);
  useEffect(() => {
    dispatch(ChangeCurrentPage("home"));
  }, []);

  return (
    <div>
      <Header />
      MainPage
      <p>Current Value: {value}</p>
      <button onClick={() => dispatch(plus())}>plus</button>
      <button onClick={() => dispatch(minus())}>minus</button>
      <button onClick={() => dispatch(incrementByAmount(5))}>+5</button>
    </div>
  );
}
