import { createSlice } from "@reduxjs/toolkit";
interface alertControlState {
  alertState: boolean;
}

const initialState: alertControlState = {
  alertState: false,
};

// 알림 바 컨트롤 글로벌 상태관리
const AlertControlSlice = createSlice({
  name: "AlertControl",
  initialState,
  reducers: {
    ChangeAlertState(state) {
      state.alertState = !state.alertState;
    },
    ChangeAlertStates(state) {
      state.alertState = !state.alertState;
    },
  },
});

export const { ChangeAlertState } = AlertControlSlice.actions;
export default AlertControlSlice.reducer;
