import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  value: {
    amount: "",
    currency: "",
    paymentType: "",
    date: "",
    selectedUser: { name: "", email: "" },
    img: "",
  },
};

const SpendDataSlice = createSlice({
  name: "SpendData",
  initialState,
  reducers: {
    SaveSpendData(state, action) {
      state.value = action.payload;
    },
  },
});

export const { SaveSpendData } = SpendDataSlice.actions;
export default SpendDataSlice.reducer;
