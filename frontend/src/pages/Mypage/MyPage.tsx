import React, { useEffect } from "react";
import Header from "../../components/Common/Header";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../store";
import { ChangeCurrentPage } from "../../slices/currentPageSlice";

export default function MyPage() {
  const dispatch: AppDispatch = useDispatch();
  useEffect(() => {
    dispatch(ChangeCurrentPage("mypage"));
  }, []);
  return (
    <div>
      <Header />
    </div>
  );
}
