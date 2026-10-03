
import { NavLink } from "react-router-dom";
import { Activity, Trophy, Shield, Users } from "lucide-react";

function Navbar() {
  return (
    <header className="navbar">
      <div className="navbar-inner">
        <NavLink to="/" className="brand">
          <span className="brand-icon">
            <Trophy size={21} />
          </span>
          <span>Crick<span className="brand-highlight">Info</span></span>
        </NavLink>

        <nav className="nav-links">
          <NavLink to="/" end>
            Home
          </NavLink>
          <NavLink to="/live">
            <Activity size={16} />
            Live Scores
          </NavLink>
          <NavLink to="/teams">
            <Shield size={16} />
            Teams
          </NavLink>
          <NavLink to="/players">
            <Users size={16} />
            Players
          </NavLink>
        </nav>

        <div className="nav-status">
          <span className="status-dot" />
          Score Center
        </div>
      </div>
    </header>
  );
}

export default Navbar;