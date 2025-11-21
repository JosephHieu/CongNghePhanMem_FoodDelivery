import { useState, useMemo, useCallback } from "react";
import AuthContext from "./AuthContext";
import { jwtDecode } from "jwt-decode";

export default function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("token"));

  // user được derive trực tiếp từ token, KHÔNG dùng useEffect + setUser nữa
  const user = useMemo(() => {
    if (!token) return null;
    try {
      const decoded = jwtDecode(token);
      return {
        id: decoded.userId,
        role: decoded.role,
      };
    } catch {
      return null;
    }
  }, [token]);

  const login = useCallback((jwt) => {
    localStorage.setItem("token", jwt);
    setToken(jwt);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    setToken(null);
  }, []);

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
