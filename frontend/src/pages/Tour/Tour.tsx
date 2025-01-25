import React from "react";
import Header from "../../components/Common/Header";
import TourInfo from "./TourInfo";
import { useLocation } from "react-router-dom";

export default function Tour() {
  const { state } = useLocation();
  const { Tourdata } = state || {};

  return (
    <div>
      <Header $bgColor={"white"} />
      <TourInfo Tourdata={Tourdata} />
    </div>
  );
}
