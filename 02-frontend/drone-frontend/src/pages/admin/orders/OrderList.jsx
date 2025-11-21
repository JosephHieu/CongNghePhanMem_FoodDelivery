import { useEffect, useState, useCallback } from "react";
import { Table, Tag, Button, Space, Modal, message } from "antd";
import api from "../../../utils/api";

export default function OrderList() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);

  const [detailOpen, setDetailOpen] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);

  // ===============================
  // LOAD ORDERS
  // ===============================
  const loadOrders = useCallback(async () => {
    try {
      setLoading(true);
      const res = await api.get("/api/orders");
      setOrders(res.data);
    } catch (err) {
      message.error("Failed to load orders" + err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadOrders();
  }, [loadOrders]);

  // ===============================
  // UPDATE ORDER STATUS
  // ===============================
  const updateStatus = async (id, status) => {
    try {
      await api.put(`/api/orders/${id}/status`, { status });
      message.success("Order updated");
      loadOrders();
    } catch (err) {
      message.error("Failed to update" + err.message);
    }
  };

  // ===============================
  // MARK DELIVERED
  // ===============================
  const markDelivered = async (id) => {
    try {
      await api.post(`/api/orders/${id}/delivered`);
      message.success("Order marked as delivered");
      loadOrders();
    } catch (err) {
      message.error("Failed to mark delivered" + err.message);
    }
  };

  // ===============================
  // STATUS TAG COLOR
  // ===============================
  const statusColor = {
    PENDING_PAYMENT: "orange",
    PAID: "blue",
    PREPARING: "purple",
    READY_FOR_DISPATCH: "cyan",
    DELIVERING: "gold",
    COMPLETED: "green",
    CANCELLED: "red",
    PAYMENT_FAILED: "red",
  };

  const columns = [
    { title: "Order ID", dataIndex: "id" },
    { title: "User ID", dataIndex: "userId" },
    { title: "Restaurant ID", dataIndex: "restaurantId" },
    {
      title: "Total",
      dataIndex: "totalPrice",
      render: (v) => `${v.toLocaleString()}đ`,
    },
    {
      title: "Status",
      dataIndex: "status",
      render: (s) => <Tag color={statusColor[s] || "default"}>{s}</Tag>,
    },
    {
      title: "Actions",
      render: (_, r) => (
        <Space>
          {/* Detail */}
          <Button
            onClick={() => {
              setSelectedOrder(r);
              setDetailOpen(true);
            }}
          >
            Details
          </Button>

          {/* Update Status */}
          {r.status !== "COMPLETED" && (
            <Button
              type="primary"
              onClick={() => updateStatus(r.id, "PREPARING")}
            >
              Preparing
            </Button>
          )}

          {r.status === "PREPARING" && (
            <Button onClick={() => updateStatus(r.id, "READY_FOR_DISPATCH")}>
              Ready
            </Button>
          )}

          {r.status === "READY_FOR_DISPATCH" && (
            <Button onClick={() => updateStatus(r.id, "DELIVERING")}>
              Delivering
            </Button>
          )}

          {r.status === "DELIVERING" && (
            <Button type="primary" onClick={() => markDelivered(r.id)}>
              Mark Delivered
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h1>Order Management</h1>

      <Table
        rowKey="id"
        dataSource={orders}
        columns={columns}
        loading={loading}
        style={{ marginTop: 20 }}
      />

      {/* Order Detail Modal */}
      <Modal
        title="Order Details"
        open={detailOpen}
        onCancel={() => setDetailOpen(false)}
        footer={null}
      >
        {selectedOrder && (
          <div>
            <p>
              <b>User ID:</b> {selectedOrder.userId}
            </p>
            <p>
              <b>Restaurant ID:</b> {selectedOrder.restaurantId}
            </p>
            <p>
              <b>Total:</b> {selectedOrder.totalPrice.toLocaleString()}đ
            </p>
            <p>
              <b>Payment ID:</b> {selectedOrder.paymentId || "N/A"}
            </p>
            <p>
              <b>Status:</b> {selectedOrder.status}
            </p>

            <h3>Items:</h3>
            <ul>
              {selectedOrder.items.map((item, i) => (
                <li key={i}>
                  {item.menuItemId} — Qty: {item.quantity} —{" "}
                  {(item.price * item.quantity).toLocaleString()}đ
                </li>
              ))}
            </ul>
          </div>
        )}
      </Modal>
    </div>
  );
}
