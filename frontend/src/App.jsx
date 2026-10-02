
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import MatchDetails from "./pages/MatchDetails";
import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <div className="app-shell">
        <Navbar />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route
            path="/live"
            element={<Home key="live" initialFilter="live" />}
          />
          <Route path="/matches/:id" element={<MatchDetails />} />
          <Route path="*" element={<Home />} />
        </Routes>

        <footer className="footer">
          <div className="footer-inner">
            <span className="footer-brand">CrickInfo</span>
            <span>Real-time cricket score management</span>
            <span>Developed by TANVI NANAWARE | Roll No. 44</span>
          </div>
        </footer>
      </div>
    </BrowserRouter>
  );
}


export default App;