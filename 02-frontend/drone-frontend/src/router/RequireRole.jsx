import { useContext } from "react";
import { Navigate, Outlet } from "react-router-dom";
import AuthContext from "../context/AuthContext";

export default function RequireRole({ role }) {
  const { user } = useContext(AuthContext);

  if (!user) return <Navigate to="/login" replace />;

  if (user.role !== role) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}
