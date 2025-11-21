import { useEffect, useState, useCallback } from "react";
import {
  Table,
  Tag,
  Button,
  Space,
  message,
  Modal,
  Form,
  Input,
  Select,
} from "antd";
import api from "../../../utils/api";

export default function UserList() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  // Modal states
  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);

  const [form] = Form.useForm();
  const [editForm] = Form.useForm();

  const loadUsers = useCallback(async () => {
    try {
      setLoading(true);
      const res = await api.get("/api/users");
      setUsers(res.data);
    } catch (err) {
      console.error(err);
      message.error("Failed to load users");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadUsers();
  }, [loadUsers]);

  // ------------------------------
  // Ban / Unban
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

  // ------------------------------
  // Create user
  // ------------------------------
  const handleAddUser = async () => {
    try {
      const values = await form.validateFields();
      await api.post("/api/users", values);
      message.success("User created successfully");
      form.resetFields();
      setIsAddOpen(false);
      loadUsers();
    } catch (err) {
      console.error(err);
      message.error("Failed to create user");
    }
  };

  // ------------------------------
  // Update user
  // ------------------------------
  const handleEditUser = async () => {
    try {
      const values = await editForm.validateFields();
      await api.put(`/api/users/${editingUser._id}`, values);
      message.success("User updated");
      setIsEditOpen(false);
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
          {/* Ban / Unban */}
          {record.status === 1 ? (
            <Button danger onClick={() => toggleStatus(record._id, 0)}>
              Ban
            </Button>
          ) : (
            <Button type="primary" onClick={() => toggleStatus(record._id, 1)}>
              Unban
            </Button>
          )}

          {/* Edit */}
          <Button
            onClick={() => {
              setEditingUser(record);
              editForm.setFieldsValue(record);
              setIsEditOpen(true);
            }}
          >
            Edit
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h1 style={{ marginBottom: 20 }}>Users Management</h1>

      <Button
        type="primary"
        onClick={() => setIsAddOpen(true)}
        style={{ marginBottom: 20 }}
      >
        Add User
      </Button>

      <Table
        rowKey="_id"
        columns={columns}
        dataSource={users}
        loading={loading}
      />

      {/* Add User Modal */}
      <Modal
        title="Add User"
        open={isAddOpen}
        onCancel={() => setIsAddOpen(false)}
        onOk={handleAddUser}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            label="Full Name"
            name="fullName"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item label="Email" name="email" rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <Form.Item label="Phone" name="phone">
            <Input />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true }]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item label="Role" name="role" rules={[{ required: true }]}>
            <Select
              options={[
                { label: "ADMIN", value: "ADMIN" },
                { label: "CUSTOMER", value: "CUSTOMER" },
                { label: "RESTAURANT_OWNER", value: "RESTAURANT_OWNER" },
              ]}
            />
          </Form.Item>
        </Form>
      </Modal>

      {/* Edit User Modal */}
      <Modal
        title="Edit User"
        open={isEditOpen}
        onCancel={() => setIsEditOpen(false)}
        onOk={handleEditUser}
      >
        <Form form={editForm} layout="vertical">
          <Form.Item
            label="Full Name"
            name="fullName"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item label="Email" name="email" rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <Form.Item label="Phone" name="phone">
            <Input />
          </Form.Item>

          <Form.Item label="Role" name="role" rules={[{ required: true }]}>
            <Select
              options={[
                { label: "ADMIN", value: "ADMIN" },
                { label: "CUSTOMER", value: "CUSTOMER" },
                { label: "RESTAURANT_OWNER", value: "RESTAURANT_OWNER" },
              ]}
            />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
