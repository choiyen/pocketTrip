import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface TravelData {
  name: string;
  selectedCountry: string;
  budget: number;
  startDate: string;
  endDate: string;
  ImgArr: string[];
  bgImg: string;
}

const initialState: TravelData = {
  name: "",
  selectedCountry: "",
  budget: 0,
  startDate: "",
  endDate: "",
  ImgArr: [],
  bgImg: "",
};

const travelSlice = createSlice({
  name: "travel",
  initialState,
  reducers: {
    setTravelData: (state, action: PayloadAction<TravelData>) => {
      return action.payload; // 새로운 여행 데이터로 상태 업데이트
    },
  },
});

export const { setTravelData } = travelSlice.actions;
export default travelSlice.reducer;
