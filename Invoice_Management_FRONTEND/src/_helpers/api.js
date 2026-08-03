const BASE_URL = import.meta.env.VITE_BASE_URL;

export const setToken = (token) => {
  localStorage.setItem('token', token);
};

export const getToken = () => {
  return localStorage.getItem('token');
};

export const clearToken = () => {
  localStorage.removeItem('token');
};

export async function api(endpoint, options = {}) {
	const token = getToken();
	console.log("Das ist mein token",token);
  const headers = { 'Content-Type': 'application/json' };
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  if (options.body) {
    options.body = JSON.stringify(options.body);
  }
  const response = await fetch(`${BASE_URL}${endpoint}`, { ...options, headers });
  if (response.status === 204) {
    return null;
  }
  const text = await response.text();
  if (!response.ok) {
    throw new Error(text);
  }
  if (!text) {
    return null;
  }
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}
