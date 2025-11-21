import { useEffect, useState, useCallback } from "react";
import { Table, Tag, Button, Space, message } from "antd";
import api from "../../../utils/api";

export default function UserList() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  // ------------------------------
  // FIX: loadUsers wrapped in useCallback
  // ------------------------------
  const loadUsers = useCallback(async () => {
    try {
      setLoading(true);

      const res = await api.get("/api/users");

      // FIX: Only set state AFTER api call (async)
      setUsers(res.data);
    } catch (err) {
      console.error(err);
      message.error("Failed to load users");
    } finally {
      setLoading(false);
    }
  }, []);

  // ------------------------------
  // Run loadUsers inside effect (SAFE)
  // ------------------------------
  useEffect(() => {
    loadUsers();
  }, [loadUsers]);

  // ------------------------------
  // Toggle status (ban/unban)
  // ------------------------------
  const toggleStatus = async (id, status) => {
    try {
      await api.put(`/api/users/${id}/status`, { status });
      message.success("Updated user status");
      loadUsers();
    } catch (err) {
      console.error(err);
      message.error("Failed to update user");
    }
  };

  const columns = [
    { title: "Full Name", dataIndex: "fullName" },
    { title: "Email", dataIndex: "email" },
    {
      title: "Role",
      dataIndex: "role",
      render: (role) => <Tag color="blue">{role}</Tag>,
    },
    {
      title: "Status",
      dataIndex: "status",
      render: (s) =>
        s === 1 ? (
          <Tag color="green">ACTIVE</Tag>
        ) : (
          <Tag color="red">BANNED</Tag>
        ),
    },
    {
      title: "Actions",
      render: (_, record) => (
        <Space>
          {record.status === 1 ? (
            <Button danger onClick={() => toggleStatus(record.id, 0)}>
              Ban
            </Button>
          ) : (
            <Button type="primary" onClick={() => toggleStatus(record.id, 1)}>
              Unban
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h1 style={{ marginBottom: 20 }}>Users Management</h1>

      <Table
        rowKey="id"
        columns={columns}
        dataSource={users}
        loading={loading}
      />
    </div>
  );
}
