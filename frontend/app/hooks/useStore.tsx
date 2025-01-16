// import {createContext, type ReactNode, useContext} from "react";
// import {drawingStore, type DrawingStore} from "~/stores/DrawingStore";
//
// const StoreContext = createContext<DrawingStore>(drawingStore);
//
// export const useStore = () => useContext(StoreContext);
//
// export const StoreProvider = ({ children }: { children: ReactNode }) => {
//     return (
//         <StoreContext.Provider value={drawingStore}>
//             {children}
//         </StoreContext.Provider>
//     );
// }