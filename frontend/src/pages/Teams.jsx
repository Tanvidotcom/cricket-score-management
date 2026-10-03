
import { useCallback, useEffect, useState } from "react";
import { Pencil, Plus, RefreshCw, Shield, Trash2 } from "lucide-react";
import {
  createTeam,
  deleteTeam,
  getTeams,
  updateTeam,
} from "../services/matchService";

const emptyForm = { name: "", shortName: "", country: "" };

function Teams() {
  const [teams, setTeams] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const loadTeams = useCallback(async () => {
    try {
      setError("");
      setTeams(await getTeams());
    } catch {
      setError("Could not load teams. Check that the backend is running.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadTeams();
  }, [loadTeams]);

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError("");
    setSuccess("");

    try {
      if (editingId !== null) {
        await updateTeam(editingId, form);
        setSuccess("Team updated successfully.");
      } else {
        await createTeam(form);
        setSuccess("Team added successfully.");
      }

      resetForm();
      await loadTeams();
    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to save team. Check the details and try again."
      );
    } finally {
      setSaving(false);
    }
  };

  const handleEdit = (team) => {
    setEditingId(team.id);
    setForm({
      name: team.name || "",
      shortName: team.shortName || "",
      country: team.country || "",
    });
    setError("");
    setSuccess("");
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (team) => {
    if (!window.confirm(`Delete ${team.name}?`)) return;

    setError("");
    setSuccess("");

    try {
      await deleteTeam(team.id);
      setSuccess("Team deleted successfully.");
      await loadTeams();
    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to delete team. It may have registered players."
      );
    }
  };

  return (
    <main className="page-container management-page">
      <section className="management-heading">
        <span className="eyebrow">SQUAD DIRECTORY</span>
        <h1>Team management</h1>
        <p>Organize teams and maintain your cricket database.</p>
      </section>

      <section className="management-layout">
        <form className="management-form" onSubmit={handleSubmit}>
          <div className="management-form-title">
            <span className="management-icon"><Shield size={20} /></span>
            <div>
              <h2>{editingId !== null ? "Edit team" : "Add a team"}</h2>
              <p>Enter the team details below.</p>
            </div>
          </div>

          <label>
            Team name *
            <input
              required
              maxLength={100}
              placeholder="e.g. India"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
          </label>

          <label>
            Short name
            <input
              maxLength={20}
              placeholder="e.g. IND"
              value={form.shortName}
              onChange={(e) => setForm({ ...form, shortName: e.target.value })}
            />
          </label>

          <label>
            Country
            <input
              maxLength={80}
              placeholder="e.g. India"
              value={form.country}
              onChange={(e) => setForm({ ...form, country: e.target.value })}
            />
          </label>

          {error && <p className="form-feedback error-text">{error}</p>}
          {success && <p className="form-feedback success-text">{success}</p>}

          <button className="primary-button" type="submit" disabled={saving}>
            <Plus size={17} />
            {saving ? "Saving..." : editingId !== null ? "Save changes" : "Add team"}
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
              <h2>Registered teams</h2>
              <p>{teams.length} team{teams.length === 1 ? "" : "s"} in database</p>
            </div>
            <button className="icon-button" onClick={loadTeams} title="Refresh">
              <RefreshCw size={17} />
            </button>
          </div>

          {loading ? (
            <div className="management-empty">Loading teams...</div>
          ) : teams.length === 0 ? (
            <div className="management-empty">
              <Shield size={30} />
              <strong>No teams added yet</strong>
              <span>Use the form to register your first team.</span>
            </div>
          ) : (
            <div className="management-table-wrap">
              <table className="management-table">
                <thead>
                  <tr>
                    <th>Team</th>
                    <th>Short name</th>
                    <th>Country</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {teams.map((team) => (
                    <tr key={team.id}>
                      <td className="table-primary">{team.name}</td>
                      <td>{team.shortName || "—"}</td>
                      <td>{team.country || "—"}</td>
                      <td>
                        <div className="table-actions">
                          <button className="icon-button" title="Edit"
                            onClick={() => handleEdit(team)}>
                            <Pencil size={16} />
                          </button>
                          <button className="icon-button delete-action" title="Delete"
                            onClick={() => handleDelete(team)}>
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

export default Teams;