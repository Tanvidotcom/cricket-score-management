
import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8081/api",
  timeout: 15000,
});

export const getMatches = async () => {
  const response = await API.get("/matches");
  return response.data;
};

export const getMatchById = async (id) => {
  const response = await API.get(`/matches/${id}`);
  return response.data;
};

export const getMatchScores = async (id) => {
  const response = await API.get(`/matches/${id}/scores`);
  return response.data;
};

export const syncMatches = async () => {
  const response = await API.post("/matches/sync");
  return response.data;
};

export const getTeams = async () => {
  const response = await API.get("/teams");
  return response.data;
};

export const createTeam = async (team) => {
  const response = await API.post("/teams", team);
  return response.data;
};

export const updateTeam = async (id, team) => {
  const response = await API.put(`/teams/${id}`, team);
  return response.data;
};

export const deleteTeam = async (id) => {
  const response = await API.delete(`/teams/${id}`);
  return response.data;
};

export const getPlayers = async () => {
  const response = await API.get("/players");
  return response.data;
};

export const createPlayer = async (player) => {
  const response = await API.post("/players", player);
  return response.data;
};

export const updatePlayer = async (id, player) => {
  const response = await API.put(`/players/${id}`, player);
  return response.data;
};

export const deletePlayer = async (id) => {
  const response = await API.delete(`/players/${id}`);
  return response.data;
};