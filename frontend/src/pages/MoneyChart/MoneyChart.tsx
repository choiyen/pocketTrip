import React, { useState, useEffect } from "react";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import { useParams, useLocation } from "react-router-dom";
import Header from "../../components/Common/Header"; // Header 컴포넌트 임포트
import { MoneyLogProps } from "../Tour/Tour";

// 카테고리, 지출 수단, 통화 데이터
const categoryData = [
  { id: 1, name: "숙소", icon: "🏠", color: "#A5D8FF" },
  { id: 2, name: "교통", icon: "🚌", color: "#FFD3B6" },
  { id: 3, name: "식사", icon: "🍽️", color: "#FFEE93" },
  { id: 4, name: "쇼핑", icon: "🛍️", color: "#FFB6C1" },
  { id: 5, name: "선물", icon: "🎁", color: "#D9C2E9" },
  { id: 6, name: "마트", icon: "🛒", color: "#C4E9C5" },
  { id: 7, name: "투어", icon: "🎡", color: "#B9EBC2" },
  { id: 8, name: "카페", icon: "☕", color: "#D9B99B" },
  { id: 9, name: "항공", icon: "✈️", color: "#A8CFF0" },
  { id: 10, name: "통신", icon: "📱", color: "#D1E4F2" },
  { id: 11, name: "의료", icon: "🩺", color: "#AEE6E6" },
  { id: 12, name: "주류", icon: "🍻", color: "#F4A8A8" },
  { id: 13, name: "환전", icon: "💱", color: "#C8E5B5" },
  { id: 14, name: "미용", icon: "💇🏻", color: "#EAB2E8" },
  { id: 15, name: "관광", icon: "🎠", color: "#FFEAB6" },
  { id: 16, name: "팁", icon: "💸", color: "#C8E6D8" },
];
const paymentData = [
  { name: "현금", id: 60, color: "#D3D3D3" },
  { name: "카드", id: 40, color: "#BEBEBE" },
];
const currencyData = [
  { name: "VND", id: 90, color: "#D3D3D3" },
  { name: "KRW", id: 10, color: "#BEBEBE" },
];

const categoryList = [
  { name: "숙소", percentage: 14.2, amount: "68,400 KRW", icon: "🏨" },
  { name: "식사", percentage: 9.7, amount: "46,740 KRW", icon: "🍴" },
  { name: "투어", percentage: 8.3, amount: "39,900 KRW", icon: "🎡" },
  { name: "마트", percentage: 7.2, amount: "34,507.8 KRW", icon: "🛒" },
  { name: "교통", percentage: 7.1, amount: "34,200 KRW", icon: "🚗" },
];
const paymentList = [
  { name: "현금", percentage: 77.1, amount: "370,500 KRW", icon: "💵" },
  { name: "카드", percentage: 22.9, amount: "109,747.8 KRW", icon: "💳" },
];
const currencyList = [
  {
    name: "VND",
    percentage: 95.2,
    amount: "10,435,480 VND",
    converted: "594,822.36 KRW",
    icon: "🇻🇳",
  },
  {
    name: "KRW",
    percentage: 4.8,
    amount: "30,000 KRW",
    converted: "30,000 KRW",
    icon: "🇰🇷",
  },
  {
    name: "USD",
    percentage: 3,
    amount: "30 USD",
    converted: "43,379.68 KRW",
    icon: "🇺🇸",
  },
];

// TabMenu 컴포넌트
const TabMenu = ({
  activeTab,
  setActiveTab,
}: {
  activeTab: number;
  setActiveTab: (index: number) => void;
}) => {
  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        marginBottom: "20px",
      }}
    >
      {["카테고리 별", "지출수단 별", "통화 별"].map((tab, index) => (
        <button
          key={index}
          onClick={() => setActiveTab(index)}
          style={{
            flex: 1,
            padding: "10px 20px",
            margin: "0 5px",
            border: "1px solid #ccc",
            borderRadius: "5px",
            backgroundColor: activeTab === index ? "#f5f5f5" : "#fff",
            fontWeight: activeTab === index ? "bold" : "normal",
          }}
        >
          {tab}
        </button>
      ))}
    </div>
  );
};

// PieChart 컴포넌트
const PieChartComponent = ({
  data,
}: {
  data: { name: string; id: number; color: string }[];
}) => {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <PieChart>
        <Pie
          data={data}
          dataKey="id"
          nameKey="name"
          innerRadius="40%"
          outerRadius="70%"
          paddingAngle={5}
        >
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={entry.color} />
          ))}
        </Pie>
        <Tooltip />
      </PieChart>
    </ResponsiveContainer>
  );
};

// List 컴포넌트
const List = ({ list }: { list: any[] }) => {
  return (
    <div>
      {list.map((item, index) => (
        <div
          key={index}
          style={{
            display: "flex",
            alignItems: "center",
            marginBottom: "15px",
            padding: "10px",
            borderBottom: "1px solid #e0e0e0",
          }}
        >
          <span style={{ fontSize: "24px", marginRight: "10px" }}>
            {item.icon}
          </span>
          <div style={{ flex: 1 }}>
            <div style={{ fontSize: "14px", color: "#666" }}>{item.name}</div>
            <div style={{ fontSize: "12px", color: "#999" }}>
              {item.percentage}%
            </div>
          </div>
          <div style={{ fontWeight: "bold" }}>{item.amount}</div>
          {item.converted && (
            <div style={{ fontSize: "12px", color: "#999" }}>
              ({item.converted})
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

// MoneyChart 컴포넌트
const MoneyChart = () => {
  const [activeTab, setActiveTab] = useState(0);
  const { id } = useParams();

  const dataSets = [categoryData, paymentData, currencyData];
  const listSets = [categoryList, paymentList, currencyList];

  useEffect(() => {
    console.log(`Tour ID: ${id}`);
  }, [id]);

  const location = useLocation();
  const dummyLogs: MoneyLogProps[] = [
    {
      LogState: "minus",
      title: "커피",
      detail: "스타벅스 아메리카노",
      profile: "/ProfileImage.png",
      type: "카드",
      money: "5,000",
    },
    {
      LogState: "minus",
      title: "환전",
      detail: "100달러 환전",
      profile: "/ProfileImage.png",
      type: "현금",
      money: "130,000",
    },
  ];

  const logs = location.state?.logs ?? dummyLogs; // ✅ 기본값 설정

  console.log("받은 logs 데이터:", logs);

  return (
    <div
      style={{
        maxWidth: "400px",
        margin: "0 auto",
        fontFamily: "Arial, sans-serif",
      }}
    >
      <Header $bgColor="transparent" id={id} />
      <TabMenu activeTab={activeTab} setActiveTab={setActiveTab} />
      <PieChartComponent data={dataSets[activeTab]} />
      <List list={listSets[activeTab]} />
      {activeTab === 2 && (
        <div
          style={{ textAlign: "center", marginTop: "20px", fontWeight: "bold" }}
        >
          총 624,822.36 원
        </div>
      )}
    </div>
  );
};

export default MoneyChart;
