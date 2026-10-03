
import { useCallback, useEffect, useState } from "react";
import { Pencil, Plus, RefreshCw, Users, Trash2 } from "lucide-react";
import {
  createPlayer,
  deletePlayer,
  getPlayers,
  getTeams,
  updatePlayer,
} from "../services/matchService";

const emptyForm = {
  name: "",
  role: "Batter",
  battingStyle: "",
  bowlingStyle: "",
  jerseyNumber: "",
  teamId: "",
};

function Players() {
  const [players, setPlayers] = useState([]);
  const [teams, setTeams] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const loadData = useCallback(async () => {
    try {
      setError("");
      const [playerData, teamData] = await Promise.all([
        getPlayers(),
        getTeams(),
      ]);
      setPlayers(playerData);
      setTeams(teamData);
    } catch {
      setError("Could not load players or teams. Check the backend.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError("");
    setSuccess("");

    const payload = {
      ...form,
      jerseyNumber: form.jerseyNumber === "" ? null : Number(form.jerseyNumber),
      teamId: Number(form.teamId),
    };

    try {
      if (editingId !== null) {
        await updatePlayer(editingId, payload);
        setSuccess("Player updated successfully.");
      } else {
        await createPlayer(payload);
        setSuccess("Player added successfully.");
      }

      resetForm();
      await loadData();
    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to save player. Check the details and try again."
      );
    } finally {
      setSaving(false);
    }
  };

  const handleEdit = (player) => {
    setEditingId(player.id);
    setForm({
      name: player.name || "",
      role: player.role || "Batter",
      battingStyle: player.battingStyle || "",
      bowlingStyle: player.bowlingStyle || "",
      jerseyNumber: player.jerseyNumber ?? "",
      teamId: String(player.teamId),
    });
    setError("");
    setSuccess("");
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (player) => {
    if (!window.confirm(`Delete ${player.name}?`)) return;

    setError("");
    setSuccess("");

    try {
      await deletePlayer(player.id);
      setSuccess("Player deleted successfully.");
      await loadData();
    } catch (err) {
      setError(err.response?.data?.message || "Unable to delete player.");
    }
  };

  return (
    <main className="page-container management-page">
      <section className="management-heading">
        <span className="eyebrow">PLAYER DIRECTORY</span>
        <h1>Player management</h1>
        <p>Register players, assign teams and manage squad information.</p>
      </section>

      <section className="management-layout">
        <form className="management-form" onSubmit={handleSubmit}>
          <div className="management-form-title">
            <span className="management-icon"><Users size={20} /></span>
            <div>
              <h2>{editingId !== null ? "Edit player" : "Add a player"}</h2>
              <p>Complete the player profile.</p>
            </div>
          </div>

          <label>
            Player name *
            <input
              required
              maxLength={100}
              placeholder="Enter player name"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
          </label>

          <label>
            Team *
            <select
              required
              value={form.teamId}
              onChange={(e) => setForm({ ...form, teamId: e.target.value })}
            >
              <option value="">Select a team</option>
              {teams.map((team) => (
                <option key={team.id} value={team.id}>{team.name}</option>
              ))}
            </select>
          </label>

          <label>
            Player role
            <select
              value={form.role}
              onChange={(e) => setForm({ ...form, role: e.target.value })}
            >
              <option>Batter</option>
              <option>Bowler</option>
              <option>All-rounder</option>
              <option>Wicketkeeper</option>
            </select>
          </label>

          <label>
            Batting style
            <select
              value={form.battingStyle}
              onChange={(e) => setForm({ ...form, battingStyle: e.target.value })}
            >
              <option value="">Select batting style</option>
              <option>Right-handed</option>
              <option>Left-handed</option>
            </select>
          </label>

          <label>
            Bowling style
            <input
              maxLength={80}
              placeholder="e.g. Right-arm fast"
              value={form.bowlingStyle}
              onChange={(e) => setForm({ ...form, bowlingStyle: e.target.value })}
            />
          </label>

          <label>
            Jersey number
            <input
              type="number"
              min="0"
              max="999"
              placeholder="e.g. 18"
              value={form.jerseyNumber}
              onChange={(e) => setForm({ ...form, jerseyNumber: e.target.value })}
            />
          </label>

          {teams.length === 0 && (
            <p className="form-feedback error-text">
              Add a team before registering a player.
            </p>
          )}
          {error && <p className="form-feedback error-text">{error}</p>}
          {success && <p className="form-feedback success-text">{success}</p>}

          <button
            className="primary-button"
            type="submit"
            disabled={saving || teams.length === 0}
          >
            <Plus size={17} />
            {saving ? "Saving..." : editingId !== null ? "Save changes" : "Add player"}
          </button>

          {editingId !== null && (
            <button className="secondary-button" type="button" onClick={resetForm}>
              Cancel editing
            </button>
          )}
        </form>

        <section className="management-list">
          <div className="management-list-heading">
            <div>
              <h2>Registered players</h2>
              <p>{players.length} player{players.length === 1 ? "" : "s"} in database</p>
            </div>
            <button className="icon-button" onClick={loadData} title="Refresh">
              <RefreshCw size={17} />
            </button>
          </div>

          {loading ? (
            <div className="management-empty">Loading players...</div>
          ) : players.length === 0 ? (
            <div className="management-empty">
              <Users size={30} />
              <strong>No players registered yet</strong>
              <span>Add a team, then register players here.</span>
            </div>
          ) : (
            <div className="management-table-wrap">
              <table className="management-table">
                <thead>
                  <tr>
                    <th>Player</th>
                    <th>Team</th>
                    <th>Role</th>
                    <th>Jersey</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {players.map((player) => (
                    <tr key={player.id}>
                      <td className="table-primary">{player.name}</td>
                      <td>{player.teamName}</td>
                      <td>{player.role || "—"}</td>
                      <td>{player.jerseyNumber ?? "—"}</td>
                      <td>
                        <div className="table-actions">
                          <button className="icon-button" title="Edit"
                            onClick={() => handleEdit(player)}>
                            <Pencil size={16} />
                          </button>
                          <button className="icon-button delete-action" title="Delete"
                            onClick={() => handleDelete(player)}>
                            <Trash2 size={16} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </section>
    </main>
  );
}

export default Players;