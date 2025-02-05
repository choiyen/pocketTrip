import React from "react";
import Header from "../../components/Common/Header";
import { useParams } from "react-router-dom";

export default function MoneyChart() {
  const { id } = useParams<{ id: string }>();
  return (
    <div>
      <Header id={id} />
    </div>
  );
}
