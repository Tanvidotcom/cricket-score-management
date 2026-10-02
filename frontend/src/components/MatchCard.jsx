
import { Link } from "react-router-dom";
import { ChevronRight, MapPin } from "lucide-react";

function getStatusType(status = "") {
  const value = status.toLowerCase();

  if (
    value.includes("won") ||
    value.includes("draw") ||
    value.includes("tied") ||
    value.includes("no result") ||
    value.includes("abandoned")
  ) {
    return "completed";
  }

  if (
    value.includes("live") ||
    value.includes("need") ||
    value.includes("trail") ||
    value.includes("lead") ||
    value.includes("innings break") ||
    value.includes("in progress")
  ) {
    return "live";
  }

  return "upcoming";
}

function MatchCard({ match }) {
  const statusType = getStatusType(match.status);

  return (
    <article className="match-card">
      <div className="match-card-top">
        <span className="match-type">
          {(match.matchType || "Cricket").toUpperCase()}
        </span>

        <span className={`match-status ${statusType}`}>
          {statusType === "live" && <span className="status-dot" />}
          {statusType === "live"
            ? "LIVE"
            : statusType === "completed"
              ? "COMPLETED"
              : "UPCOMING"}
        </span>
      </div>

      <h3 className="match-title">
        {match.matchTitle || "Cricket Match"}
      </h3>

      <div className="teams">
        <div className="team-row">
          <span className="team-name">
            {match.team1Name || "Team 1"}
          </span>
          <strong className="team-score">
            {match.team1Score || "—"}
          </strong>
        </div>

        <div className="team-row">
          <span className="team-name">
            {match.team2Name || "Team 2"}
          </span>
          <strong className="team-score">
            {match.team2Score || "—"}
          </strong>
        </div>
      </div>

      <p className="match-result">
        {match.status || "Match details unavailable"}
      </p>

      <div className="match-card-bottom">
        <span className="match-venue">
          <MapPin size={14} />
          {match.venue || "Venue unavailable"}
        </span>

        <Link to={`/matches/${match.id}`} className="details-link">
          Details <ChevronRight size={16} />
        </Link>
      </div>
    </article>
  );
}

export default MatchCard;