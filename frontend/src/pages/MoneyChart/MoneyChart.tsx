import React, { useState } from "react";
import { PieChart, Pie, Cell } from "recharts";
import { Card, CardContent } from "../../components/ui/card";
import { Button } from "../../components/ui/button";
import Header from "../../components/Common/Header";

type ChartData = {
  name: string;
  value: number;
  color: string;
};

const dataCategories: ChartData[] = [
  { name: "항공료", value: 52, color: "#FF6384" },
  { name: "선물", value: 25, color: "#36A2EB" },
  { name: "쇼핑", value: 13, color: "#FFCE56" },
  { name: "투어", value: 10, color: "#4BC0C0" },
];

const dataPaymentMethods: ChartData[] = [
  { name: "현금", value: 60, color: "#4CAF50" },
  { name: "카드", value: 40, color: "#007BFF" },
];

const dataCurrencies: ChartData[] = [
  { name: "VND", value: 90, color: "#FF6384" },
  { name: "KRW", value: 10, color: "#36A2EB" },
];

const ExpenseChart = () => {
  const [activeTab, setActiveTab] = useState("카테고리 별");

  const renderChartData = (): ChartData[] => {
    switch (activeTab) {
      case "카테고리 별":
        return dataCategories;
      case "지출수단 별":
        return dataPaymentMethods;
      case "통화별":
        return dataCurrencies;
      default:
        return [];
    }
  };

  const renderTotal = () => {
    if (activeTab === "통화별") {
      return (
        <div className="text-center font-bold mt-4 text-lg">
          총 624,822.36 원
        </div>
      );
    }
    return null;
  };

  return (
    <div className="p-4">
      <Header />
      <div className="flex justify-center space-x-4 mb-4">
        <Button
          variant={activeTab === "카테고리 별" ? "default" : "outline"}
          onClick={() => setActiveTab("카테고리 별")}
        >
          카테고리 별
        </Button>
        <Button
          variant={activeTab === "지출수단 별" ? "default" : "outline"}
          onClick={() => setActiveTab("지출수단 별")}
        >
          지출수단 별
        </Button>
        <Button
          variant={activeTab === "통화별" ? "default" : "outline"}
          onClick={() => setActiveTab("통화별")}
        >
          통화별
        </Button>
      </div>

      <Card>
        <CardContent>
          <PieChart width={300} height={300}>
            <Pie
              data={renderChartData()}
              dataKey="value"
              nameKey="name"
              cx="50%"
              cy="50%"
              outerRadius={100}
              fill="#8884d8"
              label={(entry: ChartData) => `${entry.name} ${entry.value}%`}
            >
              {renderChartData().map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
            </Pie>
          </PieChart>

          <div className="mt-4 space-y-2">
            {renderChartData().map((entry, index) => (
              <div key={index} className="flex justify-between items-center">
                <div className="flex items-center space-x-2">
                  <div
                    className="w-4 h-4 rounded"
                    style={{ backgroundColor: entry.color }}
                  ></div>
                  <span>{entry.name}</span>
                </div>
                <span>{entry.value}%</span>
              </div>
            ))}
          </div>

          {renderTotal()}
        </CardContent>
      </Card>
    </div>
  );
};

export default ExpenseChart;
