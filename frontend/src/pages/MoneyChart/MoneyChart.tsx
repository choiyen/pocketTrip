import React, { useState } from "react";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Common/Header"; // Header 컴포넌트 임포트

const categoryData = [
  { name: "항공료", id: 52, color: "#D3D3D3" },
  { name: "선물", id: 25, color: "#BEBEBE" },
  { name: "쇼핑", id: 13, color: "#A9A9A9" },
  { name: "투어", id: 10, color: "#949494" },
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
];

const TabMenu = ({
  activeTab,
  setActiveTab,
}: {
  activeTab: number;
  setActiveTab: (index: number) => void;
}) => {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate(-1); // 뒤로가기
  };

  return (
    <>
      <div onClick={handleBack}>
        <Header />
      </div>

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
    </>
  );
};

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

const MoneyChart = () => {
  const [activeTab, setActiveTab] = useState(0);

  const dataSets = [categoryData, paymentData, currencyData];
  const listSets = [categoryList, paymentList, currencyList];

  return (
    <div
      style={{
        maxWidth: "400px",
        margin: "0 auto",
        fontFamily: "Arial, sans-serif",
      }}
    >
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
