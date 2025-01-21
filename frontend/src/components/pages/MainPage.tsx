import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../store";
import { incrementByAmount, minus, plus } from "../../slices/counterSlice";

export default function MainPage() {
  const dispatch: AppDispatch = useDispatch();
  const value = useSelector((state: RootState) => state.counter.value);
  return (
    <div>
      MainPage
      <p>Current Value: {value}</p>
      <button onClick={() => dispatch(plus())}>plus</button>
      <button onClick={() => dispatch(minus())}>minus</button>
      <button onClick={() => dispatch(incrementByAmount(5))}>+5</button>
    </div>
  );
}
