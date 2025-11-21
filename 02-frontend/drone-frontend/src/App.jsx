import { Routes, Route } from "react-router-dom";

import RequireAuth from "./router/RequireAuth";
import RequireRole from "./router/RequireRole";

import Login from "./pages/auth/Login";

import CustomerLayout from "./layouts/CustomerLayout";
import OwnerLayout from "./layouts/OwnerLayout";
import AdminLayout from "./layouts/AdminLayout";

import Home from "./pages/customer/Home";
import OwnerDashboard from "./pages/owner/OwnerDashboard";
import AdminDashboard from "./pages/admin/AdminDashboard";

import UserList from "./pages/admin/users/UserList.jsx";
import RestaurantList from "./pages/admin/restaurants/RestaurantList";

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      {/* Customer */}
      <Route element={<RequireAuth />}>
        <Route path="/" element={<CustomerLayout />}>
          <Route index element={<Home />} />
        </Route>
      </Route>

      {/* Owner */}
      <Route element={<RequireRole role="RESTAURANT_OWNER" />}>
        <Route path="/owner" element={<OwnerLayout />}>
          <Route index element={<OwnerDashboard />} />
        </Route>
      </Route>

      {/* Admin */}
      <Route element={<RequireRole role="ADMIN" />}>
        <Route path="/admin" element={<AdminLayout />}>
          <Route index element={<AdminDashboard />} />
          <Route path="users" element={<UserList />} />
          <Route path="restaurants" element={<RestaurantList />} />
        </Route>
      </Route>
    </Routes>
  );
}
