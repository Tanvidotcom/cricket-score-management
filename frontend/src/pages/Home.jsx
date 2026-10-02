
import { useCallback, useEffect, useMemo, useState } from "react";
import { RefreshCw, Search, Trophy } from "lucide-react";
import MatchCard from "../components/MatchCard";
import { getMatches } from "../services/matchService";

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

function Home({ initialFilter = "all" }) {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const [filter, setFilter] = useState(initialFilter);
  const [lastUpdated, setLastUpdated] = useState(null);

  const loadMatches = useCallback(async () => {
    try {
      setError("");
      const data = await getMatches();
      setMatches(Array.isArray(data) ? data : []);
      setLastUpdated(new Date());
    } catch (err) {
      setError(
        "Unable to connect to the backend. Make sure Spring Boot is running on port 8081."
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadMatches();

    // Refresh from the local backend every 30 seconds.
    const interval = setInterval(loadMatches, 30000);

    return () => clearInterval(interval);
  }, [loadMatches]);

  const filteredMatches = useMemo(() => {
    return matches.filter((match) => {
      const text = [
        match.matchTitle,
        match.team1Name,
        match.team2Name,
        match.venue,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      const matchesSearch = text.includes(search.toLowerCase());
      const status = getStatusType(match.status);

      const matchesFilter =
        filter === "all" || filter === status;

      return matchesSearch && matchesFilter;
    });
  }, [matches, search, filter]);

  const liveCount = matches.filter(
    (match) => getStatusType(match.status) === "live"
  ).length;

  return (
    <main className="page-container">
      <section className="hero">
        <div className="hero-content">
          <span className="eyebrow">
            <Trophy size={15} /> YOUR CRICKET SCORE CENTER
          </span>

          <h1>
            Every match.
            <br />
            <span>Every moment.</span>
          </h1>

          <p>
            Follow cricket matches, explore scorecards and
            stay up to date with match results.
          </p>

          <div className="hero-stats">
            <div>
              <strong>{matches.length}</strong>
              <span>Matches tracked</span>
            </div>
            <div>
              <strong>{liveCount}</strong>
              <span>Live matches</span>
            </div>
          </div>
        </div>

        <div className="hero-art" aria-hidden="true">
          <div className="hero-circle circle-one" />
          <div className="hero-circle circle-two" />
          <div className="cricket-ball">✦</div>
          <div className="hero-art-label">CRICK INFO</div>
        </div>
      </section>

      <section className="matches-section">
        <div className="section-heading">
          <div>
            <span className="eyebrow">MATCH CENTER</span>
            <h2>Explore matches</h2>
            <p className="section-description">
              Scores and match details saved in the database.
            </p>
          </div>

          <button
            className="refresh-button"
            onClick={loadMatches}
            disabled={loading}
          >
            <RefreshCw size={16} />
            Refresh
          </button>
        </div>

        <div className="match-controls">
          <div className="search-box">
            <Search size={18} />
            <input
              type="text"
              placeholder="Search teams, matches or venues..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          <div className="filter-tabs">
            {["all", "live", "upcoming", "completed"].map((item) => (
              <button
                key={item}
                className={filter === item ? "filter-tab active" : "filter-tab"}
                onClick={() => setFilter(item)}
              >
                {item.charAt(0).toUpperCase() + item.slice(1)}
              </button>
            ))}
          </div>
        </div>

        {lastUpdated && (
          <p className="updated-text">
            Last checked: {lastUpdated.toLocaleTimeString()}
          </p>
        )}

        {loading && (
          <div className="message-panel">
            <div className="loader" />
            <p>Loading match data...</p>
          </div>
        )}

        {!loading && error && (
          <div className="message-panel error-panel">
            <h3>Backend connection issue</h3>
            <p>{error}</p>
            <button className="primary-button" onClick={loadMatches}>
              Try again
            </button>
          </div>
        )}

        {!loading && !error && filteredMatches.length === 0 && (
          <div className="message-panel">
            <Trophy size={32} />
            <h3>No matches found</h3>
            <p>
              {matches.length === 0
                ? "No matches have been synchronized yet."
                : "Try another search or filter."}
            </p>
          </div>
        )}

        {!loading && !error && filteredMatches.length > 0 && (
          <div className="match-grid">
            {filteredMatches.map((match) => (
              <MatchCard key={match.id} match={match} />
            ))}
          </div>
        )}
      </section>
    </main>
  );
}

export default Home;