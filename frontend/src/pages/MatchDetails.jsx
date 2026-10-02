
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { ArrowLeft, CalendarDays, MapPin, Trophy } from "lucide-react";
import {
  getMatchById,
  getMatchScores,
} from "../services/matchService";

function MatchDetails() {
  const { id } = useParams();
  const [match, setMatch] = useState(null);
  const [scores, setScores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;

    async function loadDetails() {
      try {
        setLoading(true);
        setError("");

        const [matchData, scoreData] = await Promise.all([
          getMatchById(id),
          getMatchScores(id),
        ]);

        if (active) {
          setMatch(matchData);
          setScores(Array.isArray(scoreData) ? scoreData : []);
        }
      } catch (err) {
        if (active) {
          setError("Unable to load this match. It may not exist.");
        }
      } finally {
        if (active) setLoading(false);
      }
    }

    loadDetails();

    return () => {
      active = false;
    };
  }, [id]);

  if (loading) {
    return (
      <main className="page-container">
        <div className="message-panel">
          <div className="loader" />
          <p>Loading match details...</p>
        </div>
      </main>
    );
  }

  if (error || !match) {
    return (
      <main className="page-container">
        <div className="message-panel error-panel">
          <h3>Match unavailable</h3>
          <p>{error || "No match data was found."}</p>
          <Link to="/" className="primary-button">
            Back to matches
          </Link>
        </div>
      </main>
    );
  }

  const innings = [...scores].sort(
    (a, b) => a.inningsNumber - b.inningsNumber
  );

  return (
    <main className="page-container details-page">
      <Link to="/" className="back-link">
        <ArrowLeft size={17} /> Back to matches
      </Link>

      <section className="details-hero">
        <span className="eyebrow">
          {(match.matchType || "CRICKET").toUpperCase()} MATCH
        </span>

        <h1>{match.matchTitle || "Match details"}</h1>

        <div className="details-meta">
          <span>
            <MapPin size={16} /> {match.venue || "Venue unavailable"}
          </span>
          <span>
            <CalendarDays size={16} /> {match.matchDate || "Date unavailable"}
          </span>
        </div>

        <div className="details-teams">
          <div className="details-team">
            <span className="team-initial">
              {(match.team1Name || "T1").slice(0, 3).toUpperCase()}
            </span>
            <h2>{match.team1Name || "Team 1"}</h2>
            <strong>{match.team1Score || "—"}</strong>
          </div>

          <span className="versus">VS</span>

          <div className="details-team">
            <span className="team-initial">
              {(match.team2Name || "T2").slice(0, 3).toUpperCase()}
            </span>
            <h2>{match.team2Name || "Team 2"}</h2>
            <strong>{match.team2Score || "—"}</strong>
          </div>
        </div>

        <div className="result-banner">
          <Trophy size={17} />
          {match.status || "Status unavailable"}
        </div>
      </section>

      <section className="score-history">
        <div className="section-heading">
          <div>
            <span className="eyebrow">INNINGS</span>
            <h2>Score history</h2>
          </div>
        </div>

        {innings.length === 0 ? (
          <div className="message-panel">
            <p>No innings score snapshots are available yet.</p>
          </div>
        ) : (
          <div className="innings-list">
            {innings.map((score) => (
              <article className="innings-card" key={score.id}>
                <div>
                  <span className="innings-label">
                    INNINGS {score.inningsNumber}
                  </span>
                  <h3>{score.battingTeam || "Batting team"}</h3>
                </div>

                <div className="innings-score">
                  <strong>
                    {score.runs}/{score.wickets}
                  </strong>
                  <span>{score.overs} overs</span>
                </div>

                <div className="innings-extra">
                  <span>Run rate</span>
                  <strong>{score.runRate ?? "—"}</strong>
                </div>

                {score.target != null && (
                  <div className="innings-extra">
                    <span>Target</span>
                    <strong>{score.target}</strong>
                  </div>
                )}

                <p className="snapshot-time">
                  Updated:{" "}
                  {score.fetchedAt
                    ? new Date(score.fetchedAt).toLocaleString()
                    : "Unavailable"}
                </p>
              </article>
            ))}
          </div>
        )}
      </section>
    </main>
  );
}

export default MatchDetails;