import { BrowserRouter, Routes, Route } from "react-router-dom";

import UserLayout from "../layouts/UserLayout";
import OwnerLayout from "../layouts/OwnerLayout";
import AdminLayout from "../layouts/AdminLayout";

import HomePage from "../pages/user/HomePage";
import CartPage from "../pages/user/CartPage";
import CheckoutPage from "../pages/user/CheckoutPage";

import OwnerDashboard from "../pages/owner/Dashboard";
import OwnerMenu from "../pages/owner/Menu";
import OwnerOrders from "../pages/owner/Orders";

import AdminDashboard from "../pages/admin/Dashboard";
import AdminDrones from "../pages/admin/Drones";
import AdminOrders from "../pages/admin/Orders";

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        {/* USER */}
        <Route path="/" element={<UserLayout />}>
          <Route index element={<HomePage />} />
          <Route path="cart" element={<CartPage />} />
          <Route path="checkout" element={<CheckoutPage />} />
        </Route>

        {/* OWNER */}
        <Route path="/owner" element={<OwnerLayout />}>
          <Route index element={<OwnerDashboard />} />
          <Route path="menu" element={<OwnerMenu />} />
          <Route path="orders" element={<OwnerOrders />} />
        </Route>

        {/* ADMIN */}
        <Route path="/admin" element={<AdminLayout />}>
          <Route index element={<AdminDashboard />} />
          <Route path="drones" element={<AdminDrones />} />
          <Route path="orders" element={<AdminOrders />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
