
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