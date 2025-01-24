import { createSlice } from "@reduxjs/toolkit";
interface alertControlState {
  alertState: boolean;
}

const initialState: alertControlState = {
  alertState: false,
};
const AlertControlSlice = createSlice({
  name: "AlertControl",
  initialState,
  reducers: {
    ChangeAlertState(state) {
      state.alertState = !state.alertState;
    },
  },
});

export const { ChangeAlertState } = AlertControlSlice.actions;
export default AlertControlSlice.reducer;
