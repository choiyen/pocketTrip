import { configureStore } from "@reduxjs/toolkit";
import counterReducer from "../slices/counterSlice";

const store = configureStore({
  reducer: {
    counter: counterReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export default store;
export type AppDispatch = typeof store.dispatch;
