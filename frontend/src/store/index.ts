import { configureStore } from "@reduxjs/toolkit";
import counterReducer from "../slices/counterSlice";
import currentPageReducer from "../slices/currentPageSlice";
import ModalControlReducer from "../slices/ModalControlSlice";

const store = configureStore({
  reducer: {
    counter: counterReducer,
    currentPage: currentPageReducer,
    modalControl: ModalControlReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export default store;
export type AppDispatch = typeof store.dispatch;
