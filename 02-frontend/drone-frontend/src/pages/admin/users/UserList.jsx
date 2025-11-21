import { useEffect, useState } from "react";
import { Table, Tag, Button, Space, message } from "antd";
import api from "../../../utils/api";

export default function UserList() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  // 1. Load danh sách users
  const loadUsers = async () => {
    setLoading(true);
    try {
      const res = await api.get("/api/users");
      setUsers(res.data);
    } catch (err) {
      console.error(err);
      message.error("Failed to load users");
    }
    setLoading(false);
  };

  useEffect(() => {
    let active = true;

    const fetch = async () => {
      setLoading(true);
      try {
        const res = await api.get("/api/users");
        if (active) {
          setUsers(res.data);
        }
      } catch (err) {
        console.error(err);
      } finally {
        if (active) setLoading(false);
      }
    };

    fetch();

    return () => {
      active = false;
    };
  }, []);

  // 2. Ban / Unban
  const toggleStatus = async (id, status) => {
    try {
      await api.put(`/api/users/me`, { status }); // backend bạn có updateProfile

      message.success("Updated user");
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
